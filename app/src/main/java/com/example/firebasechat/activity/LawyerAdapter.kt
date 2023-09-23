package com.example.firebasechat.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.databinding.CardviewBinding  // Replace with your actual package name and layout
import java.util.*

class LawyerAdapter(private val lawyerList: List<LawyerDetails>) :
    RecyclerView.Adapter<LawyerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lawyer = lawyerList[position]

        // Bind lawyer data to the ViewHolder
        holder.bind(lawyer)

        // Set a click listener for opening Google Maps
        holder.itemView.setOnClickListener {
            val coordinates = lawyer.coordinates
            openGoogleMaps(holder.itemView.context, coordinates)
        }
    }

    override fun getItemCount(): Int {
        return lawyerList.size
    }

    inner class ViewHolder(private val binding: CardviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lawyer: LawyerDetails) {
            binding.lawyername.text = lawyer.name
            binding.lawyerfirmname.text = lawyer.firmName
            binding.lawyerConatctinfo.text = lawyer.contact
            binding.lawyerpracticearea.text = lawyer.practiceArea
        }
    }

    private fun openGoogleMaps(context: Context, coordinates: String) {
        // Split the coordinates into latitude and longitude
        val parts = coordinates.split(", ")
        if (parts.size == 2) {
            val latitude = parts[0]
            val longitude = parts[1]
            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps") // Use the Google Maps app
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                // Handle the case where Google Maps is not installed on the device
                // You can open a web URL or show a message to the user.
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://maps.google.com/?q=$latitude,$longitude")
                )
                if (webIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(webIntent)
                } else {
                    // You can show a message to the user indicating that they need to install Google Maps or use a web browser.
                    // Example: Toast.makeText(context, "Google Maps is not installed.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle invalid coordinates format
            // You can show an error message to the user or take appropriate action.
        }
    }
}
