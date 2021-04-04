package com.dicoding.submission2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission2.data.User
import com.dicoding.submission2.R
import com.dicoding.submission2.databinding.ItemRowUserBinding
import de.hdodenhof.circleimageview.CircleImageView

var filterFollowingList = ArrayList<User>()

class AdapterFollowing(listingUser: ArrayList<User>) : RecyclerView.Adapter<AdapterFollowing.ListViewHolder>() {

    init {
        filterFollowingList = listingUser
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        val sch = ListViewHolder(ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context)))
        mcontext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val pdata = filterFollowingList[position]
        Glide.with(holder.itemView.context)
            .load(pdata.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.avatarImg)
        holder.usernameTxt.text = pdata.username
        holder.nameTxt.text = pdata.name
        holder.companyTxt.text = pdata.company
        holder.locationTxt.text = pdata.location
        holder.itemView.setOnClickListener {
            //DO NOTHING
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(UserData: User)
    }

    override fun getItemCount(): Int = filterFollowingList.size

    class ListViewHolder(binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        var avatarImg: CircleImageView = binding.avatarImg
        var usernameTxt: TextView = binding.usernameTxt
        var nameTxt: TextView = binding.nameTxt
        var companyTxt: TextView = binding.companyTxt
        var locationTxt: TextView = binding.locationTxt
    }

}