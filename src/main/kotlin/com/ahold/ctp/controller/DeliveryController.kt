package com.ahold.ctp.controller

import com.ahold.ctp.dto.BusinessSummaryResponse
import com.ahold.ctp.dto.CreateDeliveryRequest
import com.ahold.ctp.dto.DeliveryResponse
import com.ahold.ctp.dto.UpdateDeliveryRequest
import com.ahold.ctp.model.Delivery
import com.ahold.ctp.model.Status
import com.ahold.ctp.service.DeliveryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.time.OffsetDateTime
import java.util.*

@RestController
@RequestMapping("/deliveries")
class DeliveryController(@Autowired private val deliveryService:DeliveryService) {

    //@PostMapping {creates the delivery}
    @Operation(
        summary = "Create a new delivery",
        description = "Creates a new delivery record with the specified vehicle ID, start time, and status.",
        responses = [
            ApiResponse(
                responseCode = "201", description = "Delivery created successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = DeliveryResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid request body")
        ]
    )
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
                       @RequestBody @Valid body: Map<String, Any>)
            : Delivery {
        val finishedAt = body["finishedAt"]?.let { OffsetDateTime.parse(it as String) }
        val status = Status.valueOf(body["status"] as String)
        return deliveryService.updateDelivery(id, finishedAt, status.name)
    }

    //@PatchMapping { that does bulk updates}
    @PatchMapping("/bulk-update")
    fun updateBulkDeliveries(@RequestBody @Valid updates: List<UpdateDeliveryRequest>): ResponseEntity<List<DeliveryResponse>> {
        val updatedDeliveries = deliveryService.bulkUpdateDeliveries(updates)
        return ResponseEntity.ok(updatedDeliveries)
    }

    //GetMapping {Calculates the business zummary}
    @GetMapping("/business-summary")
    fun getBusinessSummary(): ResponseEntity<BusinessSummaryResponse> {
        val summary = deliveryService.getBusinessSummary()
        return ResponseEntity.ok(summary)
    }
}