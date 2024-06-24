package com.android.urfruits

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.api.ApiClient
import com.android.response.BuahResponse
import com.android.response.History
import com.android.response.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val buahFilterAdapter by lazy { BuahFilterAdapter() }
    private val buahAdapter by lazy { BuahAdapter() }

    private var listFilter: List<BuahFilterData> = emptyList()
    private var listHistory: List<History> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        ApiClient.init(requireContext())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userNameTextView: TextView = view.findViewById(R.id.nameProfile)
        val userName = sharedPreferences.getString("USER_NAME", "Default Name") // Replace "Default Name" with a default value if needed
        userNameTextView.text = userName

        // Inisialisasi RecyclerView dan Adapter
        val filterRecyclerView = view.findViewById<RecyclerView>(R.id.rv_filter)
        filterRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.adapter = buahFilterAdapter

        val historyRecyclerView = requireView().findViewById<RecyclerView>(R.id.rvBuah)
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.adapter = buahAdapter

        // Set listener ketika button filter diklik
        buahFilterAdapter.onItemClick = { selectedFilter ->
            // Mengubah filter yang dipilih menjadi selected
            listFilter = listFilter.map {
                it.copy(selected = it.id == selectedFilter.id)
            }
            // Mengirim list filter yang sudah diubah ke adapter
            buahFilterAdapter.submitList(listFilter)
            // Menampilkan history sesuai filter yang dipilih
            showHistory()
        }

        // Set listener ketika button "Lihat Semua" diklik
        val seeAllButton = view.findViewById<TextView>(R.id.seeAllButton)
        seeAllButton.setOnClickListener {
            // Mengubah selected pada semua filter menjadi false
            listFilter = listFilter.map {
                it.copy(selected = false)
            }
            // Mengirim list filter yang sudah diubah ke adapter
            buahFilterAdapter.submitList(listFilter)
            // Menampilkan semua history
            showHistory()
        }

        // Set listener ketika item history diklik
        buahAdapter.onItemClick = { history ->
            // Menavigasi ke DetailHistoryFragment
            val detailHistoryFragment = DetailHistoryFragment()
            val bundle = Bundle()
            // Mengirim data history ke DetailHistoryFragment
            bundle.putParcelable(DetailHistoryFragment.EXTRA_HISTORY, history)
            detailHistoryFragment.arguments = bundle
            // Menampilkan DetailHistoryFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailHistoryFragment)
                .addToBackStack(null)
                .commit()
        }

        fetchHistory()
    }

    // Fungsi untuk mengambil data history dari API
    private fun fetchHistory() {
        val apiService = ApiClient.apiService
        apiService.getHistory().enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if(!data.isNullOrEmpty()) {
                        // Mengambil list history dari data
                        listHistory = data.mapNotNull { it?.history }

                        // Membuat list filter dari list history dengan menghilangkan nama yang sama
                        val uniqueList = data.distinctBy { it?.history?.name }
                        // Mengubah list history menjadi list filter
                        listFilter = uniqueList.mapIndexed { index, dataItem ->
                            // Mengubah data history menjadi data filter
                            BuahFilterData(dataItem?.id.orEmpty(), dataItem?.history?.name ?: dataItem?.history?.nama.orEmpty())
                        }.sortedBy { it.name }
                        buahFilterAdapter.submitList(listFilter)

                        showHistory()
                    }
                } else {
                    Log.e("ScanFragment", "API call failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("ScanFragment", "API call failed", t)
            }
        })
    }

    private fun showHistory() {
        // Mengambil filter yang dipilih
        val selectedFilter = listFilter.find { it.selected == true }
        // Mengambil history yang sesuai dengan filter yang dipilih
        val filteredHistoryList = if (selectedFilter != null) {
            listHistory.filter { it.name == selectedFilter.name || it.nama == selectedFilter.name }
        } else {
            listHistory
        }
        // Mengirim history yang sudah difilter ke adapter
        buahAdapter.setData(filteredHistoryList)
    }
}
