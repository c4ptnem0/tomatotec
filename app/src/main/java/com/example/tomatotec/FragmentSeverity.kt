package com.example.tomatotec

import android.content.Intent
import android.graphics.Typeface
import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import com.google.common.primitives.UnsignedBytes.toInt
import kotlin.math.roundToInt

class FragmentSeverity : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var imageAdd1: ImageView
    private lateinit var imageAdd2: ImageView
    private lateinit var imageAdd3: ImageView
    private lateinit var imageAdd4: ImageView
    private lateinit var imageAdd5: ImageView
    private lateinit var imageMinus1: ImageView
    private lateinit var imageMinus2: ImageView
    private lateinit var imageMinus3: ImageView
    private lateinit var imageMinus4: ImageView
    private lateinit var imageMinus5: ImageView
    private lateinit var HRCountTV: TextView
    private lateinit var RCountTV: TextView
    private lateinit var MRCountTV: TextView
    private lateinit var SPCountTV: TextView
    private lateinit var HSCountTV: TextView

    private lateinit var aboutDef1: TextView
    private lateinit var aboutDef2: TextView
    private lateinit var aboutDef3: TextView
    private lateinit var aboutDef4: TextView
    private lateinit var aboutDef5: TextView

    private lateinit var severityBtnCV: LinearLayout
    private lateinit var calculateBtnCV: CardView
    private lateinit var clearAllBtnTV: TextView

    var totalDamageScore: Double = 0.0
    var totalPlantSample: Double = 0.0
    var totalScore: Double = 0.0
    var severityPercentage: Double = 0.0

    var HRCount = 0.0
    var RCount = 0.0
    var MRCount = 0.0
    var SPCount = 0.0
    var HSCount = 0.0

    var HRCountScore = 0.0
    var RCountScore = 0.0
    var MRCountScore = 0.0
    var SPCountScore = 0.0
    var HSCountScore = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_severity, container, false)

        imageAdd1 = view.findViewById(R.id.imageAdd1)
        imageAdd2 = view.findViewById(R.id.imageAdd2)
        imageAdd3 = view.findViewById(R.id.imageAdd3)
        imageAdd4 = view.findViewById(R.id.imageAdd4)
        imageAdd5 = view.findViewById(R.id.imageAdd5)
        imageMinus1 = view.findViewById(R.id.imageMinus1)
        imageMinus2 = view.findViewById(R.id.imageMinus2)
        imageMinus3 = view.findViewById(R.id.imageMinus3)
        imageMinus4 = view.findViewById(R.id.imageMinus4)
        imageMinus5 = view.findViewById(R.id.imageMinus5)
        HRCountTV = view.findViewById(R.id.HRCountTV)
        RCountTV = view.findViewById(R.id.RCountTV)
        MRCountTV = view.findViewById(R.id.MRCountTV)
        SPCountTV = view.findViewById(R.id.SPCountTV)
        HSCountTV = view.findViewById(R.id.HSCountTV)

        aboutDef1 = view.findViewById(R.id.aboutDef1)
        aboutDef2 = view.findViewById(R.id.aboutDef2)
        aboutDef3 = view.findViewById(R.id.aboutDef3)
        aboutDef4 = view.findViewById(R.id.aboutDef4)
        aboutDef5 = view.findViewById(R.id.aboutDef5)

        severityBtnCV = view.findViewById(R.id.severityBtnCV)
        calculateBtnCV = view.findViewById(R.id.calculateBtnCV)
        clearAllBtnTV = view.findViewById(R.id.clearAllBtnTV)


        val aboutDef1String = "1 - less than 20% symptoms"
        val aboutDef2String = "2 - 21% to 40% symptoms"
        val aboutDef3String = "3 - 41% to 60% symptoms"
        val aboutDef4String = "4 - 61% to 80% symptoms"
        val aboutDef5String = "5 - 81% to 100% symtoms"

        val spannableString1 = SpannableString(aboutDef1String)
        val spannableString2 = SpannableString(aboutDef2String)
        val spannableString3 = SpannableString(aboutDef3String)
        val spannableString4 = SpannableString(aboutDef4String)
        val spannableString5 = SpannableString(aboutDef5String)

        // font style
        val boldItalicSpan = StyleSpan(Typeface.BOLD_ITALIC)

        // applying the font style
        spannableString1.setSpan(boldItalicSpan, 4,26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString2.setSpan(boldItalicSpan, 4,23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString3.setSpan(boldItalicSpan, 4,23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString4.setSpan(boldItalicSpan, 4,23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString5.setSpan(boldItalicSpan, 4,23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        aboutDef1.text = spannableString1
        aboutDef2.text = spannableString2
        aboutDef3.text = spannableString3
        aboutDef4.text = spannableString4
        aboutDef5.text = spannableString5

        // function call
        setHRCount()
        setRCount()
        setMRCount()
        setSPCount()
        setHSCount()
        clearAllBtnVisibility()

        // open the severity activity
        severityBtnCV.setOnClickListener {
            // Open intent Activity
            val intent = Intent(activity, DiseaseDiagnosisActivity::class.java)
            startActivity(intent)
        }

        calculateBtnCV.setOnClickListener {

            if(HRCount > 0 || RCount > 0 || MRCount > 0 || SPCount > 0 || HSCount > 0) {

                severityCalculationFormula()

                // show management advice popup fragment
                val showPopUp = FragmentManagementAdvice()
                showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "FragmentManagementAdvice")
            }
            else {
                // Show a toast message indicating no input
                Toast.makeText(requireContext(), "Please provide inputs to calculate!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun clearAllBtnVisibility() {
        if (HRCount > 0 || RCount > 0 || MRCount > 0 || SPCount > 0 || HSCount > 0) {
            clearAllBtnTV.visibility = View.VISIBLE
            clearAllDatas()
        } else {
            clearAllBtnTV.visibility = View.GONE
        }
    }

    private fun clearAllDatas() {
        // Reset all variables to their initial state
        clearAllBtnTV.setOnClickListener {

            HRCount = 0.0
            RCount = 0.0
            MRCount = 0.0
            SPCount = 0.0
            HSCount = 0.0

            HRCountScore = 0.0
            RCountScore = 0.0
            MRCountScore = 0.0
            SPCountScore = 0.0
            HSCountScore = 0.0

            totalDamageScore = 0.0
            totalPlantSample = 0.0
            totalScore = 0.0
            severityPercentage = 0.0

            // Update the TextViews
            HRCountTV.text = "0"
            RCountTV.text = "0"
            MRCountTV.text = "0"
            SPCountTV.text = "0"
            HSCountTV.text = "0"

            clearAllBtnTV.visibility = View.GONE
        }
    }

    private fun severityCalculationFormula() {
        totalDamageScore = HRCountScore + RCountScore + MRCountScore + SPCountScore + HSCountScore
        totalPlantSample = HRCount + RCount + MRCount + SPCount + HSCount

        // calculate the average score
        totalScore = totalDamageScore / totalPlantSample
        // calculate the severity percentage
        severityPercentage = ((totalScore / 5) * 100)
        // round to the nearest int
        val roundedSeverityPercentage = severityPercentage.roundToInt()

        sharedViewModel.roundedSeverityPercentage = roundedSeverityPercentage

        Toast.makeText(requireContext(), "Severity Percentage: $roundedSeverityPercentage", Toast.LENGTH_SHORT).show()
    }

    // Highest Resistance (HS)
    private fun setHRCount() {
        imageAdd1.setOnClickListener {
            HRCount += 1
            HRCountScore = HRCount * 1
            HRCountTV.text = HRCount.toInt().toString()

            clearAllBtnVisibility()
        }

        imageMinus1.setOnClickListener {
            if(HRCount > 0) {
                HRCount -= 1
                HRCountScore = HRCount - 1
                HRCountTV.text = HRCount.toInt().toString()

                clearAllBtnVisibility()
            }
        }
    }
    // Resistance (R)
    private fun setRCount() {
        imageAdd2.setOnClickListener {
            RCount += 1
            RCountScore = RCount * 2
            RCountTV.text = RCount.toInt().toString()

            clearAllBtnVisibility()
        }

        imageMinus2.setOnClickListener {
            if(RCount > 0) {
                RCount -= 1
                RCountScore = RCount - 2
                RCountTV.text = RCount.toInt().toString()

                clearAllBtnVisibility()
            }
        }
    }
    // Moderate Resistance (MR)
    private fun setMRCount() {
        imageAdd3.setOnClickListener {
            MRCount += 1
            MRCountScore = MRCount * 3
            MRCountTV.text = MRCount.toInt().toString()

            clearAllBtnVisibility()
        }

        imageMinus3.setOnClickListener {
            if(MRCount > 0) {
                MRCount -= 1
                MRCountScore = MRCount - 3
                MRCountTV.text = MRCount.toInt().toString()

                clearAllBtnVisibility()
            }
        }
    }
    // Susceptible (SP)
    private fun setSPCount() {
        imageAdd4.setOnClickListener {
            SPCount += 1
            SPCountScore = SPCount * 4
            SPCountTV.text = SPCount.toInt().toString()

            clearAllBtnVisibility()
        }

        imageMinus4.setOnClickListener {
            if(SPCount > 0) {
                SPCount -= 1
                SPCountScore = SPCount - 4
                SPCountTV.text = SPCount.toInt().toString()

                clearAllBtnVisibility()
            }
        }
    }
    // High Susceptible (HS)
    private fun setHSCount() {
        imageAdd5.setOnClickListener {
            HSCount += 1
            HSCountScore = HSCount * 5
            HSCountTV.text = HSCount.toInt().toString()

            clearAllBtnVisibility()
        }

        imageMinus5.setOnClickListener {
            if(HSCount > 0) {
                HSCount -= 1
                HSCountScore = HSCount - 5
                HSCountTV.text = HSCount.toInt().toString()

                clearAllBtnVisibility()
            }
        }
    }
}