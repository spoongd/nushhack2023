package com.nushhack.keko

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class LessonsRecyclerAdapter(private val lessons: JSONObject) : RecyclerView.Adapter<LessonsRecyclerAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.card_lesson, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = lessons.names()?.get(position) as String
        val lesson = lessons.getJSONObject(id)
        holder.bind(id, lesson)
    }

    override fun getItemCount() = 0

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private var title: TextView = view.findViewById(R.id.lesson_title)
        fun bind(id: String, lesson: JSONObject) {
//            title =

        }
    }
}