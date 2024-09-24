package com.challenge.library.controller

import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.model.Book
import com.challenge.library.service.BookManagementService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/book")
class BookManagementController(
    private val bookManagementService: BookManagementService,
) {

    @GetMapping()
    fun consultBook(
        @RequestParam search: String,
        @PageableDefault pagination: Pageable
    ): Page<Book>{
        val book = bookManagementService.consultBook(search, pagination)
        return book
    }

    @PostMapping
    fun registerBook(
        @RequestBody @Valid bookForm: BookRequestDTO,
        uriBuilder: UriComponentsBuilder
    ) : ResponseEntity<Book>{
        val registeredBook = bookManagementService.registerBook(bookForm)

        var uri = uriBuilder.path("/books").build().toUri()

        return ResponseEntity.created(uri).body(registeredBook)
    }

    @PutMapping
    fun editBook(
        @RequestBody @Valid bookForm: BookUpdateRequestDTO
    ): Book {
        return bookManagementService.editBook(bookForm)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(
        @PathVariable id: String
    ){
        bookManagementService.delete(id)
    }
}