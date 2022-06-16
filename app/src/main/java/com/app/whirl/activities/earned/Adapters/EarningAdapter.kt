package com.app.whirl.activities.earned.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.whirl.activities.earned.Models.Data

import com.app.whirl.databinding.EarnedRowBinding

import com.example.ranierilavastone.Utils.DateTimeUtil

class EarningAdapter(

    val context: Context,
    val list:ArrayList<Data>?

): RecyclerView.Adapter<EarningAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val v= LayoutInflater.from(parent.context).inflate(R.layout.company_rows,parent,false)
        //return Holder(v)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: EarnedRowBinding = EarnedRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return list!!.size
    }

    override  fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val data=list?.get(position)

        if(!data?.amount.isNullOrEmpty()){
            holder.binding.tvPrice.setText("$"+data?.amount)
        }
        if(!data?.pickup_address.isNullOrEmpty()){
            holder.binding.tvSource.setText(data?.pickup_address)
        }
        if(!data?.drop_address.isNullOrEmpty()){
            holder.binding.tvDestination.setText(data?.drop_address)
        }
        if(!data?.created_date.isNullOrEmpty()){
            holder.binding.tvDate.setText(DateTimeUtil.changedateformat(data?.created_date,"yyyy-MM-dd HH:mm:ss","dd-MM-yyyy"))
        }
    }


    class ViewHolder(binding: EarnedRowBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: EarnedRowBinding

        init {
            this.binding = binding
        }
    }
}