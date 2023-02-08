package com.www.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class NewsActivity : AppCompatActivity(),NewsItemClicked{
    private lateinit var mAdapter: StockAdapter
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        fetchData()
        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = StockAdapter(this)
        recyclerView.adapter = mAdapter

    }

    private fun fetchData(){
        // Enter your API key here
        val API_KEY = "demo"

        val url = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=AAPL&apikey=$API_KEY"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val newsJsonArray = response.getJSONArray("feed")

                val newsArray = ArrayList<NewsData>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = NewsData(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("banner_image"),
                        newsJsonObject.getString("summary"),
                        newsJsonObject.getString("source")
                    )
                    newsArray.add(news)
                    mAdapter.updateNews(newsArray)
                    progressBar.visibility = View.GONE
                }
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: NewsData) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(browserIntent)

    }
}