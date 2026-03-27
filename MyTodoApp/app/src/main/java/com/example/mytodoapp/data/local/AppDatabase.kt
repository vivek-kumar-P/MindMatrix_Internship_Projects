package com.example.mytodoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytodoapp.data.local.entity.SubtaskEntity
import com.example.mytodoapp.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, SubtaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}