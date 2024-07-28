package eu.practice.room_data_base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.practice.room_data_base.databinding.ItemsRowBinding

class ItemAdapter(
    private val items: ArrayList<EmployeeEntity>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvName = binding.tvName2
        val tvEmail = binding.tvEdit
        val ivDelete = binding.ivDelete
        val ivEdit = binding.ivEdit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        holder.llMain.setBackgroundColor(
            if (position % 2 == 0) ContextCompat.getColor(holder.itemView.context, R.color.LightGrey)
            else ContextCompat.getColor(holder.itemView.context, R.color.white)
        )

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }
}
