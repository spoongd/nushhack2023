package com.nushhack.keko

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import com.google.android.material.radiobutton.MaterialRadioButton
import com.nushhack.keko.databinding.FragmentQuizBinding
import org.json.JSONObject

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var question: String? = null
    private var options: ArrayList<String>? = null
    private var answer: Int? = null
    private val radioButtons: ArrayList<MaterialRadioButton> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getString("question")
            options = it.getStringArrayList("options")
            answer = it.getInt("answer")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        binding.questionStatement.text = question
        for (option in options!!.iterator()) {
            val button = MaterialRadioButton(binding.root.context)
            button.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            button.setPadding(10)
            button.text = option
            binding.answerRadios.addView(button)
            radioButtons.add(button)
        }
        binding.submitButton.setOnClickListener {
            if (!binding.answerRadios.isSelected) {
                // TODO
            } else if (binding.answerRadios.indexOfChild(
                    binding.answerRadios.findViewById(
                        binding.answerRadios.checkedRadioButtonId
                    )) == answer) {
                // TODO
            } else {
                for (i in 0..<radioButtons.size) {
                    // TODO
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(question: JSONObject) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString("question", question.getString("question"))
                    val optionsIntent = arrayListOf<String>()
                    val options = question.getJSONArray("options")
                    for (i in 0..<options.length()) {
                        val option = options.getString(i)
                        optionsIntent.add(option)
                    }
                    putStringArrayList("options", optionsIntent)
                    putInt("answer", question.getInt("answer"))
                }
            }
    }

}