package eu.practice.room_data_base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface EmployeeDao {

    @Insert
     fun insert(employeeEntity: EmployeeEntity)

    @Update
     fun update(employeeEntity: EmployeeEntity)

    @Delete
      fun delete(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `employee_table` ")
    fun fetchAllEmployees():Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee_table` Where id=:id ")
     fun fetchEmployeesById(id:Int):Flow<EmployeeEntity>




}
