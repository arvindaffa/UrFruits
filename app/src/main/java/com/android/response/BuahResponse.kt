package com.android.response

import com.google.gson.annotations.SerializedName

data class BuahResponse(
	@SerializedName("status")
	val status: String? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("data")
	val data: Data? = null
)

data class Data(
	@SerializedName("id")
	val id: String? = null,

	@SerializedName("createdAt")
	val createdAt: String? = null,

	@SerializedName("confidenceScore")
	val confidenceScore: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("nama latin")
	val namaLatin: String? = null,

	@SerializedName("cara menanam")
	val caraMenanam: String? = null,

	@SerializedName("sumber")
	val sumber: String? = null,

	@SerializedName("image")
	val image: String? = null
)
