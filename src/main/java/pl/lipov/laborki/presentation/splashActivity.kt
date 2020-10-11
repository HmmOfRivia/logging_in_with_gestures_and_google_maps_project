package pl.lipov.laborki.presentation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import pl.lipov.laborki.R


class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animator = ValueAnimator.ofInt(0, progressBar.max)
        animator.duration = 3000
        animator.addUpdateListener { animation ->
            progressBar.progress =
                (animation.animatedValue as Int)
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val intent = Intent(this@splashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

                super.onAnimationEnd(animation)
            }
        })
        animator.start()
    }
}
