package com.cenna.creditapplicationsystem.service.impl

import com.cenna.creditapplicationsystem.entity.Customer
import com.cenna.creditapplicationsystem.repository.CustomerRepository
import com.cenna.creditapplicationsystem.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
): ICustomerService {
    override fun save(customer: Customer): Customer {
        return this.customerRepository.save(customer)
    }

    override fun findById(id: Long): Customer {
       return this.customerRepository.findById(id).orElseThrow{
           throw RuntimeException("ID $id não existe")
       }
    }

    override fun delete(id: Long) {
        return this.customerRepository.deleteById(id)
    }
}