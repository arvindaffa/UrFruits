package com.android.urfruits

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.android.api.ApiClient
import com.android.api.ApiService
import com.android.response.BuahResponse
import com.android.response.Data
import com.google.android.material.progressindicator.CircularProgressIndicator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFragment : Fragment() {

    private var imageUri: String? = null
    private lateinit var imageView: ImageView
    private lateinit var confidencetextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var latinNameTextView: TextView
    private lateinit var plantingMethodTextView: TextView
    private lateinit var articleTextView: TextView
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var groupContent: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUri = it.getString(ARG_IMAGE_URI)
        }

        // Initialize ApiClient
        ApiClient.init(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_result, container, false)
        imageView = view.findViewById(R.id.imageView)
        confidencetextView = view.findViewById(R.id.tvConfidenceScore)
        nameTextView = view.findViewById(R.id.tvNamaBuah)
        latinNameTextView = view.findViewById(R.id.tvNamaLatinBuah)
        plantingMethodTextView = view.findViewById(R.id.tvCaraMerawat)
        articleTextView = view.findViewById(R.id.tvArtikelBuah)
        progressBar = view.findViewById(R.id.progressIndicator)
        groupContent = view.findViewById(R.id.group_content)

        imageUri?.let { uri ->
            try {
                imageView.setImageURI(Uri.parse(uri))
                val file = fromUriToFile(Uri.parse(uri), requireContext())
                Log.d("ScanFragment", "File created: ${file.path}")
                uploadImage(file)
            } catch (e: Exception) {
                Log.e("ScanFragment", "Error processing image URI", e)
            }
        } ?: Log.e("ScanFragment", "imageUri is null")

        return view
    }

    private fun showLoading(showLoading: Boolean) {
        progressBar.visibility = if (showLoading) View.VISIBLE else View.GONE
        groupContent.visibility = if (showLoading) View.GONE else View.VISIBLE
    }

    private fun uploadImage(file: File) {
        val apiService = ApiClient.apiService
        val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)

        Log.d("ScanFragment", "Uploading image: ${file.name}")
        showLoading(true)
        apiService.uploadImage(body).enqueue(object : Callback<BuahResponse> {
            override fun onResponse(call: Call<BuahResponse>, response: Response<BuahResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        Log.d("ScanFragment", "API response: ${response.body()}")
                        val name = data.name.orEmpty()
                        fetchFruitDetails(name)
                    }
                } else {
                    Log.e("ScanFragment", "API call failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BuahResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                Log.e("ScanFragment", "API call failed", t)
            }
        })
    }

    private fun fetchFruitDetails(name: String) {
        val apiService = ApiClient.apiService
        apiService.getFruitDetails(name).enqueue(object : Callback<BuahResponse> {
            override fun onResponse(call: Call<BuahResponse>, response: Response<BuahResponse>) {
                if (response.isSuccessful) {
                    showLoading(false)
                    response.body()?.data?.let { data ->
                        displayFruitDetails(data)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch fruit details", Toast.LENGTH_SHORT).show()
                    Log.e("ScanFragment", "API call failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BuahResponse>, t: Throwable) {
                Log.e("ScanFragment", "API call failed", t)
            }
        })
    }

    private fun displayFruitDetails(data: Data) {
        nameTextView.text = data.name
        confidencetextView.text = data.confidenceScore
        latinNameTextView.text = data.namaLatin
        plantingMethodTextView.text = data.caraMenanam
        articleTextView.text = data.sumber
        articleTextView.text = data.sumber
        // Load image from URL if needed using Glide or Picasso
        // Glide.with(this).load(data.image).into(imageView)
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"

        fun newInstance(imageUri: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                }
            }
    }
}
