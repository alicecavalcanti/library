package com.challenge.library.controller

import com.challenge.library.configuration.security.UserAuthenticationPrincipal
import com.challenge.library.controller.dto.BookRatingDTO
import com.challenge.library.controller.dto.BookReviewDTO
import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.model.Book
import com.challenge.library.model.project.ResourcePermission
import com.challenge.library.service.BookService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
        return bookService.consultBook(search, pagination)
    }

    @PostMapping
    @PreAuthorize("hasPermission(#user.id, '${ResourcePermission.RESOURCE_BOOK}', '${ResourcePermission.ACTION_CREATE}')")
    fun registerBook(
        @RequestBody @Valid bookForm: BookRequestDTO,
        @AuthenticationPrincipal user : UserAuthenticationPrincipal,
        uriBuilder: UriComponentsBuilder,
    ) : ResponseEntity<Book>{
        val registeredBook = bookService.registerBook(bookForm)
        var uri = uriBuilder.path("/books").build().toUri()
        return ResponseEntity.created(uri).body(registeredBook)
    }

    @PutMapping
    @PreAuthorize("hasPermission(#user.id, '${ResourcePermission.RESOURCE_BOOK}', '${ResourcePermission.ACTION_UPDATE}')")
    fun editBook(
        @RequestBody @Valid bookRequestDTO: BookUpdateRequestDTO,
        @AuthenticationPrincipal user : UserAuthenticationPrincipal
    ): Book{
        return bookService.editBook(bookRequestDTO)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#user.id, '${ResourcePermission.RESOURCE_BOOK}', '${ResourcePermission.ACTION_DELETE}')")
    @DeleteMapping("/{id}")
    fun deleteBook(
        @PathVariable id: String,
        @AuthenticationPrincipal user : UserAuthenticationPrincipal
    ){
        bookService.delete(id)
    }

    @PostMapping("/review")
    fun submitReview(
        @RequestBody @Valid bookReviewDTO: BookReviewDTO
    ): Book {
        return bookService.addBookReview(bookReviewDTO)
    }

    @PostMapping("/rating")
    fun submitRating(
        @RequestBody @Valid bookRatingDTO: BookRatingDTO
    ): Book{
        return bookService.addBookRating(bookRatingDTO)
    }
}