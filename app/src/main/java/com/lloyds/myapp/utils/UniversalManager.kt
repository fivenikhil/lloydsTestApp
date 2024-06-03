package com.lloyds.myapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

object UniversalManager {

    private var connectivityRepository: ConnectivityRepository? = null

    fun getSessionManagerContext(context: Context): SessionManager {
        return SessionManager(context)
    }

    fun moveToOtherFragment(clazz: Fragment, fragment: Int) {
        NavHostFragment.findNavController(clazz).navigate(fragment)
    }

    //onBackPress
    fun moveToOtherFragmentBack(clazz: Fragment, fragment: Int) {
        NavHostFragment.findNavController(clazz).navigate(
            fragment, null, NavOptions.Builder()
                .setPopUpTo(fragment, true).build()
        )
    }

    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    fun showClickableLink(textView: TextView, text: String, url: String) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                textView.context.startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val spannableString = SpannableString(text)
        spannableString.setSpan(
            clickableSpan,
            0,
            text.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun getInstance(context: Context): ConnectivityRepository {
        if (connectivityRepository == null) {
            connectivityRepository = ConnectivityRepository(context.applicationContext)
        }
        return connectivityRepository!!
    }
}