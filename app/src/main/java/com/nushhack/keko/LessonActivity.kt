package com.nushhack.keko

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nushhack.keko.databinding.ActivityLessonBinding
import org.json.JSONObject

class LessonActivity : AppCompatActivity(), PagerCommunication, ScoreCommunication {
    private lateinit var binding: ActivityLessonBinding
    private lateinit var lectureFragment: LectureFragment
    private lateinit var completionFragment: CompletionFragment
    private val fragments = arrayListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val id = intent.getStringExtra(getString(R.string.intent_lesson_id))

        val queue = Volley.newRequestQueue(this)

        queue.add(
            StringRequest(Request.Method.GET, "http://34.125.233.174:5000/lesson?id=$id",
            {response ->
                val lesson = JSONObject(response)
                val title = lesson.getString("title")
                lectureFragment = LectureFragment.newInstance(lesson.getJSONObject("lecture"))
                fragments.add(lectureFragment)
                val questions = lesson.getJSONArray("quiz")
                for (i in 0..<questions.length()) {
                    val question = questions.getJSONObject(i)
                    fragments.add(QuizFragment.newInstance(i == questions.length() - 1, question))
                }
                completionFragment = CompletionFragment.newInstance(title, questions.length())
                fragments.add(completionFragment)

                binding.content.viewPager.adapter = LessonPagerAdapter(this, fragments)
                binding.content.viewPager.isUserInputEnabled = false
                binding.content.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                        if (!binding.toolbarTitle2.isVisible) binding.toolbarTitle2.isVisible = true

                        binding.toolbarTitle.translationX = -positionOffset * binding.toolbar.width
                        binding.toolbarTitle2.translationX = -positionOffset * binding.toolbar.width + binding.toolbar.width

                        if (position == questions.length()) {
                            binding.toolbarTitle2.text = getString(R.string.completed_title)
                        } else {
                            binding.toolbarTitle2.text = getString(R.string.question_number_template).format(position + 1)
                        }
                        if (position == 0) {
                            binding.toolbarTitle.text = getString(R.string.lecture_video_title)
                        } else if (position == questions.length() + 1) {
                            binding.toolbarTitle.text = getString(R.string.completed_title)
                        } else {
                            binding.toolbarTitle.text = getString(R.string.question_number_template).format(position)
                        }
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        when(state) {
                            ViewPager2.SCROLL_STATE_IDLE -> {
                                if (binding.toolbarTitle2.isVisible) binding.toolbarTitle2.isVisible = false
                            }
                            else -> {}
                        }
                        super.onPageScrollStateChanged(state)
                    }
                })
                binding.content.indicator.setViewPager(binding.content.viewPager)
            }, {})
        )
    }

    override fun nextPage() {
        binding.content.viewPager.currentItem++
    }

    override fun previousPage() {
        binding.content.viewPager.currentItem--
    }

    override fun increaseScore() {
        completionFragment.increaseScore()
    }
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (!isInPictureInPictureMode){
            lectureFragment.pipChange()
            supportActionBar?.show()
        }
    }
}