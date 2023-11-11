package com.nushhack.keko

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.Rational
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.nushhack.keko.databinding.FragmentLectureBinding
import org.json.JSONObject

class LectureFragment : Fragment() {
    private var _binding: FragmentLectureBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaController: MediaController
    private lateinit var pagerCallback: PagerCommunication
    private var difficulty: Int? = null
    private var tags: ArrayList<String>? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            difficulty = it.getInt("difficulty")
            tags = it.getStringArrayList("tags")
            url = it.getString("url")
        }
        pagerCallback = activity as PagerCommunication
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLectureBinding.inflate(inflater, container, false)
        binding.tags.text = tags?.joinToString(", ")
        binding.difficulty.text = getString(R.string.difficulty_format).format(difficulty)
        binding.nextButton.setOnClickListener {
            pagerCallback.nextPage()
        }

        binding.video.setVideoPath("http://34.125.233.174:5000/$url")
        mediaController = MediaController(context)
        mediaController.setAnchorView(binding.video)
        binding.video.setMediaController(mediaController)
        binding.video.addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (left != oldLeft || right != oldRight || top != oldTop || bottom != oldBottom) {
                val sourceRectHint = Rect()
                binding.video.getGlobalVisibleRect(sourceRectHint)
                requireActivity().setPictureInPictureParams(
                    PictureInPictureParams.Builder().setSourceRectHint(sourceRectHint).build()
                )
            }
        }

        binding.pipButton.setOnClickListener {
            val rational = Rational(binding.video.width, binding.video.height)
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(rational).setSourceRectHint(Rect()).build()
            binding.tags.visibility = View.GONE
            binding.difficulty.visibility = View.GONE
            binding.nextButton.visibility = View.GONE
            binding.pipButton.visibility = View.GONE
            binding.video.setMediaController(null)
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
            requireActivity().enterPictureInPictureMode(params)
        }

        return binding.root
    }

    fun pipChange() {
        binding.tags.visibility = View.VISIBLE
        binding.difficulty.visibility = View.VISIBLE
        binding.pipButton.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(lecture: JSONObject) =
            LectureFragment().apply {
                arguments = Bundle().apply {
                    putInt("difficulty", lecture.getInt("difficulty"))
                    val tags = lecture.getJSONArray("tags")
                    val tagsList = arrayListOf<String>()
                    for (i in 0..<tags.length()) {
                        tagsList.add(tags.getString(i))
                    }
                    putStringArrayList("tags", tagsList)
                    putString("url", lecture.getString("url"))
                }
            }
    }
}