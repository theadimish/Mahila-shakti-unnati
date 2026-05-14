
package com.example.mahilashaktiunnati.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val uniqueId: String,
    val phone: String,
    val photoUri: String = ""
)