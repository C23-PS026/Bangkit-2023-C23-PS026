package com.capstoneproject.meatfyi.adapter

import android.content.Intent
import android.net.Uri
import com.capstoneproject.meatfyi.R
import com.capstoneproject.meatfyi.model.Article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ListArticleAdapter(private val listArticle: ArrayList<Article>) :
    RecyclerView.Adapter<ListArticleAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gambar = itemView.findViewById<ImageView>(R.id.item_gambar)
        val judul = itemView.findViewById<TextView>(R.id.tv_item_judul)
        val ringkasan = itemView.findViewById<TextView>(R.id.tv_item_ringkasan)
        val button = itemView.findViewById<MaterialButton>(R.id.view_more_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (gbr, jdl, rks, link) = listArticle[position]
        holder.gambar.setImageResource(gbr)
        holder.judul.text = jdl
        holder.ringkasan.text = rks

        holder.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            holder.button.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return listArticle.size
    }
}