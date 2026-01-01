package pt.isec.amov.safetysec.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AssociationRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun generateOtp(): String {
        val uid = auth.currentUser?.uid ?: throw Exception("Not logged in")
        val otp = (100000..999999).random().toString()

        firestore.collection("otp_links")
            .document(otp)
            .set(
                mapOf(
                    "protectedId" to uid,
                    "expiresAt" to Timestamp.now().seconds + 300 // 5 min
                )
            )
            .await()

        return otp
    }

    suspend fun linkWithOtp(otp: String) {
        val monitorId = auth.currentUser?.uid ?: throw Exception("Not logged in")

        val doc = firestore.collection("otp_links")
            .document(otp)
            .get()
            .await()

        if (!doc.exists()) throw Exception("Invalid OTP")

        val protectedId = doc.getString("protectedId")
            ?: throw Exception("Invalid OTP")

        if (protectedId == monitorId)
            throw Exception("You cannot associate yourself")

        firestore.collection("associations")
            .add(
                mapOf(
                    "monitorId" to monitorId,
                    "protectedId" to protectedId,
                    "createdAt" to Timestamp.now()
                )
            )
            .await()

        firestore.collection("otp_links").document(otp).delete().await()
    }
}
