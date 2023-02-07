package com.example.mymusiclibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class AlbumAdapter(private var exercises: MutableList<Album>) :
    RecyclerView.Adapter<AlbumAdapter.ListViewHolder>() {

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumTitle: TextView = itemView.findViewById(R.id.albumTitleTextView)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val album = exercises[position]
        holder.albumTitle.text = album.title
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(position)
            holder.itemView.findNavController().navigate(action)
        }
    }

}