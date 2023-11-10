package com.nushhack.keko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.nushhack.keko.databinding.ActivityLessonBinding
import org.json.JSONObject

class LessonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLessonBinding
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

        val questions = lesson.getJSONArray("quiz")
        for (i in 0..<questions.length()) {
            val question = questions.getJSONObject(i)
            fragments.add(QuizFragment.newInstance(question))
        }
        binding.content.viewPager.adapter = LessonPagerAdapter(this, fragments)
        binding.content.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                if (!binding.toolbarTitle2.isVisible) binding.toolbarTitle2.isVisible = true

                binding.toolbarTitle.translationX = -positionOffset * binding.toolbar.width
                binding.toolbarTitle2.translationX = -positionOffset * binding.toolbar.width + binding.toolbar.width

                binding.toolbarTitle2.text = getString(R.string.question_number_template).format(position + 2)

                binding.toolbarTitle.text = getString(R.string.question_number_template).format((position + 1))

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
}