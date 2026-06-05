package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.TodoDao
import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override fun getAllTodos(): Flow<List<TodoEntity>> = dao.getAllTodos()

    override fun getPendingTodos(): Flow<List<TodoEntity>> = dao.getPendingTodos()

    override fun getCompletedTodos(): Flow<List<TodoEntity>> = dao.getCompletedTodos()

    override suspend fun insertTodo(todo: TodoEntity): Long = dao.insertTodo(todo)

    override suspend fun updateTodo(todo: TodoEntity) = dao.updateTodo(todo)

    override suspend fun deleteTodo(todo: TodoEntity) = dao.deleteTodo(todo)

    override suspend fun deleteCompletedTodos() = dao.deleteCompletedTodos()
}
