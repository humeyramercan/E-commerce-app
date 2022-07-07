package com.humeyramercan.e_commerceapp.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.databinding.ActivityMainBinding
import com.humeyramercan.e_commerceapp.workmanager.SendNotificationWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesBasket: SharedPreferences
    private var auth=Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //To check if there are any products in the basket.
        sharedPreferencesBasket = this.getSharedPreferences(
            "com.humeyramercan.e_commerceapp",
            Context.MODE_PRIVATE
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHost
        navController = navHostFragment.navController

        //Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)

        //View of the bottom navigation by destination
        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment || destination.id == R.id.forgotPasswordFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }

            if(destination.id==R.id.productDetailFragment || destination.id==R.id.productsFragment){
                binding.toolbar.visibility=View.VISIBLE
            }else{
                binding.toolbar.visibility=View.GONE
            }
        }

        //Bottom navigation view
        binding.bottomNavigation.setupWithNavController(navController)

    }

    //Up button control
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        WorkManager.getInstance(this@MainActivity).cancelAllWork()
    }

    override fun onStop() {
        super.onStop()
        if(auth.currentUser!=null) {
            val isProductInBag = sharedPreferencesBasket.getBoolean("isProductInBag", false)
            if (isProductInBag) {
                startWorkManager()
            }
        }else{
            WorkManager.getInstance(this@MainActivity).cancelAllWork()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        WorkManager.getInstance(this@MainActivity).cancelAllWork()
    }

    private fun startWorkManager(){
        val constraints= Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<SendNotificationWorker>(10,
            TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this@MainActivity).enqueue(myWorkRequest)
    }

}