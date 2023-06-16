package com.capstoneproject.meatfyi.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.meatfyi.R
import com.capstoneproject.meatfyi.adapter.ListArticleAdapter
import com.capstoneproject.meatfyi.model.Article

class ArticleActivity : AppCompatActivity() {
    private val arraylist = ArrayList<Article>()
    private lateinit var rv_article: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Meat Articles"

        rv_article = findViewById(R.id.rv_article)
        rv_article.setHasFixedSize(true)

        arraylist.addAll(getList())
        showRVList()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    @SuppressLint("Recycle")
    private fun getList(): ArrayList<Article> {
        val judulData = resources.getStringArray(R.array.data_judul)
        val photoData = resources.obtainTypedArray(R.array.data_gambar)
        val ringkasanData = resources.getStringArray(R.array.data_ringkasan)
        val linkData = resources.getStringArray(R.array.data_link)
        val listArticle = ArrayList<Article>()
        for (i in judulData.indices) {
            val article =
                Article(photoData.getResourceId(i, -1), judulData[i], ringkasanData[i], linkData[i])
            listArticle.add(article)
        }
        return listArticle
    }

    private fun showRVList() {
        rv_article.layoutManager = LinearLayoutManager(this)
        val listArticleAdapter = ListArticleAdapter(arraylist)
        rv_article.adapter = listArticleAdapter
    }
}