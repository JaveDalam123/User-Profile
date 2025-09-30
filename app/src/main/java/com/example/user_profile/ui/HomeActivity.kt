package com.example.user_profile.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.user_profile.R
import com.example.user_profile.databinding.ActivityHomeBinding
import com.example.user_profile.network.ApiRepository
import com.example.user_profile.network.Resource
import com.example.user_profile.ui.viewModel.ProfileViewModel
import kotlin.jvm.java

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = ApiRepository()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(repository) as T
            }
        })[ProfileViewModel::class.java]

        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchUser()

        binding.containerShots.setBackgroundResource(R.drawable.bg_shots_selected)
        binding.containerCollection.setBackgroundResource(R.drawable.bg_shots_unselected)

        binding.tvShots.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
        binding.tvShotsValue.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
        binding.tvCollection.setTextColor(ContextCompat.getColor(this, R.color.lightBlack))
        binding.tvCollectionValue.setTextColor(ContextCompat.getColor(this, R.color.lightBlack))

            binding.containerShots.setOnClickListener {
            binding.containerShots.setBackgroundResource(R.drawable.bg_shots_selected)
            binding.containerCollection.setBackgroundResource(R.drawable.bg_shots_unselected)

            binding.tvShots.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
            binding.tvShotsValue.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
            binding.tvCollection.setTextColor(ContextCompat.getColor(this, R.color.lightBlack))
            binding.tvCollectionValue.setTextColor(ContextCompat.getColor(this, R.color.lightBlack))
        }

            binding.containerCollection.setOnClickListener {
            binding.containerCollection.setBackgroundResource(R.drawable.bg_shots_selected)
            binding.containerShots.setBackgroundResource(R.drawable.bg_shots_unselected)

            binding.tvCollection.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
            binding.tvCollectionValue.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
            binding.tvShots.setTextColor(ContextCompat.getColor(this, R.color.lightBlack))
            binding.tvShotsValue.setTextColor(ContextCompat.getColor(this, R.color.lightBlack))
        }


        viewModel.user.observe(this) { state ->
            when (state) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val user = state.data
                    binding.location.text = "${user.user.location.city} ${user.user.location.country}"
                    binding.fullName.text = user.user.name
                    binding.tvBrunopham.text = user.user.username
                    binding.tvFollowersValue.text = user.user.statistics.followers.toString()
                    binding.tvFollowingValue.text = user.user.statistics.following.toString()
                    binding.tvShotsValue.text = user.user.statistics.activity.shots.toString()
                    binding.tvCollectionValue.text = user.user.statistics.activity.collections.toString()
                    binding.ivInstagram.setOnClickListener {
                        var url = user.user.social.profiles[0].url
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "https://$url"
                        }
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    binding.ivFacebook.setOnClickListener {
                        var url = user.user.social.profiles[1].url
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "https://$url"
                        }
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    binding.ivGlobe.setOnClickListener {
                        var url = user.user.social.profiles.toString()

                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "https://$url"
                        }

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "No app found to open link", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                    Glide.with(binding.profileImage.context)
                        .load(user.user.avatar)
                        .into(binding.profileImage)
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }

        }
    }
}
