package com.ahold.ctp.service

import com.ahold.ctp.dto.CreateDeliveryRequest
import com.ahold.ctp.dto.DeliveryResponse
import com.ahold.ctp.model.Delivery
import com.ahold.ctp.repository.DeliveryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class DeliveryService(@Autowired private val deliveryRepository:DeliveryRepository) {

    //business logic to create a new delivery
    fun createDelivery(createDeliveryRequest: CreateDeliveryRequest): DeliveryResponse {
        return DeliveryResponse(
            id= UUID.randomUUID(),
            finishedAt = "",
            startedAt = "",
            vehicleId = "",
            status = ""


        )
    }

}