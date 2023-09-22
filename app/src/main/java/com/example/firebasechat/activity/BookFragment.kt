package com.example.firebasechat.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.databinding.ArticlesBinding
import com.example.firebasechat.databinding.FragmentBookBinding
import com.google.android.material.snackbar.Snackbar
import data.ArticleContent
import data.ArticleNumbers
import data.ArticleTitles


data class Article(val number: String, val title: String, val content: String)

class BookFragment : Fragment() {
    private val articlesList: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter
    private lateinit var binding: FragmentBookBinding // Declare View Binding variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false) // Initialize View Binding

        // Access the list of custom titles, numbers, and content
        val customTitles = ArticleTitles.titles
        val customNumbers = ArticleNumbers.numbers
        val customContent = ArticleContent.content

        // Determine the minimum size between customTitles, customNumbers, and customContent
        val minSize = minOf(customTitles.size, customNumbers.size, customContent.size)

        // Generate sample articles with custom titles, numbers, and content
        for (i in 0 until minSize) {
            val articleNumber = customNumbers[i]
            val articleTitle = customTitles[i]
            val articleContent = customContent[i]
            val article = Article(articleNumber, articleTitle, articleContent)
            articlesList.add(article)
        }

        val recyclerView = binding.recyclerView // Use View Binding to access views
        adapter = ArticleAdapter(articlesList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
// Get the EditText inside the SearchView


// Change the hint text color

// Change the hint text color


        val searchView = binding.searchView // Use View Binding to access views
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        return binding.root
    }
}

class ArticleAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>(), Filterable {

    private var filteredArticles: List<Article> = articles

    inner class ViewHolder(private val binding: ArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Access views through binding
        val articleTitleTextView: TextView = binding.articleTitleTextView
        val articleNumberTextView: TextView = binding.articleNumberTextView
        val articleContentTextView: TextView = binding.articleContentTextView

        private var isExpanded = false

        init {
            // Set an OnClickListener to the root view (CardView or other container)
            binding.root.setOnClickListener {
                if (canExpand()) {
                    // Toggle the expanded state
                    isExpanded = !isExpanded
                    // Call a function to update the UI based on the expanded state
                    updateUI()
                } else {
                    // Display a Snackbar error message when there is nothing to expand
                    Snackbar.make(binding.root, "Nothing to expand", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        // Function to check if the item can be expanded
        private fun canExpand(): Boolean {
            // Check if the number of lines in the TextView is greater than or equal to 5
            return articleContentTextView.lineCount >= 5
        }

        // Function to update the UI based on the expanded state
        private fun updateUI() {
            if (isExpanded) {
                // Expand the item (show complete content)
                articleContentTextView.maxLines = Int.MAX_VALUE
            } else {
                // Collapse the item (show truncated content)
                articleContentTextView.maxLines = 0 // Adjust as needed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = filteredArticles[position]
        holder.articleTitleTextView.text = article.title
        holder.articleNumberTextView.text = "ARTICLE - ${article.number}"
        holder.articleContentTextView.text = article.content
    }

    override fun getItemCount(): Int {
        return filteredArticles.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Article>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(articles)
                } else {
                    val filterPattern = constraint.toString().trim()
                    for (article in articles) {
                        if (article.number.contains(filterPattern, ignoreCase = true)) {
                            filteredList.add(article)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    filteredArticles = results.values as List<Article>
                    notifyDataSetChanged()
                }
            }
        }
    }
}
