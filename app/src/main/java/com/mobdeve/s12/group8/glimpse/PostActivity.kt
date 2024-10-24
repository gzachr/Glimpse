package com.mobdeve.s12.group8.glimpse

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.group8.glimpse.databinding.ActivityPostBinding

class PostActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRootLayout.requestFocus()

        binding.postIv.post {
            val width = binding.postIv.width
            val layoutParams = binding.postIv.layoutParams
            layoutParams.height = width
            binding.postIv.layoutParams = layoutParams
        }

        binding.captionCounterTv.visibility = View.GONE

        //only show the caption counter if user is focused on it
        binding.captionEt.setOnFocusChangeListener{_, hasFocus ->
            if(hasFocus)
                binding.captionCounterTv.visibility = View.VISIBLE
            else
                binding.captionCounterTv.visibility = View.GONE
        }

        binding.captionEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the character count text view
                val currentLength = s?.length ?: 0
                binding.captionCounterTv.text = "$currentLength/60"
            }
        })

        binding.postBtn.setOnClickListener {
            /**
             * TODO posting logic, save to DB
             */

            finish()
        }

        binding.postExitBtn.setOnClickListener {
            finish()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && event.action == MotionEvent.ACTION_DOWN) {
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    // Hide keyboard and clear focus if clicking outside the EditText
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}