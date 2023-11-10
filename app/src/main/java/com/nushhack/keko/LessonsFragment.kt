package com.nushhack.keko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nushhack.keko.databinding.FragmentLessonsBinding
import org.json.JSONObject

class LessonsFragment : Fragment() {
    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        binding.lessonRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.lessonRecyclerView.adapter = PackagesRecyclerAdapter(JSONObject(), JSONObject())
        return binding.root
    }

}