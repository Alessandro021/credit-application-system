package com.cenna.creditapplicationsystem.controller

import com.cenna.creditapplicationsystem.dto.CustomerDto
import com.cenna.creditapplicationsystem.dto.CustomerUpdateDto
import com.cenna.creditapplicationsystem.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private  lateinit var mockMvc: MockMvc
    @Autowired private  lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
    }

    @BeforeEach fun setup() = customerRepository.deleteAll()
    @AfterEach fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return 201 status`() {
        //given
        val customerDto: CustomerDto = buildCustomerDto()
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Alex"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva Santos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("alex@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("123456789"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua A"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should not save a customer with same CPF and return 409 status`() {
        //given
        customerRepository.save(buildCustomerDto().toEntity())
        val customerDto: CustomerDto = buildCustomerDto()
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("conflict: consulte a documentação."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with firstName empty and return 400 status`() {
        //given
        val customerDto: CustomerDto = buildCustomerDto(firstName = "")
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("bad request: consulte a documentação."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id and return 200 status`() {
        //given
        val customer =  customerRepository.save(buildCustomerDto().toEntity())

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Alex"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva Santos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("alex@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("123456789"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua A"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer whith invalid id and return 400 status`() {
        //given
        val invalidId = 99L

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$invalidId").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("bad request: consulte a documentação."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception").value("class com.cenna.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete customer by id  204 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should delete customer by id and return 400 status`() {
        //given
        val invalidId = 99L
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$invalidId").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("bad request: consulte a documentação."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception").value("class com.cenna.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should update a customer by id and return 200 status`(){
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}").contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("AlexUpdate"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva Santos Update"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("123456700"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua B"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should not update a customer whith invalid id and return 400 status`() {
        //given
        val invalidId = 99L
        val customerUpdateDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //when & then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=$invalidId").contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("bad request: consulte a documentação."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception").value("class com.cenna.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    fun buildCustomerDto(
        firstName: String = "Alex",
        lastName: String = "Silva Santos",
        cpf: String = "28475934625",
        email: String = "alex@email.com",
        password: String = "1234",
        zipCode: String = "123456789",
        street: String = "Rua A",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        street = street,
        zipCode = zipCode,
        income = income,
    )

    fun buildCustomerUpdateDto(
        firstName: String = "AlexUpdate",
        lastName: String = "Silva Santos Update",
        zipCode: String = "123456700",
        street: String = "Rua B",
        income: BigDecimal = BigDecimal.valueOf(2000.0),
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        street = street,
        zipCode = zipCode,
        income = income,
    )


}