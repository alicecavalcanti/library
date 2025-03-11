package com.challenge.library.controller

import com.challenge.library.controller.dto.BookFeedbackDTO
import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.model.Book
import com.challenge.library.service.BookService
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
class BookController(
    private val bookService: BookService,
) {
    @GetMapping("/catalog")
    fun listAllBooksLibrary(
        @PageableDefault pagination: Pageable
    ):Page<Book>{
        return bookService.listAllBooksLibrary(pagination)
    }

    @GetMapping()
    fun consultBook(
        @RequestParam search: String,
        @PageableDefault pagination: Pageable
    ): Page<Book>{
        val book = bookService.consultBook(search, pagination)
        return book
    }

    @PostMapping
    fun registerBook(
        @RequestBody @Valid bookForm: BookRequestDTO,
        uriBuilder: UriComponentsBuilder,
    ) : ResponseEntity<Book>{
        val registeredBook = bookService.registerBook(bookForm)
        var uri = uriBuilder.path("/books").build().toUri()
        return ResponseEntity.created(uri).body(registeredBook)
    }

    @PutMapping
    fun editBook(
        @RequestBody @Valid bookRequestDTO: BookUpdateRequestDTO,
    ): Book{
        return bookService.editBook(bookRequestDTO)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteBook(
        @PathVariable id: String,
    ){
        bookService.delete(id)
    }

    @PostMapping("/feedback-book")
    fun feedbackBookUser(@RequestBody @Valid bookFeedbackDTO: BookFeedbackDTO): Book {
        return bookService.feedbackBookUser(bookFeedbackDTO)
    }

}