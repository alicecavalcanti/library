package com.challenge.library.controller

import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.controller.dto.UserRequestDTO
import com.challenge.library.model.Book
import com.challenge.library.repository.BookRepository
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
    @GetMapping("/catalog")
    fun catalogoLivros(
        @PageableDefault pagination: Pageable
    ):Page<Book>{
        return bookManagementService.listAll(pagination)
    }

    @GetMapping("/search")
    fun consultBook(
        @RequestParam search: String,
        @PageableDefault pagination: Pageable
    ): Page<Book>{
        val book = bookManagementService.consultBook(search, pagination)
        return book
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CONFLICT)
    fun registerBook(
        @RequestBody @Valid bookForm: BookRequestDTO,
        @RequestHeader token: String,
        uriBuilder: UriComponentsBuilder,
    ) : ResponseEntity<Book>{

        val registeredBook = bookManagementService.registerBook(bookForm, token)

        var uri = uriBuilder.path("/books").build().toUri()

        return ResponseEntity.created(uri).body(registeredBook)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CONFLICT)
    fun editBook(
        @RequestBody @Valid bookRequestDTO: BookUpdateRequestDTO,
        @RequestHeader token: String
    ): Book{
        return bookManagementService.editBook(bookRequestDTO, token)

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(
        @PathVariable id: String,
        @RequestHeader token: String
    ){
        return bookManagementService.delete(id, token)
    }
}