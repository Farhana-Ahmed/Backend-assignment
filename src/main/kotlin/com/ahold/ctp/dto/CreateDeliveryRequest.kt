package com.ahold.ctp.dto

import com.ahold.ctp.model.Status
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateDeliveryRequest(

    @field:NotBlank(message = "VehicleId must not be Empty")
    val vehicleId:String,

    @field:NotBlank(message = "startedAt must not be Empty")
    val startedAt:String,

    @field:NotNull(message = "Status must be provided")
    val status: Status
)
