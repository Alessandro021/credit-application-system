package com.cenna.creditapplicationsystem.service

import com.cenna.creditapplicationsystem.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long): Customer
}