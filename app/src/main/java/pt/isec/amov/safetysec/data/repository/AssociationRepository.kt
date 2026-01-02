package pt.isec.amov.safetysec.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pt.isec.amov.safetysec.data.model.Association
import pt.isec.amov.safetysec.data.model.OtpLink

class AssociationRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /* ================= OTP ================= */

    suspend fun generateOtp(): String {
        val protectedId = auth.currentUser?.uid
            ?: throw Exception("Not logged in")

        val otp = (100000..999999).random().toString()

        val otpLink = OtpLink(
            protectedId = protectedId,
            expiresAt = Timestamp.now().seconds + 300
        )

        firestore.collection("otp_links")
            .document(otp)
            .set(otpLink)
            .await()

        return otp
    }

    suspend fun linkWithOtp(otp: String) {
        val monitorId = auth.currentUser?.uid
            ?: throw Exception("Not logged in")

        val docRef = firestore.collection("otp_links").document(otp)
        val doc = docRef.get().await()

        if (!doc.exists()) throw Exception("Invalid OTP")

        val otpLink = doc.toObject(OtpLink::class.java)
            ?: throw Exception("Invalid OTP data")

        if (otpLink.expiresAt < Timestamp.now().seconds)
            throw Exception("OTP expired")

        if (otpLink.protectedId == monitorId)
            throw Exception("You cannot associate yourself")

        // duplicados
        val existing = firestore.collection("associations")
            .whereEqualTo("monitorId", monitorId)
            .whereEqualTo("protectedId", otpLink.protectedId)
            .get()
            .await()

        if (!existing.isEmpty)
            throw Exception("Association already exists")

        val association = Association(
            monitorId = monitorId,
            protectedId = otpLink.protectedId
        )

        firestore.collection("associations")
            .add(association)
            .await()

        docRef.delete().await()
    }

    /* ================= QUERIES ================= */

    suspend fun getProtectedUsers(monitorId: String): List<String> {
        val snapshot = firestore.collection("associations")
            .whereEqualTo("monitorId", monitorId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.getString("protectedId")
        }
    }

    suspend fun getMonitors(protectedId: String): List<String> {
        val snapshot = firestore.collection("associations")
            .whereEqualTo("protectedId", protectedId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.getString("monitorId")
        }
    }

    suspend fun removeAssociation(monitorId: String, protectedId: String) {
        val snapshot = firestore.collection("associations")
            .whereEqualTo("monitorId", monitorId)
            .whereEqualTo("protectedId", protectedId)
            .get()
            .await()

        snapshot.documents.forEach {
            it.reference.delete().await()
        }
    }
}
