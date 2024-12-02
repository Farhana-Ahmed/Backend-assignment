package com.ahold.ctp

import com.ahold.ctp.controller.DeliveryController
import com.ahold.ctp.dto.CreateDeliveryRequest
import com.ahold.ctp.dto.DeliveryResponse
import com.ahold.ctp.model.Status
import com.ahold.ctp.service.DeliveryService
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
@AutoConfigureMockMvc
class CtpBackendAssignmentSandboxApplicationTests {

	@Test
	fun contextLoads() {
	}
	@MockBean
	lateinit var deliveryService: DeliveryService

	@Mock
	lateinit var mockMvc: MockMvc

	@Test
	fun `createDelivery should return status 201 when request is valid`() {
		val mockRequestJson = """
        {
            "vehicleId": "AHV-589",
            "startedAt": "2023-10-09T12:45:34.678Z",
            "status": "IN_PROGRESS"
        }
    """.trimIndent()

		val expectedResponse = DeliveryResponse(
			id = "69201507-0ae4-4c56-ac2d-75fbe27efad8",
			vehicleId = "AHV-589",
			startedAt = "2023-10-09T12:45:34.678Z",
			finishedAt = null,
			status = "IN_PROGRESS"
		)
		Mockito.`when`(deliveryService.createDelivery(Mockito.any(CreateDeliveryRequest::class.java)))
			.thenReturn(expectedResponse)
		mockMvc.perform(
			post("/deliveries")
				.contentType("application/json")
				.content(mockRequestJson)
		)
			.andExpect(MockMvcResultMatchers.status().isCreated)
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id))
	}
}
