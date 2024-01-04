package com.cenna.creditapplicationsystem.dto

import com.cenna.creditapplicationsystem.entity.Address
import com.cenna.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Input invalido.") val firstName: String,
    @field:NotEmpty(message = "Input invalido.")  val lastName: String,
    @field:NotEmpty(message = "Input invalido.") @field:CPF(message = "Cpf invalido.") val cpf: String,
    @field:NotNull(message = "Input invalido.") val income: BigDecimal,
    @field:NotEmpty(message = "Input invalido.") @field:Email(message = "Email invalido.") val email: String,
    @field:NotEmpty(message = "Input invalido.") val password: String,
    @field:NotEmpty(message = "Input invalido.") val zipCode: String,
    @field:NotEmpty(message = "Input invalido.") val street: String
){
    fun toEntity() : Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}
