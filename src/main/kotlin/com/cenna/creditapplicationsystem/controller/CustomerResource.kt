package com.cenna.creditapplicationsystem.controller

import com.cenna.creditapplicationsystem.dto.CustomerDto
import com.cenna.creditapplicationsystem.dto.CustomerUpdateDto
import com.cenna.creditapplicationsystem.dto.CustomerView
import com.cenna.creditapplicationsystem.entity.Customer
import com.cenna.creditapplicationsystem.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerResource(
    private val customerService: CustomerService
) {
    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<String> {
        val saveCustomer = this.customerService.save(customerDto.toEntity())

        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente ${saveCustomer.email} salvo com sucesso.")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)

        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(customer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long) {
        return this.customerService.delete(id)
    }

    @PatchMapping
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long,
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        val customerToUpdate: Customer = customerUpdateDto.toEntity(customer)
        val customerUpdate: Customer = this.customerService.save(customerToUpdate)

        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(customerUpdate))
    }
}