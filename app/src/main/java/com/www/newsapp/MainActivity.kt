package com.www.newsapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock.sleep
import android.widget.Toast

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Toast.makeText(this, "Please wait while we fetch the latest news", Toast.LENGTH_LONG).show()

        while(!goToNextPage()){
            // do nothing
            sleep(2000)
        }
    }
    private fun goToNextPage():Boolean{
        // If Internet is available then wait for 2s and then go to the next page
        if (checkForInternet(this)){
            val intent = Intent(this, NewsActivity::class.java)
            try{
                sleep(2000)
                startActivity(intent)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
            return true
        }
        Toast.makeText(this, "Please enable internet connection to get the latest news", Toast.LENGTH_LONG).show()
        return false
    }
    private fun checkForInternet(context:Context):Boolean{
        // Register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M  or greater we need to use the  NetworkCapabilities to check what type of  network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Returns a Network object corresponding to the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,  or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        }
        else {
            // if the android version is below M
                val networkInfo = connectivityManager.activeNetworkInfo ?: return false
                return networkInfo.isConnected
        }
    }
}