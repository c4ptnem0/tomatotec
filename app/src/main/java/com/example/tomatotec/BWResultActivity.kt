package com.example.tomatotec

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import org.w3c.dom.Text

class BWResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bwresult)

        // Hide the title bar
        supportActionBar?.hide()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val aboutDef = findViewById<TextView>(R.id.aboutDef)
        val diseaseWarningIndicator = findViewById<LinearLayout>(R.id.diseaseWarningIndicator)
        val diagnosisBtn = findViewById<ImageView>(R.id.diagnosisBtn)

        val imageList = ArrayList<SlideModel>()

        val image1 = R.drawable.bacterial_wilt_img1
        val image2 = R.drawable.bacterial_wilt_img2
        val image3 = R.drawable.bacterial_wilt_img3
        val image4 = R.drawable.bacterial_wilt_img4

        val definitionString = "Bacterial wilt caused by \"Ralstonia solanacearum\" is a serious disease of tomato plants. It survives for long periods in the soil, even in the absence of host plants. Even though there are now reported tolerant varieties, outbreaks of the disease can still be catastrophic problem."
        val spannableString = SpannableString(definitionString)
        val boldSpan = StyleSpan(Typeface.BOLD)
        val boldItalicSpan = StyleSpan(Typeface.BOLD_ITALIC)
        val italicSpan = StyleSpan(Typeface.ITALIC)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        imageList.add(SlideModel(image1))
        imageList.add(SlideModel(image2))
        imageList.add(SlideModel(image3))
        imageList.add(SlideModel(image4))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        spannableString.setSpan(boldSpan, 0,15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(italicSpan, 26,50, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        aboutDef.text = spannableString

        val predictedClass = intent.getStringExtra("predictedClass")

        if (predictedClass == "Bacterial Wilt") {
            diseaseWarningIndicator.visibility = View.VISIBLE
            diagnosisBtn.visibility = View.VISIBLE

            val animation = AnimationUtils.loadAnimation(this, R.anim.blink_animation)
            diseaseWarningIndicator.startAnimation(animation)

        }

        diagnosisBtn.setOnClickListener {
            // Open intent Activity
            val intent = Intent(this, DiseaseDiagnosisActivity::class.java)
            startActivity(intent)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}