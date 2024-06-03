package com.lloyds.myapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.lloyds.myapp.R

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.let {
            if (!isInternetAvailable(it)) {
                val builder = AlertDialog.Builder(context)
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.no_internet_connection, null)
                builder.setView(view)
                val btnRetry = view.findViewById<AppCompatButton>(R.id.btn_retry)

                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setGravity(Gravity.CENTER)

                btnRetry.setOnClickListener {
                    dialog.dismiss()
                    onReceive(context, intent)
                }
            }
        }
    }


    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}