package com.nushhack.keko

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nushhack.keko.databinding.FragmentLessonsBinding
import org.json.JSONObject

class LessonsFragment : Fragment() {
    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        binding.lessonRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.welcomeTextView.text = "Hello ${requireActivity().getPreferences(Context.MODE_PRIVATE).getString("Name", "New User")}!"

        val queue = Volley.newRequestQueue(context)
        var requests = 2
        var packages = JSONObject()
        var lessons = JSONObject()
        queue.add(StringRequest(Request.Method.GET, "http://34.125.233.174:5000/get-packages",
            {response ->
                packages = JSONObject(response)
                requests--
                if (requests == 0)  {
                    bindAdapter(packages, lessons)
                }
            }, {}))
        queue.add(StringRequest(Request.Method.GET, "http://34.125.233.174:5000/get-lessons",
            {response ->
                lessons = JSONObject(response)
                requests--
                if (requests == 0) {
                    bindAdapter(packages, lessons)
                }
            }, {}))
        return binding.root
    }

    fun bindAdapter(packages: JSONObject, lessons: JSONObject) {
        binding.loading.visibility = View.GONE
        binding.lessonRecyclerView.adapter = PackagesRecyclerAdapter(packages, lessons)
    }

}