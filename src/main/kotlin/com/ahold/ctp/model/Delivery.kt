package com.ahold.ctp.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
data class Delivery(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(nullable = false)
    val vehicleId:String ,

    @Column(nullable = false)
    val startedAt: OffsetDateTime,

    @Column
    var finishedAt: OffsetDateTime?=null,

    @Column(nullable = false)
    var status:String ,
    )
{
    constructor() : this(
        id = null,
        vehicleId = "",
        startedAt = OffsetDateTime.now(),
        finishedAt = null,
        status = ""
    )
}