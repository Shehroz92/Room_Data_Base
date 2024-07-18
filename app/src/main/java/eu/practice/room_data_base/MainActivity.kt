package eu.practice.room_data_base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
            addRecord(employeeDao)
        }

        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list,employeeDao)
            }
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
                Toast.makeText(applicationContext,"Name and Email can't be Blank",Toast.LENGTH_LONG).show()
            }
        }


    private fun setupListOfDataIntoRecyclerView(
        employeesList:ArrayList<EmployeeEntity> ,
        employeeDao: EmployeeDao){
        if (employeesList.isNotEmpty()){
            val itemAdapter = ItemAdapter(employeesList,


                )
            binding?.rvItemList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemList?.adapter= itemAdapter
            binding?.rvItemList?.visibility= View.VISIBLE
            binding?.tvNoRecordAvailable?.visibility = View.GONE

        }else{
            binding?.rvItemList?.visibility= View.INVISIBLE
            binding?.tvNoRecordAvailable?.visibility = View.VISIBLE
        }
    }

    private fun updateRecordDialog(id:Int,employeeDao: EmployeeDao){}
    private fun deleteRecordDialog(id:Int,employeeDao: EmployeeDao){}


}
