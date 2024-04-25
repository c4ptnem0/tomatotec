package com.example.tomatotec

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView


class FragmentHome : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        fun navigateToFragmentSeverity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val home_ins_definition = view.findViewById<TextView>(R.id.home_ins_definition)
        val cvBWDefinition = view.findViewById<CardView>(R.id.cvBWDefinition)
        val severityBtnCV = view.findViewById<LinearLayout>(R.id.severityBtnCV)

        val definitionString = "Bacterial wilt is caused by a soil-borne bacterium named \"Ralstonia solanacearum\" (formerly known as Pseudomonas solonacearum). Tomato wilt bacterium mainly inhabits the roots, and enters the root system at points of injury caused by farm tools or equipment and soil pests."
        val spannableString = SpannableString(definitionString)
        val boldSpan = StyleSpan(Typeface.BOLD)
        val boldItalicSpan = StyleSpan(Typeface.BOLD_ITALIC)
        val italicSpan = StyleSpan(Typeface.ITALIC)

        spannableString.setSpan(boldSpan,0,14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(boldItalicSpan,57,82, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(italicSpan, 101, 126, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set the modified text to the TextView
        home_ins_definition.text = spannableString

        cvBWDefinition.setOnClickListener {
            // Open intent Activity
            val intent = Intent(activity, BWResultActivity::class.java)
            startActivity(intent)
        }

        severityBtnCV.setOnClickListener {
            listener?.navigateToFragmentSeverity()
        }

        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}
