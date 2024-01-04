package com.cenna.creditapplicationsystem.dto

import com.cenna.creditapplicationsystem.entity.Credit
import com.cenna.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Input invalido.") val creditValue: BigDecimal,
    @field:Future(message = "Input invalido.") val dayFirstOfInstallment: LocalDate,
    @field:Positive(message = "Input invalido.")val numberOfInstallment: Int,
    @field:NotNull(message = "Input invalido.") val customerId: Long
) {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallment,
        customer = Customer(id = this.customerId)
        )

}
