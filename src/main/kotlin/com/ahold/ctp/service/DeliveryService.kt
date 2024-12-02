package com.ahold.ctp.service

import com.ahold.ctp.dto.*
import com.ahold.ctp.model.Delivery
import com.ahold.ctp.model.Status
import com.ahold.ctp.repository.DeliveryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId
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

        );
        val savedDelivery = deliveryRepository.save(delivery);
        val formattedStartedAt = savedDelivery.startedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return DeliveryResponse(
            id = savedDelivery.id.toString(),
            vehicleId = savedDelivery.vehicleId,
            startedAt = formattedStartedAt,
            finishedAt = savedDelivery.finishedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            status = savedDelivery.status
        )
    }

    //business logic to update the fields
    fun updateDelivery(id: UUID?, finishedAt: OffsetDateTime?, status: String): Delivery {
        if (id == null) {
            throw IllegalArgumentException("ID must not be null")
        }
        val delivery = deliveryRepository.findById(id).orElseThrow { NoSuchElementException("Delivery not found") }

        if (status == Status.DELIVERED.name && finishedAt == null) {
            throw IllegalArgumentException("finishedAt is required for DELIVERED status")
        }

        delivery.finishedAt = finishedAt
        delivery.status = status
        return deliveryRepository.save(delivery)
    }

    fun bulkUpdateDeliveries(updates: List<UpdateDeliveryRequest>): List<DeliveryResponse> {
        return updates.map { updateRequest ->
            val delivery = deliveryRepository.findById(updateRequest.id)
                .orElseThrow { IllegalArgumentException("Delivery with id ${updateRequest.id} not found") }

            if (updateRequest.status == Status.DELIVERED && updateRequest.finishedAt == null) {
                throw IllegalArgumentException("finishedAt must be provided for DELIVERED status")
            }

            if (updateRequest.status == Status.IN_PROGRESS && updateRequest.finishedAt != null) {
                throw IllegalArgumentException("finishedAt must not be provided for IN_PROGRESS status")
            }

            delivery.status = updateRequest.status.name
            delivery.finishedAt = updateRequest.finishedAt?.let { OffsetDateTime.parse(it) }

            val updatedDelivery = deliveryRepository.save(delivery)

            DeliveryResponse(
                id = updatedDelivery.id.toString(),
                vehicleId = updatedDelivery.vehicleId,
                startedAt = updatedDelivery.startedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                finishedAt = updatedDelivery.finishedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                status = updatedDelivery.status
            )
        }
    }


    //calculates the business summary
    fun getBusinessSummary(): BusinessSummaryResponse {
        val amsterdamZone = ZoneId.of("Europe/Amsterdam")
        val nowInAmsterdam = OffsetDateTime.now(amsterdamZone)
        val yesterdayStart = nowInAmsterdam.minusDays(1).toLocalDate().atStartOfDay(amsterdamZone).toOffsetDateTime()
        val yesterdayEnd = yesterdayStart.plusDays(1)
        println("Time range: $yesterdayStart to $yesterdayEnd")

        val deliveries = deliveryRepository.findAllByStartedAtBetween(
            yesterdayStart,
            yesterdayEnd
        )
        val totalDeliveries = deliveries.size
        val averageMinutesBetweenDeliveryStart = if (totalDeliveries > 1) {
            val sortedStartTimes = deliveries.map { it.startedAt }.sorted()

            val totalMinutes = sortedStartTimes.zipWithNext { start, next ->
                Duration.between(start, next).toMinutes()
            }.sum()
            println("Total minutes $totalMinutes")

            totalMinutes / (totalDeliveries - 1)
        } else {
            0
        }
        return BusinessSummaryResponse(
            deliveries = totalDeliveries,
            averageMinutesBetweenDeliveryStart = averageMinutesBetweenDeliveryStart
        )
    }

}





