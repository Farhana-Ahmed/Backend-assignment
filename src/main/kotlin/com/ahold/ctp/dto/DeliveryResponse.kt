package com.ahold.ctp.dto

import java.util.*

data class DeliveryResponse(
      val id: String,
      val vehicleId: String,
      val startedAt:String,
      val finishedAt:String?,
      val status:String
)
