package com.nushhack.keko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nushhack.keko.databinding.FragmentCompletionBinding

class CompletionFragment : Fragment() {
    private var _binding: FragmentCompletionBinding? = null
    private val binding get() = _binding!!
    private var title: String? = null
    private var score: Int = 0
    private var maxScore: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            title = it.getString("title")
            maxScore = it.getInt("maxScore")
        }
        super.onCreate(savedInstanceState)
    }

    fun increaseScore() {
        score++
        updateScore()
    }

    fun updateScore() {
        _binding?.completionText?.text = getString(R.string.completion_message).format(title, score, maxScore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompletionBinding.inflate(inflater, container, false)
        updateScore()
        binding.exitButton.setOnClickListener {
            activity?.finish()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, maxScore: Int) =
            CompletionFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putInt("maxScore", maxScore)
                }
            }
    }
}