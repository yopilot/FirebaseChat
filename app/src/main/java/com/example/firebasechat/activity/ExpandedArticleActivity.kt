package com.example.firebasechat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechat.databinding.ActivityExpandedArticleBinding


class ExpandedArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpandedArticleBinding // Declare View Binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandedArticleBinding.inflate(layoutInflater) // Initialize View Binding

        // Retrieve the data from the intent
        val articleTitle = intent.getStringExtra("articleTitle")
        val articleContent = intent.getStringExtra("articleContent")

        // Display the data using View Binding
        binding.expandedArticleTitleTextView.text = articleTitle
        binding.expandedArticleContentTextView.text = articleContent

        // Set the root view for the activity
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Add any necessary logic to handle back navigation
        // For example, if you want to go back to the main activity:
        // finish()
    }
}
