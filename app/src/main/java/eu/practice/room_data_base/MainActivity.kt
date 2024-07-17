package eu.practice.room_data_base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import eu.practice.room_data_base.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao =(application as EmployeeApp).db.employeeDao()

        binding?.btnRecord?.setOnClickListener {
            // TODO addRecord with employeeDao
            addRecord(employeeDao)
        }


    }

        fun addRecord(employeeDao: EmployeeDao){
            val name = binding?.etname?.text?.toString()
            val email = binding?.etemailid?.text?.toString()

            if (email!!.isNotEmpty() && name!!.isNotEmpty()){
                lifecycleScope.launch {
                    employeeDao.insert(EmployeeEntity(name= name,email= email))
                    Toast.makeText(applicationContext,"Record Saved",Toast.LENGTH_LONG).show()
                    binding?.etname?.text?.clear()
                    binding?.etemailid?.text?.clear()
                }
            }else{
                Toast.makeText(applicationContext,"Name and Email cannot be Blank",Toast.LENGTH_LONG).show()
            }

        }
}
