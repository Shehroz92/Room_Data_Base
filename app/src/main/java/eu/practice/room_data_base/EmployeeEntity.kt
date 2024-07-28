package eu.practice.room_data_base

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_table")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0  ,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "email_id")
    val email: String = ""
)
