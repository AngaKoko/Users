package com.angakoko.vpdmoney.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.angakoko.vpdmoney.databinding.ListUsersBinding
import com.angakoko.vpdmoney.model.User

class ListUserAdapter(private val onClick: OnClickListener): PagingDataAdapter<User, ListUserAdapter.MyViewHolder>(UserDiffCallBack()) {

    interface OnClickListener{
        fun onUserClicked(item: User)
    }

    inner class MyViewHolder(private val binding: ListUsersBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = getUser(position)
            binding.user = item
            binding.root.setOnClickListener {
                onClick.onUserClicked(item)
            }
        }
    }

    class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    fun getUser(position: Int) = getItem(position) ?: User()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListUsersBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

}