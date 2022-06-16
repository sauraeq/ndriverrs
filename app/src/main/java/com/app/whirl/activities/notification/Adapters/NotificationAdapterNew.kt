package com.app.whirl.activities.notification.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.whirl.activities.notification.NotificationModels.Data
import com.app.whirl.databinding.NotificationRowNewBinding

import com.example.ranierilavastone.Utils.DateTimeUtil
import com.example.ranierilavastone.Utils.StringUtil

class NotificationAdapterNew (

    val context: Context,
    val list:ArrayList<Data>?

): RecyclerView.Adapter<NotificationAdapterNew.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val v= LayoutInflater.from(parent.context).inflate(R.layout.company_rows,parent,false)
        //return Holder(v)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: NotificationRowNewBinding = NotificationRowNewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return list!!.size
    }

    override  fun onBindViewHolder(holder: ViewHolder, position: Int) {



           val data=list?.get(position)

          if(!data?.title.isNullOrEmpty()){
              holder.binding.tvTitle.setText(data?.title)
          }
          if(!data?.description.isNullOrEmpty()){
              holder.binding.tvDes.setText(data?.description)
          }

        if(!data?.created_date.isNullOrEmpty()){
            holder.binding.tvDate.setText(DateTimeUtil.changedateformat(data?.created_date,"yyyy-MM-dd HH:mm:ss","dd-MM-yyyy"))
        }
    }


    class ViewHolder(binding: NotificationRowNewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: NotificationRowNewBinding

        init {
            this.binding = binding
        }
    }
}