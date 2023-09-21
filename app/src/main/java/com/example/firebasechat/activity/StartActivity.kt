package com.example.firebasechat.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.firebasechat.R
import nl.joery.animatedbottombar.AnimatedBottomBar
import android.util.Log // Import Log class

class StartActivity : AppCompatActivity() {
    private lateinit var bottombar: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Suppress logs from the nl.joery.animatedbottombar package
        Log.d("FirebaseChat", "Suppressing logs from nl.joery.animatedbottombar")

        val fragmentsContainer = findViewById<View>(R.id.fragments_container)
        bottombar = findViewById(R.id.bottom_bar)

        // Replace the initial fragment when the activity is created
        replaceFragment(ChatFragment())

        bottombar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newIndex) {
                    1 -> replaceFragment(ChatFragment())
                    0 -> replaceFragment(ContactFragment())
                    2 -> replaceFragment(BookFragment())
                    3 -> replaceFragment(ProfileFragment())
                    else -> {
                        // Handle other cases
                    }
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, fragment)
            .addToBackStack(null) // Add this line to handle back navigation correctly
            .commit()
    }
}
