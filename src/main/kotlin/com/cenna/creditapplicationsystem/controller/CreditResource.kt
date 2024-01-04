package com.cenna.creditapplicationsystem.controller

import com.cenna.creditapplicationsystem.dto.CreditDto
import com.cenna.creditapplicationsystem.dto.CreditListView
import com.cenna.creditapplicationsystem.dto.CreditView
import com.cenna.creditapplicationsystem.entity.Credit
import com.cenna.creditapplicationsystem.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto): ResponseEntity<String>{
       val credit: Credit =  this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Credito ${credit.creditCode} - Cliente ${credit.customer?.firstName} criado.")
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditListView>> {
        val creditViewList: List<CreditListView> =  this.creditService.findAllByCustomer(customerId).stream().map { credit: Credit -> CreditListView(credit) }.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long, @PathVariable creditCode: UUID ): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)

        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }

}