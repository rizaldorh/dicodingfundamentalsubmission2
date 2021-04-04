package com.dicoding.submission2.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.submission2.R
import com.dicoding.submission2.adapter.SectionsPagerAdapter
import com.dicoding.submission2.data.User
import com.dicoding.submission2.databinding.ActivityDetailUserBinding

class UserDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()
        viewPagerConfig()
    }

    private fun viewPagerConfig() {
        val sectionPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPage.adapter = sectionPagerAdapter
        binding.tabss.setupWithViewPager(binding.viewPage)

        supportActionBar?.elevation = 0f
    }


    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun setData() {
        val penggunaData = intent.getParcelableExtra<User>(EXTRA_DATA) as User
        penggunaData.name.let { setActionBarTitle(it) }
        binding.nameDetail.text = penggunaData.name
        binding.usernameDetail.text = penggunaData.username
        binding.companyDetail.text = getString(R.string.company, penggunaData.company)
        binding.locationDetail.text = getString(R.string.location, penggunaData.location)
        binding.repositoryDetail.text = getString(R.string.repository, penggunaData.repository)
        Glide.with(this)
            .load(penggunaData.avatar)
            .into(binding.avatarDetail)
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