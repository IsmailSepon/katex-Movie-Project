package com.sepon.katexentertainment.ui.search.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class SearchResponse(

	@field:SerializedName("expression")
	val expression: String? = null,

	@field:SerializedName("searchType")
	val searchType: String? = null,

	@field:SerializedName("errorMessage")
	val errorMessage: String? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null
)

@Keep
@Parcelize
data class ResultsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("resultType")
	val resultType: String? = null
): Parcelable
