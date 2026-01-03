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
            ?: throw Exception("Not authenticated")

        val otp = (100000..999999).random().toString()

        val otpLink = OtpLink(
            code = otp,
            protectedId = protectedId,
            expiresAt = System.currentTimeMillis() / 1000 + 300
        )

        firestore.collection("otp_links")
            .add(otpLink)
            .await()

        return otp
    }

    suspend fun linkWithOtp(code: String) {
        val monitorId = auth.currentUser?.uid
            ?: throw Exception("Not authenticated")

        val snapshot = firestore.collection("otp_links")
            .whereEqualTo("code", code)
            .whereEqualTo("used", false)
            .get()
            .await()

        if (snapshot.isEmpty)
            throw Exception("Invalid or expired OTP")

        val doc = snapshot.documents.first()
        val otpLink = doc.toObject(OtpLink::class.java)
            ?: throw Exception("Invalid OTP data")

        if (otpLink.expiresAt < System.currentTimeMillis() / 1000)
            throw Exception("OTP expired")

        if (otpLink.protectedId == monitorId)
            throw Exception("You cannot associate yourself")

        // Verificar duplicados
        val existing = firestore.collection("associations")
            .whereEqualTo("monitorId", monitorId)
            .whereEqualTo("protectedId", otpLink.protectedId)
            .whereEqualTo("status", "ACTIVE")
            .get()
            .await()

        if (!existing.isEmpty)
            throw Exception("Association already exists")

        val association = Association(
            monitorId = monitorId,
            protectedId = otpLink.protectedId,
            status = "ACTIVE"
        )

        firestore.collection("associations")
            .add(association)
            .await()

        // Marcar OTP como usado
        doc.reference.update("used", true).await()
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
