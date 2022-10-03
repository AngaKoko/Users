package com.angakoko.vpdmoney.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.angakoko.vpdmoney.databinding.ListUsersBinding
import com.angakoko.vpdmoney.model.User

class ListUserAdapter(private val onClick: OnClickListener): RecyclerView.Adapter<ListUserAdapter.MyViewHolder>(){

    private var listUser = mutableListOf<User>()

    interface OnClickListener{
        fun onUserClicked(item: User)
    }

    fun swapList(newList: List<User>){
        //Check difference between old and new list then get result
        val diffCallBack = UserDiffCallBack(listUser, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list
        listUser = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)
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

    private class UserDiffCallBack(private val oldList: List<User>,
                                   private val newList: List<User>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList == newList
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

    fun getUser(position: Int) = listUser[position]

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListUsersBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

}