package com.cenna.creditapplicationsystem.controller

import com.cenna.creditapplicationsystem.dto.CreditDto
import com.cenna.creditapplicationsystem.entity.Credit
import com.cenna.creditapplicationsystem.service.impl.CreditService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto): String{
       val credit: Credit =  this.creditService.save(creditDto.toEntity())
        return "Credito ${credit.creditCode} - Cliente ${credit.customer?.firstName} criado."
    }
}