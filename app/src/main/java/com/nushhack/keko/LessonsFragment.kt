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
        binding.lessonRecyclerView.adapter = PackagesRecyclerAdapter(JSONObject("{\n" +
                "  'packageID': {\n" +
                "    'title': 'the title',\n" +
                "    'lessons': ['someID', 'someOtherID']\n" +
                "  },\n" +
                "  'package2ID': {\n" +
                "    'title': 'the title 2',\n" +
                "    'lessons': ['evenOtherID']\n" +
                "  }\n" +
                "}"), JSONObject("{\n" +
                "  'someID': {\n" +
                "    'package': 'packageID',\n" +
                "    'title': 'some title',\n" +
                "    'lecture': 'videoID',\n" +
                "    'quiz': [{'question': 'What  is 1 + 1?', 'options': ['1', '2', '3', '4'], 'answer': '2'}]\n" +
                "  },\n" +
                "  'someOtherID': {\n" +
                "    'package': 'packageID',\n" +
                "    'title': 'some other title',\n" +
                "    'lecture': 'videoID',\n" +
                "    'quiz': [{'question': 'What  is 2 + 1?', 'options': ['1', '2', '3', '4'], 'answer': '3'}]\n" +
                "  },\n" +
                "  'evenOtherID': {\n" +
                "    'package': 'package2ID',\n" +
                "    'title': 'even other title',\n" +
                "    'lecture': 'videoID',\n" +
                "    'quiz': [{'question': 'What  is 3 + 1?', 'options': ['1', '2', '3', '4'], 'answer': '4'}]\n" +
                "  }\n" +
                "}"))
        return binding.root
    }

}