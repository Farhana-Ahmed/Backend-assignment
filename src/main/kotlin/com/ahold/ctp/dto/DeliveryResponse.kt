package com.ahold.ctp.dto

import java.util.*

data class DeliveryResponse(

//    "id": "69201507-0ae4-4c56-ac2d-75fbe27efad8",
//    "vehicleId": "AHV-589"
//    "startedAt": "2023-10-09T12:45:34.678Z",
//    "finishedAt": null,
//    "status": "IN_PROGRESS"

val id: String,
      val vehicleId: String,
    val startedAt:String,
    val finishedAt:String?,
    val status:String


)
