package com.fizi.lifehub.domain.usecase

import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(): Flow<List<TodoEntity>> = repository.getAllTodos()
}

class InsertTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: TodoEntity): Long = repository.insertTodo(todo)
}

class UpdateTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: TodoEntity) = repository.updateTodo(todo)
}

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: TodoEntity) = repository.deleteTodo(todo)
}

class ToggleTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: TodoEntity) {
        repository.updateTodo(todo.copy(isDone = !todo.isDone))
    }
}

class DeleteCompletedTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke() = repository.deleteCompletedTodos()
}
