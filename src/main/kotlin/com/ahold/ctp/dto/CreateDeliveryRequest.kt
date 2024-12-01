package com.ahold.ctp.dto

import com.ahold.ctp.model.Status

data class CreateDeliveryRequest(
//"vehicleId": "AHV-589",
//  "startedAt": "2023-10-09T12:45:34.678Z",
//  "status": "IN_PROGRESS"
    val vehicleId:String,
    val startedAt:String,
    val status: Status
)
