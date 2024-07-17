package eu.practice.room_data_base

import android.app.Application


class EmployeeApp:Application() {
    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }

}