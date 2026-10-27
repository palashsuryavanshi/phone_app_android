package com.phone.app.feature.recents

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.phone.app.core.platform.calllog.CallLogRepository
import com.phone.app.core.platform.calllog.CallRecord
import com.phone.app.core.platform.calllog.CallLogGrouper
import com.phone.app.core.platform.calllog.GroupedCall
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class RecentsFilter { All, Missed, Incoming, Outgoing }

data class RecentsUiState(
    val records: List<CallRecord> = emptyList(),
    val grouped: List<GroupedCall> = emptyList(),
    val filter: RecentsFilter = RecentsFilter.All,
    val query: String = "",
    val isLoading: Boolean = true,
    val permissionDenied: Boolean = false,
    val error: String? = null,
    val deleteFailed: Boolean = false
)

class RecentsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CallLogRepository(application)
    private val _state = MutableStateFlow(RecentsUiState())
    val state: StateFlow<RecentsUiState> = _state.asStateFlow()
    private var searchJob: Job? = null

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, deleteFailed = false)
            try {
                val all = repo.getAll()
                applyState(all)
            } catch (e: SecurityException) {
                _state.value = _state.value.copy(isLoading = false, permissionDenied = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun setFilter(f: RecentsFilter) {
        _state.value = _state.value.copy(filter = f)
        recompute()
    }

    fun onQueryChange(q: String) {
        _state.value = _state.value.copy(query = q)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(150)
            recompute()
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            val ok = repo.delete(id)
            if (ok) load() else _state.value = _state.value.copy(deleteFailed = true)
        }
    }

    private fun applyState(all: List<CallRecord>) {
        _state.value = _state.value.copy(records = all, isLoading = false)
        recompute()
    }

    private fun recompute() {
        val s = _state.value
        var filtered = s.records
        filtered = when (s.filter) {
            RecentsFilter.All -> filtered
            RecentsFilter.Missed -> filtered.filter { it.type == android.provider.CallLog.Calls.MISSED_TYPE }
            RecentsFilter.Incoming -> filtered.filter { it.type == android.provider.CallLog.Calls.INCOMING_TYPE }
            RecentsFilter.Outgoing -> filtered.filter { it.type == android.provider.CallLog.Calls.OUTGOING_TYPE }
        }
        if (s.query.isNotBlank()) {
            val q = s.query.lowercase()
            filtered = filtered.filter { r ->
                r.number.lowercase().contains(q) || (r.cachedName?.lowercase()?.contains(q) == true)
            }
        }
        val grouped = CallLogGrouper.group(filtered)
        _state.value = s.copy(grouped = grouped)
    }
}
