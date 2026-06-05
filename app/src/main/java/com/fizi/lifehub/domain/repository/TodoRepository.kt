package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodos(): Flow<List<TodoEntity>>
    fun getPendingTodos(): Flow<List<TodoEntity>>
    fun getCompletedTodos(): Flow<List<TodoEntity>>
    suspend fun insertTodo(todo: TodoEntity): Long
    suspend fun updateTodo(todo: TodoEntity)
    suspend fun deleteTodo(todo: TodoEntity)
    suspend fun deleteCompletedTodos()
}
