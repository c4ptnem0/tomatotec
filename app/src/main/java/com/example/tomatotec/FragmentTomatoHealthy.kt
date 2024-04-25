package com.example.tomatotec

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment

class FragmentTomatoHealthy : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_tomato_healthy, container, false)

        val closeBtn = view.findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            dismiss()
        }

        return view
    }
}