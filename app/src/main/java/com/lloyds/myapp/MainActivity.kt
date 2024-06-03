package com.lloyds.myapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lloyds.myapp.databinding.ActivityMainBinding
import com.lloyds.myapp.utils.NetworkChangeReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var mNavHostFragment: NavHostFragment? = null
    var navController: NavController? = null
    //var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //supportActionBar!!.hide()
        mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = mNavHostFragment?.navController

       // val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
       // registerReceiver(mNetworkChangeReceiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       // unregisterReceiver(mNetworkChangeReceiver)
    }
}