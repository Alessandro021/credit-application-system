package com.cenna.creditapplicationsystem.dto

import com.cenna.creditapplicationsystem.entity.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditListView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int
) {
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments
    )
}
