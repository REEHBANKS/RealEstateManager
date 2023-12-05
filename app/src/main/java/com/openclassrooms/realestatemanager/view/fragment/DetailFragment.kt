package com.openclassrooms.realestatemanager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.openclassrooms.realestatemanager.R

class DetailFragment : Fragment() {

    companion object {
        private const val ARG_DETAIL = "detail"

        fun newInstance(detailString: String): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle().apply {
                putString(ARG_DETAIL, detailString)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupérer le String passé dans les arguments du fragment
        val detailString = arguments?.getString(ARG_DETAIL)

        // Utiliser le String pour configurer le TextView
        view.findViewById<TextView>(R.id.detailTextView).text = detailString
    }

}