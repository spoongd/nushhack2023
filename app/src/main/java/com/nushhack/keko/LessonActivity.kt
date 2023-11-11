package com.nushhack.keko

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.nushhack.keko.databinding.ActivityLessonBinding
import org.json.JSONObject

class LessonActivity : AppCompatActivity(), PagerCommunication {
    private lateinit var binding: ActivityLessonBinding
    private lateinit var lectureFragment: LectureFragment;
    private val fragments = arrayListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val id = intent.getStringExtra("id")
        // val title = getTitleFromId(id)
        // val lesson = getLessonFromId(id)
        val title = "Test Lesson"
        val lesson = JSONObject("{\n" +
                "  'package': 'packageID',\n" +
                "  'title': 'some title',\n" +
                "  'lecture': 'videoID',\n" +
                "  'quiz': [{'question': 'What  is 1 + 1?', 'options': ['1', '2', '3', '4'], 'answer': 1}, {'question': 'What  is 2 + 1?', 'options': ['1', '2', '3', '4'], 'answer': 2}]\n" +
                "}")

        lectureFragment = LectureFragment.newInstance("nerd")
        fragments.add(lectureFragment)

        val questions = lesson.getJSONArray("quiz")
        for (i in 0..<questions.length()) {
            val question = questions.getJSONObject(i)
            fragments.add(QuizFragment.newInstance(i == questions.length() - 1, question))
        }
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

                binding.toolbarTitle2.text = getString(R.string.question_number_template).format(position + 1)
                if (position == 0) {
                    binding.toolbarTitle.text = getString(R.string.lecture_video_title)
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
    }

    override fun nextPage() {
        binding.content.viewPager.currentItem++
    }

    override fun previousPage() {
        binding.content.viewPager.currentItem--
    }
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (!isInPictureInPictureMode){
            lectureFragment.pipChange()
            supportActionBar?.show()
        }
    }
}