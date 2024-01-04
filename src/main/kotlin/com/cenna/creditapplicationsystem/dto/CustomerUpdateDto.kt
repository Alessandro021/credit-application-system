package com.cenna.creditapplicationsystem.dto

import com.cenna.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "Input invalido.") val firstName: String,
    @field:NotEmpty(message = "Input invalido.") val lastName: String,
    @field:NotNull(message = "Input invalido.") val income: BigDecimal,
    @field:NotEmpty(message = "Input invalido.") val zipCode: String,
    @field:NotEmpty(message = "Input invalido.") val street: String
) {

    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income= this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street

        return customer
    }

}
