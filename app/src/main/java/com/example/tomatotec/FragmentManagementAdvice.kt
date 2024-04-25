package com.example.tomatotec

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import org.w3c.dom.Text

class FragmentManagementAdvice : DialogFragment() {
    private lateinit var closeBtn: ImageView
    private lateinit var severityPercentageTV: TextView
    private lateinit var culturalMeasuresTV: TextView
    private lateinit var biologicalMeasuresTV: TextView
    private lateinit var chemicalMeasuresTV: TextView
    private lateinit var monitoringMeasuresTV: TextView
    private lateinit var levelTV: TextView
    private lateinit var severeMessageTV: TextView

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var roundedSeverityPercentage: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_management_advice, container, false)

        closeBtn = view.findViewById(R.id.closeBtn)
        severityPercentageTV = view.findViewById(R.id.severityPercentageTV)
        culturalMeasuresTV = view.findViewById(R.id.culturalMeasuresTV)
        biologicalMeasuresTV = view.findViewById(R.id.biologicalMeasuresTV)
        chemicalMeasuresTV = view.findViewById(R.id.chemicalMeasuresTV)
        monitoringMeasuresTV = view.findViewById(R.id.monitoringMeasuresTV)
        levelTV = view.findViewById(R.id.levelTV)
        severeMessageTV = view.findViewById(R.id.severeMessageTV)

        closeBtn.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the severity percentage from the shared view model
        roundedSeverityPercentage = sharedViewModel.roundedSeverityPercentage

        // set the severity percentage
        severityPercentageTV.text = "$roundedSeverityPercentage%"

        // condition for management advice based on severity disease levels
        if(roundedSeverityPercentage <= 20) {
            Toast.makeText(requireContext(), "Low Severity", Toast.LENGTH_SHORT).show()

            levelTV.setTextColor(resources.getColor(R.color.low_severity))
            levelTV.text = getString(R.string.lvl1_title)
            severeMessageTV.text = getString(R.string.lvl1_msg)
            severityPercentageTV.setTextColor(resources.getColor(R.color.low_severity))
            culturalMeasuresTV.text = getString(R.string.lvl1_cultural_measures)
            biologicalMeasuresTV.text = getString(R.string.lvl1_biological_measures)
            chemicalMeasuresTV.text = getString(R.string.lvl1_chemical_measures)
            monitoringMeasuresTV.text = getString(R.string.lvl1_monitoring_measures)
        }
        else if (roundedSeverityPercentage in 21..40) {
            Toast.makeText(requireContext(), "Mild Severity", Toast.LENGTH_SHORT).show()

            levelTV.setTextColor(resources.getColor(R.color.mild_severity))
            levelTV.text = getString(R.string.lvl2_title)
            severeMessageTV.text = getString(R.string.lvl2_msg)
            severityPercentageTV.setTextColor(resources.getColor(R.color.mild_severity))
            culturalMeasuresTV.text = getString(R.string.lvl2_cultural_measures)
            biologicalMeasuresTV.text = getString(R.string.lvl2_biological_measures)
            chemicalMeasuresTV.text = getString(R.string.lvl2_chemical_measures)
            monitoringMeasuresTV.text = getString(R.string.lvl2_monitoring_measures)
        } else if (roundedSeverityPercentage in 41..60) {
            Toast.makeText(requireContext(), "Moderate Severity", Toast.LENGTH_SHORT).show()

            levelTV.setTextColor(resources.getColor(R.color.moderate_severity))
            levelTV.text = getString(R.string.lvl3_title)
            severeMessageTV.text = getString(R.string.lvl3_msg)
            severityPercentageTV.setTextColor(resources.getColor(R.color.moderate_severity))
            culturalMeasuresTV.text = getString(R.string.lvl3_cultural_measures)
            biologicalMeasuresTV.text = getString(R.string.lvl3_biological_measures)
            chemicalMeasuresTV.text = getString(R.string.lvl3_chemical_measures)
            monitoringMeasuresTV.text = getString(R.string.lvl3_monitoring_measures)
        } else if (roundedSeverityPercentage in 61..80) {
            Toast.makeText(requireContext(), "High Severity", Toast.LENGTH_SHORT).show()

            levelTV.setTextColor(resources.getColor(R.color.high_severity))
            levelTV.text = getString(R.string.lvl4_title)
            severeMessageTV.text = getString(R.string.lvl4_msg)
            severityPercentageTV.setTextColor(resources.getColor(R.color.high_severity))
            culturalMeasuresTV.text = getString(R.string.lvl4_cultural_measures)
            biologicalMeasuresTV.text = getString(R.string.lvl4_biological_measures)
            chemicalMeasuresTV.text = getString(R.string.lvl4_chemical_measures)
            monitoringMeasuresTV.text = getString(R.string.lvl4_monitoring_measures)
        } else {
            Toast.makeText(requireContext(), "Very High Severity", Toast.LENGTH_SHORT).show()

            levelTV.setTextColor(resources.getColor(R.color.very_high_severity))
            levelTV.text = getString(R.string.lvl5_title)
            severeMessageTV.text = getString(R.string.lvl5_msg)
            severityPercentageTV.setTextColor(resources.getColor(R.color.very_high_severity))
            culturalMeasuresTV.text = getString(R.string.lvl5_cultural_measures)
            biologicalMeasuresTV.text = getString(R.string.lvl5_biological_measures)
            chemicalMeasuresTV.text = getString(R.string.lvl5_chemical_measures)
            monitoringMeasuresTV.text = getString(R.string.lvl5_monitoring_measures)
        }
    }

}