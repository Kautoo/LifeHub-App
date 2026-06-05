package com.fizi.lifehub.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getAllTodos: GetAllTodosUseCase,
    private val insertTodo: InsertTodoUseCase,
    private val updateTodo: UpdateTodoUseCase,
    private val deleteTodo: DeleteTodoUseCase,
    private val toggleTodo: ToggleTodoUseCase,
    private val deleteCompleted: DeleteCompletedTodosUseCase
) : ViewModel() {

    val todos = getAllTodos().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTodo(title: String, description: String = "", priority: Int = 1) {
        viewModelScope.launch {
            insertTodo(
                TodoEntity(
                    title = title,
                    description = description,
                    priority = priority
                )
            )
        }
    }

    fun toggleDone(todo: TodoEntity) {
        viewModelScope.launch { toggleTodo(todo) }
    }

    fun delete(todo: TodoEntity) {
        viewModelScope.launch { deleteTodo(todo) }
    }

    fun clearCompleted() {
        viewModelScope.launch { deleteCompleted() }
    }
}
