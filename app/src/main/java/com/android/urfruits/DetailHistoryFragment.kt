package com.android.urfruits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.android.response.History
import com.google.android.material.progressindicator.CircularProgressIndicator

class DetailHistoryFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var confidencetextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var latinNameTextView: TextView
    private lateinit var plantingMethodTextView: TextView
    private lateinit var articleTextView: TextView
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var groupContent: Group

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val history = arguments?.getParcelable<History>(EXTRA_HISTORY)
        if (history == null) {
            Toast.makeText(requireContext(), "History not found", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }

        imageView = view.findViewById(R.id.imageView)
        confidencetextView = view.findViewById(R.id.tvConfidenceScore)
        nameTextView = view.findViewById(R.id.tvNamaBuah)
        latinNameTextView = view.findViewById(R.id.tvNamaLatinBuah)
        plantingMethodTextView = view.findViewById(R.id.tvCaraMerawat)
        articleTextView = view.findViewById(R.id.tvArtikelBuah)
        progressBar = view.findViewById(R.id.progressIndicator)
        groupContent = view.findViewById(R.id.group_content)

        // Set the data
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        imageView.load(history?.image) {
            placeholder(circularProgressDrawable)
            crossfade(true)
        }
        nameTextView.text = history?.name ?: history?.nama.orEmpty()
        confidencetextView.text = history?.confidenceScore
        latinNameTextView.text = history?.namaLatin
        plantingMethodTextView.text = history?.caraMenanam
        articleTextView.text = history?.sumber
        progressBar.isVisible = false
        groupContent.isVisible = true
    }

    companion object {
        const val EXTRA_HISTORY = "extra_history"
    }
}