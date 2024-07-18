package eu.practice.room_data_base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.practice.room_data_base.databinding.ItemsRowBinding

class ItemAdapter ( val items : ArrayList<EmployeeEntity>,
                //    private var updateListener:(id:Int) -> Unit,
                //    private var deleteListener:(id:Int) -> Unit

):RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class  ViewHolder(binding: ItemsRowBinding):RecyclerView.ViewHolder(binding.root){

        val llMAin = binding.llMain
        val tvName = binding.tvName2
        val tvEmail = binding.tvEdit
        val ivDelete = binding.ivDelete
        val ivEdite = binding.ivEdit

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent , false ))
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        if (position % 2 == 0 ){
            holder.llMAin.setBackgroundColor(ContextCompat.getColor( holder.itemView.context,
            R.color.LightGrey
            ))

        }else{
            holder.llMAin.setBackgroundColor(ContextCompat.getColor( holder.itemView.context,
                R.color.white
            ))
        }

        holder.ivEdite.setOnClickListener {
          //  updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener {
         //   deleteListener.invoke(item.id)
        }



    }


}