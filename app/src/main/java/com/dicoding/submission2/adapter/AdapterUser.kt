package com.dicoding.submission2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission2.activity.UserDetailActivity
import com.dicoding.submission2.data.User
import com.dicoding.submission2.R
import com.dicoding.submission2.databinding.ItemRowUserBinding
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

var filterUserList = ArrayList<User>()
lateinit var mcontext: Context

class AdapterUser(private var listingData: ArrayList<User>) : RecyclerView.Adapter<AdapterUser.ListViewHolder>(), Filterable {
    init {
        filterUserList = listingData
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
        val pdata = filterUserList[position]
        Glide.with(holder.itemView.context)
            .load(pdata.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.avatarImg)
        holder.usernameTxt.text = pdata.username
        holder.nameTxt.text = pdata.name
        holder.companyTxt.text = pdata.company
        holder.locationTxt.text = pdata.location
        holder.itemView.setOnClickListener {
            val dataUser = User(
                pdata.username,
                pdata.name,
                pdata.avatar,
                pdata.company,
                pdata.location,
                pdata.repository,
                pdata.followers,
                pdata.following
            )
            val intentDetail = Intent(mcontext, UserDetailActivity::class.java)
            intentDetail.putExtra(UserDetailActivity.EXTRA_DATA, dataUser)
            mcontext.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: User)
    }

    override fun getItemCount(): Int = filterUserList.size

    class ListViewHolder(binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        var avatarImg: CircleImageView = binding.avatarImg
        var usernameTxt: TextView = binding.usernameTxt
        var nameTxt: TextView = binding.nameTxt
        var companyTxt: TextView = binding.companyTxt
        var locationTxt: TextView = binding.locationTxt
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                filterUserList = if (charSearch.isEmpty()) {
                    listingData
                } else {
                    val resultList = ArrayList<User>()
                    for (row in filterUserList) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                User(
                                    row.username,
                                    row.name,
                                    row.avatar,
                                    row.company,
                                    row.location,
                                    row.repository,
                                    row.followers,
                                    row.following
                                )
                            )
                        }
                    }
                    resultList
                }
                val resultsFilter = FilterResults()
                resultsFilter.values = filterUserList
                return resultsFilter
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filterUserList = results.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}