package pt.isec.amov.safetysec.data.repository;

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pt.isec.amov.safetysec.data.model.Rule

class RuleRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createRule(rule: Rule) {
        firestore.collection("rules")
            .add(rule)
            .await()
    }

    suspend fun getRulesForProtected(protectedId: String): List<Rule> {
        val snapshot = firestore.collection("rules")
            .whereEqualTo("protectedId", protectedId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(Rule::class.java)?.copy(id = it.id)
        }
    }

    suspend fun getRulesForMonitor(monitorId: String): List<Rule> {
        val snapshot = firestore.collection("rules")
            .whereEqualTo("monitorId", monitorId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(Rule::class.java)?.copy(id = it.id)
        }
    }

    suspend fun setAuthorization(ruleId: String, authorized: Boolean) {
        firestore.collection("rules")
            .document(ruleId)
            .update("authorized", authorized)
            .await()
    }

    suspend fun setActive(ruleId: String, active: Boolean) {
        firestore.collection("rules")
            .document(ruleId)
            .update("active", active)
            .await()
    }
}

