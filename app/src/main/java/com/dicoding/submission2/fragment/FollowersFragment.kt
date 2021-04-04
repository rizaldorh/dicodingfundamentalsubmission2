package com.dicoding.submission2.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission2.data.User
import com.dicoding.submission2.R
import com.dicoding.submission2.adapter.AdapterFollowers
import com.dicoding.submission2.adapter.filterFollowersList
import com.dicoding.submission2.databinding.FollowersFragmentBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowersFragment : Fragment() {

    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

    private var listingUser: ArrayList<User> = ArrayList()
    private lateinit var adapters: AdapterFollowers

    private lateinit var binding: FollowersFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.followers_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapters = AdapterFollowers(listingUser)
        listingUser.clear()
        val dataPengguna = activity!!.intent.getParcelableExtra<User>(EXTRA_DATA) as User
        getUserFollowers(dataPengguna.username.toString())
    }

    private fun getUserFollowers(id: String) {
        binding.progresBarFollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                if(responseBody != null) {
                    binding.progresBarFollowers?.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    try {
                        val jsonArray = JSONArray(result)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username: String = jsonObject.getString("login")
                            getUserDetail(username)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progresBarFollowers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getUserDetail(id: String) {
        binding.progresBarFollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                if (responseBody != null) {
                    binding.progresBarFollowers.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    try {
                        val jsonObject = JSONObject(result)
                        val username: String = jsonObject.getString("login").toString()
                        val name: String = jsonObject.getString("name").toString()
                        val avatar: String = jsonObject.getString("avatar_url").toString()
                        val company: String = jsonObject.getString("company").toString()
                        val location: String = jsonObject.getString("location").toString()
                        val repository: String = jsonObject.getString("public_repos")
                        val followers: String = jsonObject.getString("followers")
                        val following: String = jsonObject.getString("following")
                        listingUser.add(
                            User(
                                username,
                                name,
                                avatar,
                                company,
                                location,
                                repository,
                                followers,
                                following
                            )
                        )
                        showRecyclerList()
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progresBarFollowers.visibility = View.INVISIBLE
                val errorMessages = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity, errorMessages, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showRecyclerList() {
        binding.recyclerViewFollowers.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter =
            AdapterFollowers(filterFollowersList)
        binding.recyclerViewFollowers.adapter = adapters

        listDataAdapter.setOnItemClickCallback(object :
            AdapterFollowers.OnItemClickCallback {
            override fun onItemClicked(UserData: User) {
                //DO NOTHING
            }
        })
    }
}