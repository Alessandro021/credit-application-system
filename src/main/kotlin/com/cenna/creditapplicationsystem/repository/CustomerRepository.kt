package com.cenna.creditapplicationsystem.repository

import com.cenna.creditapplicationsystem.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {
}