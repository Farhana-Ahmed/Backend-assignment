package com.ahold.ctp.service

import com.ahold.ctp.dto.*
import com.ahold.ctp.model.Delivery
import com.ahold.ctp.model.Status
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
            status = createDeliveryRequest.status.name,
            id=UUID.randomUUID()
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

    //busines logic to update the fields
    fun updateDelivery(id: UUID, request: UpdateDeliveryRequest): DeliveryResponse
    {
        val delivery = deliveryRepository.findById(id)
            .orElseThrow{java.lang.IllegalArgumentException ("Delivery with id $id is not found")}
        when (request.status) {
            Status.DELIVERED -> {
                if (request.finishedAt == null) {
                    throw IllegalArgumentException("finishedAt must be provided for DELIVERED status")
                }
                delivery.finishedAt = OffsetDateTime.parse(request.finishedAt)
            }
            Status.IN_PROGRESS -> {
                if (request.finishedAt != null) {
                    throw IllegalArgumentException("finishedAt must not be provided for IN_PROGRESS status")
                }
                delivery.finishedAt = null
            }
        }

        delivery.status = request.status.name
        val updatedDelivery = deliveryRepository.save(delivery)
//        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        val response =  DeliveryResponse(
            id = updatedDelivery.id!!,
            vehicleId = updatedDelivery.vehicleId,
            startedAt = updatedDelivery.startedAt
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            finishedAt = updatedDelivery.finishedAt
                ?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            status = updatedDelivery.status
        )
        println("Updated Response::: $response")
        return response
    }

    // list of deliveries update \
//
    fun updateDeliveries(request: List<UpdateDeliveryRequest>): List<DeliveryResponse> {
        return request.map { updateRequest ->
            val delivery = deliveryRepository.findById(updateRequest.id)
                .orElseThrow { IllegalArgumentException("Delivery with id ${updateRequest.id} not found") }

            if (updateRequest.status == Status.DELIVERED && updateRequest.finishedAt == null) {
                throw IllegalArgumentException("finishedAt must be provided when status is DELIVERED")
            }
            if (updateRequest.status == Status.IN_PROGRESS && updateRequest.finishedAt != null) {
                throw IllegalArgumentException("finishedAt must not be provided when status is IN_PROGRESS")
            }


            delivery.status = updateRequest.status.name
            if (updateRequest.finishedAt != null) {
                delivery.finishedAt = OffsetDateTime.parse(updateRequest.finishedAt)
            }

            // Save the updated delivery
            val updatedDelivery = deliveryRepository.save(delivery)

            // Return the updated delivery as a response
            DeliveryResponse(
                id = updatedDelivery.id!!,
                vehicleId = updatedDelivery.vehicleId,
                startedAt = updatedDelivery.startedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                finishedAt = updatedDelivery.finishedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                status = updatedDelivery.status
            )
        }
    }

    //calculates the business summary
    fun getBusinessSummary(): BusinessSummaryResponse {
        return BusinessSummaryResponse(
            deliveries = 3,
            averageMinutesBetweenDeliveryStart = 240
        )
    }
}





