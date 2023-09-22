package com.example.firebasechat.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.firebasechat.databinding.ArticlePopupBinding

class ArticleDetailsDialogFragment : DialogFragment() {
    private lateinit var binding: ArticlePopupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArticlePopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the article details from arguments
        val articleNumber = arguments?.getString("number")
        val articleTitle = arguments?.getString("title")
        val articleContent = arguments?.getString("content")

        // Set the article details in the popup
        binding.popupArticleNumberTextView.text = articleNumber
        binding.popupArticleTitleTextView.text = articleTitle
        binding.popupArticleContentTextView.text = articleContent
    }
}
