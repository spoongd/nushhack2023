package com.nushhack.keko

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class PackagesRecyclerAdapter(private val packages: JSONObject, private val lessons: JSONObject) : RecyclerView.Adapter<PackagesRecyclerAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.card_package, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = packages.names()?.get(position).toString()
        val pack = packages.getJSONObject(id)
        val title = pack.getString("title")
        val tags = pack.getJSONArray("tags")
        val difficulty = pack.getInt("difficulty")
        val lessonIds = pack.getJSONArray("lessons")
        val packLessons = JSONObject()
        for (i in 0..<lessonIds.length()) {
            val lessonId = lessonIds[i] as String
            packLessons.put(lessonId, lessons.getJSONObject(lessonId))
        }
        holder.bind(title, tags, difficulty, packLessons)
    }

    override fun getItemCount() = packages.length() // TODO

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private var titleView: TextView = view.findViewById(R.id.lesson_package_title)
        private var tagsView: TextView = view.findViewById(R.id.lesson_package_tags)
        private var difficultyView: TextView = view.findViewById(R.id.lesson_package_difficulty)
        private var recycler: RecyclerView = view.findViewById(R.id.lesson_package_recycler_view)

        fun bind(title: String, tags: JSONArray, difficulty: Int, pack: JSONObject) {
            titleView.text = title
            var tagsList = arrayListOf<String>()
            for (i in 0..<tags.length()) {
                tagsList.add(tags.getString(i))
            }
            tagsView.text = tagsList.joinToString(", ")
            difficultyView.text = view.context.getString(R.string.difficulty_format).format(difficulty)
            recycler.layoutManager = LinearLayoutManager(view.context)
            recycler.adapter = LessonsRecyclerAdapter(pack)
            recycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        }
    }
}