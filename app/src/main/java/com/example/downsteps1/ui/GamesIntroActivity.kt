package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.example.downsteps1.R
import java.util.Calendar

class GamesIntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games_intro)

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        tvGreeting.text = if (hour < 12) {
            "Good morning, let’s start playing ☀️"
        } else {
            "Good evening, shall we play a quick game? 🌙"
        }

        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        startDotsAnimation(dot1, dot2, dot3)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, GamesActivity::class.java))
            finish()
        }, 4000)
    }

    private fun startDotsAnimation(dot1: View, dot2: View, dot3: View) {
        animateDot(dot1, 0)
        animateDot(dot2, 200)
        animateDot(dot3, 400)
    }

    private fun animateDot(view: View, delay: Long) {
        view.postDelayed(object : Runnable {
            override fun run() {
                view.animate()
                    .translationY(-12f)
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(250)
                    .withEndAction {
                        view.animate()
                            .translationY(0f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(250)
                            .withEndAction {
                                view.postDelayed(this, 200)
                            }
                            .start()
                    }
                    .start()
            }
        }, delay)
    }
}