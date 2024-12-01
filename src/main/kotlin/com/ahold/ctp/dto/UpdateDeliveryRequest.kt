package com.ahold.ctp.dto

import com.ahold.ctp.model.Status
import jakarta.validation.constraints.NotNull
import java.util.*

data class UpdateDeliveryRequest(
//    val id: UUID,

    val finishedAt: String ?= null,

    @field:NotNull(
        message = "Status must be required"
    )
    val status: Status
)
