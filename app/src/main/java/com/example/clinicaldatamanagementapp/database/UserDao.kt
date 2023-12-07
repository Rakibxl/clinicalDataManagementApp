package com.example.clinicaldatamanagementapp.database

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)
    @Query("SELECT * FROM users")
    fun getAllUsers() : List<UserEntity>
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): UserEntity?
}