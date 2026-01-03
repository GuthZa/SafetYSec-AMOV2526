package pt.isec.amov.safetysec.dashboard.protected.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.model.Rule
import pt.isec.amov.safetysec.data.repository.RuleRepository

class ProtectedRulesViewModel : ViewModel() {

    private val repository = RuleRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _rules = MutableStateFlow<List<Rule>>(emptyList())
    val rules: StateFlow<List<Rule>> = _rules

    fun loadRules() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            _rules.value = repository.getRulesForProtected(uid)
        }
    }

    fun toggleAuthorization(rule: Rule) {
        viewModelScope.launch {
            repository.setAuthorization(rule.id, !rule.authorized)
            loadRules()
        }
    }
}