package com.cenna.creditapplicationsystem.controller

import com.cenna.creditapplicationsystem.dto.CustomerDto
import com.cenna.creditapplicationsystem.dto.CustomerUpdateDto
import com.cenna.creditapplicationsystem.dto.CustomerView
import com.cenna.creditapplicationsystem.entity.Customer
import com.cenna.creditapplicationsystem.service.impl.CustomerService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
class CustomerResourse(
    private val customerService: CustomerService
) {
    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDto): String {
       val saveCustomer =  this.customerService.save(customerDto.toEntity())

        return "Cliente ${saveCustomer.email} salvo com sucesso."
    }

    @GetMapping("/id")
    fun findById(@PathVariable id: Long): CustomerView {
        val customer: Customer = this.customerService.findById(id)

        return CustomerView(customer)
    }

    @DeleteMapping("/id")
    fun deleteCustomer(@PathVariable id: Long){
        return this.customerService.delete(id)
    }

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") id: Long, @RequestBody customerUpdateDto: CustomerUpdateDto): CustomerView {
       val customer: Customer =  this.customerService.findById(id)
       val customerToUpdate: Customer =  customerUpdateDto.toEntity(customer)
       val customerUpdate: Customer = this.customerService.save(customerToUpdate)

       return CustomerView(customerUpdate)
    }
}