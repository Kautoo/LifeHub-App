package com.fizi.lifehub.ui.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.PomodoroSessionEntity
import com.fizi.lifehub.domain.repository.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

enum class TimerState { IDLE, RUNNING, PAUSED, BREAK }

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository
) : ViewModel() {

    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val sessionsToday: StateFlow<Int> = repository.getCountForDate(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMinutesToday: StateFlow<Int> = repository.getTotalMinutesForDate(today)
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState

    private val _timeRemaining = MutableStateFlow(25 * 60)
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private val _totalTime = MutableStateFlow(25 * 60)
    val totalTime: StateFlow<Int> = _totalTime

    private val _workMinutes = MutableStateFlow(25)
    private val _breakMinutes = MutableStateFlow(5)

    private var currentSessionId: Long = 0
    private var sessionStartTime: Long = 0

    val progress: StateFlow<Float> = combine(_timeRemaining, _totalTime) { remaining, total ->
        if (total > 0) 1f - (remaining.toFloat() / total) else 0f
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    fun startTimer() {
        if (_timerState.value == TimerState.IDLE || _timerState.value == TimerState.PAUSED) {
            _timerState.value = TimerState.RUNNING
            if (currentSessionId == 0L) {
                sessionStartTime = System.currentTimeMillis()
                viewModelScope.launch {
                    currentSessionId = repository.insert(
                        PomodoroSessionEntity(
                            startTime = sessionStartTime,
                            durationMinutes = _workMinutes.value,
                            breakMinutes = _breakMinutes.value
                        )
                    )
                }
            }
            startCountdown()
        }
    }

    fun pauseTimer() {
        _timerState.value = TimerState.PAUSED
    }

    fun resetTimer() {
        _timerState.value = TimerState.IDLE
        _timeRemaining.value = _workMinutes.value * 60
        _totalTime.value = _workMinutes.value * 60
        currentSessionId = 0
    }

    fun setWorkMinutes(minutes: Int) {
        _workMinutes.value = minutes
        if (_timerState.value == TimerState.IDLE) {
            _timeRemaining.value = minutes * 60
            _totalTime.value = minutes * 60
        }
    }

    fun setBreakMinutes(minutes: Int) {
        _breakMinutes.value = minutes
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_timerState.value == TimerState.RUNNING && _timeRemaining.value > 0) {
                delay(1000)
                if (_timerState.value == TimerState.RUNNING) {
                    _timeRemaining.value -= 1
                }
            }
            if (_timeRemaining.value <= 0 && _timerState.value == TimerState.RUNNING) {
                completeSession()
            }
        }
    }

    private fun completeSession() {
        viewModelScope.launch {
            if (currentSessionId > 0) {
                repository.update(
                    PomodoroSessionEntity(
                        id = currentSessionId,
                        startTime = sessionStartTime,
                        endTime = System.currentTimeMillis(),
                        durationMinutes = _workMinutes.value,
                        breakMinutes = _breakMinutes.value,
                        completed = true
                    )
                )
            }
            _timerState.value = TimerState.BREAK
            _timeRemaining.value = _breakMinutes.value * 60
            _totalTime.value = _breakMinutes.value * 60
            currentSessionId = 0
            startBreakCountdown()
        }
    }

    private fun startBreakCountdown() {
        viewModelScope.launch {
            while (_timerState.value == TimerState.BREAK && _timeRemaining.value > 0) {
                delay(1000)
                if (_timerState.value == TimerState.BREAK) {
                    _timeRemaining.value -= 1
                }
            }
            if (_timeRemaining.value <= 0 && _timerState.value == TimerState.BREAK) {
                _timerState.value = TimerState.IDLE
                _timeRemaining.value = _workMinutes.value * 60
                _totalTime.value = _workMinutes.value * 60
            }
        }
    }
}
