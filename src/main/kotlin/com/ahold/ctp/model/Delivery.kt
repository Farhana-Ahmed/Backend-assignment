package com.ahold.ctp.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
data class Delivery(

    //"id": "69201507-0ae4-4c56-ac2d-75fbe27efad8",
    //  "vehicleId": "AHV-589"
    //   "startedAt": "2023-10-09T12:45:34.678Z",
    //    "finishedAt": null,
    //    "status": "IN_PROGRESS"

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? =null,

    @Column(nullable = false)
    val vehicleId:String = "",

    @Column(nullable = false)
    val startedAt: OffsetDateTime,

    @Column
    var finishedAt: OffsetDateTime?=null,

    @Column(nullable = false)
    var status:String = "",
    )
