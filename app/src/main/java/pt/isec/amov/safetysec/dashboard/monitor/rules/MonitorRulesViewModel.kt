package pt.isec.amov.safetysec.dashboard.monitor.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.model.Rule
import pt.isec.amov.safetysec.data.model.RuleType
import pt.isec.amov.safetysec.data.repository.RuleRepository

class MonitorRulesViewModel : ViewModel() {

    private val repository = RuleRepository()

    private val _rulesByProtected = MutableStateFlow<Map<String, List<Rule>>>(emptyMap())
    val rulesByProtected: StateFlow<Map<String, List<Rule>>> = _rulesByProtected

    fun loadRulesForProtegido(protectedId: String) {
        viewModelScope.launch {
            val rules = repository.getRulesForMonitor(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .filter { it.protectedId == protectedId }
            _rulesByProtected.value += (protectedId to rules)
        }
    }

    fun createRuleForProtegido(protectedId: String, type: RuleType, parameters: Map<String, Any> = emptyMap()) {
        val monitorId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val rule = Rule(
            type = type,
            monitorId = monitorId,
            protectedId = protectedId,
            parameters = parameters,
            authorized = false
        )
        viewModelScope.launch {
            repository.createRule(rule)
            loadRulesForProtegido(protectedId)
        }
    }
}
