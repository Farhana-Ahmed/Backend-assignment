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
        val startedAtDateTime = OffsetDateTime.parse(createDeliveryRequest.startedAt);
        val delivery = Delivery(
            vehicleId = createDeliveryRequest.vehicleId,
            startedAt = startedAtDateTime,
            status = createDeliveryRequest.status.name
        );
        val savedDelivery = deliveryRepository.save(delivery);
        val formattedStartedAt = savedDelivery.startedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return DeliveryResponse(
            id = savedDelivery.id!!,
            vehicleId = savedDelivery.vehicleId,
            startedAt = formattedStartedAt,
            finishedAt = savedDelivery.finishedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            status = savedDelivery.status
        )
    }

}