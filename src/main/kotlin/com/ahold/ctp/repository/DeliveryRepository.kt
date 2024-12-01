package com.ahold.ctp.repository

import com.ahold.ctp.model.Delivery
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime
import java.util.*

interface DeliveryRepository : JpaRepository<Delivery, UUID>{
    fun findAllByStartedAtBetween(start: OffsetDateTime, end: OffsetDateTime): List<Delivery>

}