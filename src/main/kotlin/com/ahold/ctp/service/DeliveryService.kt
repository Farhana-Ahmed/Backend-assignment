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
    // list of deliveries update
    fun bulkUpdateDeliveries(updates: List<Delivery>): List<Delivery> {
        return updates.map { updateDelivery(it.id, it.finishedAt, it.status) }
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





