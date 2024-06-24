package com.android.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class History(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: String? = null,

	@field:SerializedName("cara menanam")
	val caraMenanam: String? = null,

	@field:SerializedName("sumber")
	val sumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama latin")
	val namaLatin: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null
): Parcelable

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("history")
	val history: History? = null
)
