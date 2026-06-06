package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.GoalEntity
import com.fizi.lifehub.data.local.entity.GoalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAll(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE status = 'IN_PROGRESS' ORDER BY targetDate ASC")
    fun getActive(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE status = 'COMPLETED' ORDER BY createdAt DESC")
    fun getCompleted(): Flow<List<GoalEntity>>

    @Query("SELECT COUNT(*) FROM goals WHERE status = :status")
    fun getCountByStatus(status: GoalStatus): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity): Long

    @Update
    suspend fun update(goal: GoalEntity)

    @Delete
    suspend fun delete(goal: GoalEntity)
}
