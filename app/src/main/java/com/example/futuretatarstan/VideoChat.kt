package com.example.futuretatarstan


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity


class VideoChat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_chat)


        val home: Button = findViewById(R.id.button_glavn)
        home.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }
        val lk: Button = findViewById(R.id.button_lich)
        lk.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profile: ImageView = findViewById(R.id.profileIcon)
        profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }



        val confirmButton:Button = findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener {







            val levelRadioGroup:RadioGroup = findViewById(R.id.levelRadioGroup)
            val ratingRadioGroup:RadioGroup = findViewById(R.id.ratingRadioGroup)


            val selectedLevelId = levelRadioGroup.checkedRadioButtonId
            val selectedRatingId = ratingRadioGroup.checkedRadioButtonId


            if (selectedLevelId == R.id.intermediateRadioButton || selectedLevelId == R.id.noviceRadioButton || selectedLevelId == R.id.advancedRadioButton ||
                selectedRatingId == R.id.between200And500RadioButton) {

                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
