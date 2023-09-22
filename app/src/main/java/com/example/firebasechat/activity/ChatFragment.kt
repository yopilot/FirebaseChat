package com.example.firebasechat.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.firebasechat.R
import com.example.firebasechat.databinding.FragmentChatBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ChatFragment : Fragment() {

    private val client = OkHttpClient()
    private lateinit var binding: FragmentChatBinding

    private val slideInAnimation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentChatBinding.inflate(inflater, container, false)

        // Set an OnClickListener for btnSend
        binding.btnSend.setOnClickListener {
            // Hide the "Send" button after clicking it
            binding.btnSend.visibility = View.GONE

            // Slide in the CardViews
            binding.questionCardView.visibility = View.VISIBLE
            binding.responseCardView.visibility = View.VISIBLE

            // Apply slide-in animation to CardViews
            binding.questionCardView.startAnimation(slideInAnimation)
            binding.responseCardView.startAnimation(slideInAnimation)

            // setting response TextView to "Please wait.."
            binding.txtResponse.text = "Please wait.."

            // validating text
            val question = binding.etQuestion.text.toString().trim()
            if (question.isNotEmpty()) {
                getResponse(question) { response ->
                    requireActivity().runOnUiThread {
                        binding.txtResponse.text = response
                    }
                }
            }
        }

        // Return the root view from the binding.
        return binding.root

    }

    private fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "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" // Replace with your actual API key
        val url = "https://api.openai.com/v1/completions"

        val requestBody = """
        {
            "model": "gpt-3.5-turbo-instruct",
            "prompt": "$question",
            "max_tokens": 7,
            "temperature": 0
        }
    """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback("Failed to get a response")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                    try {
                        val jsonObject = JSONObject(body)

                        if (jsonObject.has("choices")) {
                            val jsonArray = jsonObject.getJSONArray("choices")

                            if (jsonArray.length() > 0) {
                                val textResult = jsonArray.getJSONObject(0).getString("text")
                                callback(textResult)
                            } else {
                                callback("No response available")
                            }
                        } else {
                            Log.e("data", "JSON response does not contain 'choices' key:\n$body")
                            callback("Invalid response format: 'choices' key not found")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        callback("Invalid JSON format in response")
                    }
                } else {
                    Log.v("data", "empty")
                    callback("Empty response")
                }
            }
        })
    }

}
