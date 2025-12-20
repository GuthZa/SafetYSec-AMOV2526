package pt.isec.amov.safetysec.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val name: String = "",
    val roles: List<String> = emptyList(),
    val authorizedMonitors: List<String> = emptyList(),
    val activeRules: List<String> = emptyList(),
    val alertCancelCode: String = "0000"
)

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun createUserProfile(profile: UserProfile) {
        val uid = auth.currentUser?.uid ?: throw Exception("No logged-in user")
        db.collection("users").document(uid).set(profile).await()
    }

    suspend fun getCurrentUserProfile(): UserProfile? {
        val uid = auth.currentUser?.uid ?: return null
        val doc = db.collection("users").document(uid).get().await()
        return doc.toObject(UserProfile::class.java)
    }
}
