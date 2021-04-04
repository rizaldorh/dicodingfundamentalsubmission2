package com.dicoding.submission2.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission2.R
import com.dicoding.submission2.adapter.AdapterUser
import com.dicoding.submission2.adapter.filterUserList
import com.dicoding.submission2.data.User
import com.dicoding.submission2.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var listingData: ArrayList<User> = ArrayList()
    private lateinit var adapter: AdapterUser
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github User (UI/UX dan API)"

        adapter = AdapterUser(listingData)

        recyclerViewConfig()
        searchUser()
        getUser()
    }

    private fun searchUser() { binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()) {
                return true
            } else {
                listingData.clear()
                getUserSearch(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
    }

    private fun getUser() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {

                if (responseBody != null) {
                    binding.progressBar.visibility = View.INVISIBLE
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
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
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
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getUserSearch(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {

                if (responseBody != null) {

                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
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
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getUserDetail(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                if (responseBody != null) {
                    binding.progressBar.visibility = View.INVISIBLE
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
                        listingData.add(
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
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
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
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }


    private fun recyclerViewConfig() {
        binding.recycleView.layoutManager = LinearLayoutManager(binding.recycleView.context)
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.addItemDecoration(
            DividerItemDecoration(
                binding.recycleView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun showRecyclerList() {
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = AdapterUser(filterUserList)
        binding.recycleView.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : AdapterUser.OnItemClickCallback {
            override fun onItemClicked(dataUsers: User) {
                showSelectedUser(dataUsers)
            }
        })
    }

    private fun showSelectedUser(dataUser: User) {
        User(
            dataUser.username,
            dataUser.name,
            dataUser.avatar,
            dataUser.company,
            dataUser.location,
            dataUser.repository,
            dataUser.followers,
            dataUser.following
        )
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_DATA, dataUser)

        this@MainActivity.startActivity(intent)
        Toast.makeText(
            this,
            "${dataUser.name}",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_act_setting) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}
