package com.ahold.ctp.controller

import com.ahold.ctp.dto.CreateDeliveryRequest
import com.ahold.ctp.dto.DeliveryResponse
import com.ahold.ctp.service.DeliveryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/deliveries")
class DeliveryController(@Autowired private val deliveryService:DeliveryService) {

    //@PostMapping {creates the delivery}
    @PostMapping
    fun createDelivery(@RequestBody createDeliveryRequest: CreateDeliveryRequest)

         :ResponseEntity<DeliveryResponse>{
         val deliveryResponse = deliveryService.createDelivery(
            createDeliveryRequest
        )
       return  ResponseEntity(deliveryResponse, HttpStatus.CREATED)
    }


    //@PatchMapping {Updates the certain fields}

    //@PatchMapping { that does bulk updates}

    //GetMapping {Calculates the business zummary}
}