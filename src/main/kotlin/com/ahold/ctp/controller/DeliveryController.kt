package com.ahold.ctp.controller

import com.ahold.ctp.dto.BusinessSummaryResponse
import com.ahold.ctp.dto.CreateDeliveryRequest
import com.ahold.ctp.dto.DeliveryResponse
import com.ahold.ctp.dto.UpdateDeliveryRequest
import com.ahold.ctp.service.DeliveryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@RequestMapping("/deliveries")
class DeliveryController(@Autowired private val deliveryService:DeliveryService) {

    //@PostMapping {creates the delivery}
    @PostMapping
    fun createDelivery(@Valid @RequestBody createDeliveryRequest: CreateDeliveryRequest)

         :ResponseEntity<DeliveryResponse>{
        return try {
            val deliveryResponse = deliveryService.createDelivery(
                createDeliveryRequest
            )
            ResponseEntity(deliveryResponse,HttpStatus.CREATED)
        } catch (e: IllegalArgumentException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    //@PatchMapping {Updates the certain fields}

    @PatchMapping("/{id}")
    fun updateDelivery(@PathVariable id: UUID,
                       @RequestBody @Valid request: UpdateDeliveryRequest)
            : ResponseEntity<DeliveryResponse>{
        println("REQUEST*** $request" );
        val updateDeliveryRes = deliveryService.updateDelivery(id, request);
        return ResponseEntity.ok(updateDeliveryRes)
    }

    //@PatchMapping { that does bulk updates}

    @PatchMapping("/bulk-update")
    fun updateMultipleDeliveries(@RequestBody @Valid request: List<UpdateDeliveryRequest>)
            :ResponseEntity<List<DeliveryResponse>>{
        val deliveriesResponse = deliveryService.updateDeliveries(request)
        return ResponseEntity.ok(deliveriesResponse)
    }

    //GetMapping {Calculates the business zummary}
//    @GetMapping("/business-summary")
//    fun getBusinessSummary(): ResponseEntity<BusinessSummaryResponse> {
//        val summary = deliveryService.getBusinessSummary()
//        return ResponseEntity.ok(summary)
//    }

}