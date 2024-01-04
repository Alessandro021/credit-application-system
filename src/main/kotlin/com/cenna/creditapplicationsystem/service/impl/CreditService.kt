package com.cenna.creditapplicationsystem.service.impl

import com.cenna.creditapplicationsystem.entity.Credit
import com.cenna.creditapplicationsystem.exception.BusinessException
import com.cenna.creditapplicationsystem.repository.CreditRepository
import com.cenna.creditapplicationsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
       return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
       return this.creditRepository.findAllByCustomerID(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = (this.creditRepository.findByCreditCode(creditCode) ?: throw BusinessException("CreditCode $creditCode não existe."))

        return if(credit.customer?.id == customerId){
            credit
        } else {
            throw IllegalArgumentException("Contate o adimistrador.")
        }
    }
}