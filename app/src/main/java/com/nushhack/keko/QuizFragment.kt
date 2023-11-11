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
    private lateinit var pagerCallback: PagerCommunication
    private lateinit var scoreCallback: ScoreCommunication
    private var finish: Boolean? = null
    private var question: String? = null
    private var options: ArrayList<String>? = null
    private var answer: Int? = null
    private val radioButtons: ArrayList<MaterialRadioButton> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            finish = it.getBoolean("finish")
            question = it.getString("question")
            options = it.getStringArrayList("options")
            answer = it.getInt("answer")
        }
        pagerCallback = activity as PagerCommunication
        scoreCallback = activity as ScoreCommunication
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        binding.questionStatement.text = question
        for (option in options!!.iterator()) {
            val button = MaterialRadioButton(binding.root.context)
            button.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            button.setPadding(10)
            button.text = option
            button.setOnClickListener {
                binding.resultText.text = ""
            }
            binding.answerRadios.addView(button)
            radioButtons.add(button)
        }
        if (finish == true) {
            binding.nextButton.text = getString(R.string.finish_text)
        }
        binding.submitButton.setOnClickListener {
            if (binding.answerRadios.checkedRadioButtonId == -1) {
                binding.resultText.text = getString(R.string.unselected_text)
                binding.resultText.setTextColor(resources.getColor(R.color.unselected_text_color, requireContext().theme))
            } else {
                scoreCallback.increaseScore()
                for (button in radioButtons) {
                    button.isEnabled = false
                }
                binding.submitButton.isEnabled = false
                binding.nextButton.isEnabled = true
                val chosen = binding.answerRadios.indexOfChild(
                    binding.answerRadios.findViewById(
                        binding.answerRadios.checkedRadioButtonId
                    ))
                if (chosen == answer) {
                    radioButtons[chosen].setBackgroundColor(resources.getColor(R.color.radio_correct_color, requireContext().theme))
                    binding.resultText.text = getText(R.string.correct_answer_text)
                    binding.resultText.setTextColor(resources.getColor(R.color.correct_text_color, requireContext().theme))
                } else {
                    radioButtons[chosen].setBackgroundColor(resources.getColor(R.color.radio_wrong_color, requireContext().theme))
                    radioButtons[answer!!].setBackgroundColor(resources.getColor(R.color.radio_correct_color, requireContext().theme))
                    binding.resultText.text = getText(R.string.wrong_answer_text)
                    binding.resultText.setTextColor(resources.getColor(R.color.wrong_text_color, requireContext().theme))
                }
            }
        }
        binding.nextButton.setOnClickListener {
            pagerCallback.nextPage()
        }
        binding.previousButton.setOnClickListener {
            pagerCallback.previousPage()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(finish: Boolean, question: JSONObject) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("finish", finish)
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