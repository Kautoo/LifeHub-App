package com.fizi.lifehub.ui.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.WaterLogEntity
import com.fizi.lifehub.domain.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val today = dateFormat.format(Date())

    // Get or create today's log
    val todayLog: StateFlow<WaterLogEntity> = repository.getByDate(today)
        .map { it ?: WaterLogEntity(date = today, glasses = 0, target = 8) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WaterLogEntity(date = today, glasses = 0, target = 8))

    // Last 7 days
    private val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
    val weekLogs: StateFlow<List<WaterLogEntity>> = repository.getFromDate(dateFormat.format(weekAgo.time))
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGlass() {
        viewModelScope.launch {
            val current = todayLog.value
            repository.insert(current.copy(glasses = current.glasses + 1))
        }
    }

    fun removeGlass() {
        viewModelScope.launch {
            val current = todayLog.value
            if (current.glasses > 0) {
                repository.insert(current.copy(glasses = current.glasses - 1))
            }
        }
    }
}
