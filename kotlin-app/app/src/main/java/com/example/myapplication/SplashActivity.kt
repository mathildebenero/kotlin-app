package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.os.Handler
import android.widget.Switch


// The kotlin code for the animation

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen) // show us the splash_screen.xml content
        val switch: Switch = findViewById(R.id.my_switch) // sets the switch button if we want to skip the animation
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                transitionToMainActivity()
            } else {
                // if the switch is off we do nothing
            }
        }

        // initiates each hamburger layer and each fries

        val breadBottom = findViewById<ImageView>(R.id.breadBottom)
        val tomato = findViewById<ImageView>(R.id.tomato)
        val meat = findViewById<ImageView>(R.id.burger_meat)
        val lettuce = findViewById<ImageView>(R.id.lettuce)
        val onion = findViewById<ImageView>(R.id.onion)
        val breadTop = findViewById<ImageView>(R.id.breadTop)
        val fries1 = findViewById<ImageView>(R.id.fries1)
        val fries2 = findViewById<ImageView>(R.id.fries2)
        val fries3 = findViewById<ImageView>(R.id.fries3)
        val fries4 = findViewById<ImageView>(R.id.fries4)
        val fries5 = findViewById<ImageView>(R.id.fries5)
        val fries6 = findViewById<ImageView>(R.id.fries6)
        val fries7 = findViewById<ImageView>(R.id.fries7)
        val fries8 = findViewById<ImageView>(R.id.fries8)

        // sets each animation for each image and setting the delay so not all animations happen in the same time

        animateLayer(breadBottom, R.anim.drop_in_bottom, 0)
        animateLayer(tomato, R.anim.drop_in_tomato, 1500)
        animateLayer(meat, R.anim.drop_in_meat, 3000)
        animateLayer(lettuce, R.anim.drop_in_lettuce, 4500)
        animateLayer(onion, R.anim.drop_in_onion, 6000)
        animateLayer(breadTop, R.anim.drop_in_top, 7500)
        animateLayer(fries1, R.anim.fries1_anim, 8500)
        animateLayer(fries2, R.anim.fries2_anim, 9500)
        animateLayer(fries3, R.anim.fries3_anim, 10500)
        animateLayer(fries4, R.anim.fries4_anim, 11500)
        animateLayer(fries5, R.anim.fries5_anim, 12500)
        animateLayer(fries6, R.anim.fries6_anim, 13500)
        animateLayer(fries7, R.anim.fries7_anim, 14500)
        animateLayer(fries8, R.anim.fries7_anim, 15500)

        // we transit to the MainActivity file after the animations finished to show up
            Handler().postDelayed({
                transitionToMainActivity()
            }, 17000) // Delay enough time to show all animations before transitioning        }

    }

    // the function to start the animations
    private fun animateLayer(view: ImageView, animationId: Int, delay: Long) {
        Handler().postDelayed({
            view.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, animationId)
            view.startAnimation(animation)
        }, delay)
    }

// after showing all animations we transit to the main screen
    private fun transitionToMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
