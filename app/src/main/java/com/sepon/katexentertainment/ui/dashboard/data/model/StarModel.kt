package com.sepon.katexentertainment.ui.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class StarModel (

    @field:SerializedName("image")
    var image: String? = null,

    @field:SerializedName("title")
    var title: String? = null,

    @field:SerializedName("fulltitle")
    var fulltitle: String? = null

)