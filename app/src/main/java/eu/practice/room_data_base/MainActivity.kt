package eu.practice.room_data_base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.practice.room_data_base.databinding.ActivityMainBinding
import eu.practice.room_data_base.databinding.DialogUpdateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val employeeDao = (application as EmployeeApp).db.employeeDao()

        binding?.btnRecord?.setOnClickListener {
            addRecord(employeeDao)
        }

        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect {
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, employeeDao)
            }
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etname?.text?.toString()
        val email = binding?.etemailid?.text?.toString()

        if (!name.isNullOrEmpty() && !email.isNullOrEmpty()) {
            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        employeeDao.insert(EmployeeEntity(name = name, email = email))
                    }
                    Toast.makeText(applicationContext, "Record Saved", Toast.LENGTH_LONG).show()
                    binding?.etname?.text?.clear()
                    binding?.etemailid?.text?.clear()
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error adding record", e)
                    Toast.makeText(applicationContext, "Error adding record: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(applicationContext, "Name and Email can't be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {
        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList,
                { updateId ->
                    updateRecordDialog(updateId, employeeDao)
                },
                { deleteId ->
                    deleteRecordDialog(deleteId, employeeDao)
                })

            binding?.rvItemList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemList?.adapter = itemAdapter
            binding?.rvItemList?.visibility = View.VISIBLE
            binding?.tvNoRecordAvailable?.visibility = View.GONE
        } else {
            binding?.rvItemList?.visibility = View.INVISIBLE
            binding?.tvNoRecordAvailable?.visibility = View.VISIBLE
        }
    }

    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                employeeDao.fetchEmployeesById(id).collect {
                    withContext(Dispatchers.Main) {
                        if (it != null) {
                            binding.editeName.setText(it.name)
                            binding.editEmailId.setText(it.email)
                        }
                    }
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.editeName.text.toString()
            val email = binding.editEmailId.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        employeeDao.update(EmployeeEntity(id, name, email))
                    }
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Name and Email can't be blank", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    private fun deleteRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(R.drawable.alert)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                employeeDao.fetchEmployeesById(id).collect {
                    withContext(Dispatchers.Main) {
                        if (it != null) {
                            builder.setMessage("Are you sure you want to delete ${it.name}?")
                        }
                    }
                }
            }
        }

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    employeeDao.delete(EmployeeEntity(id))
                }
                Toast.makeText(applicationContext, "Record deleted successfully.", Toast.LENGTH_LONG).show()
                dialogInterface.dismiss()
            }
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog? = builder.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
