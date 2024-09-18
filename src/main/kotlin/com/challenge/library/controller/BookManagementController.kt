package com.challenge.library.controller

import com.challenge.library.controller.dto.BookFormDTO
import com.challenge.library.controller.dto.BookUpdateFormDTO
import com.challenge.library.model.Books
import com.challenge.library.service.BookManagementService
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/books")
class BookManagementController(
    private val bookManagementService: BookManagementService,
) {

    @GetMapping()
    fun consultBook(
        @RequestParam titulo:String,
        @PageableDefault pagination: Pageable

    ): Page<Books>{
        return bookManagementService.consultBook(titulo, pagination)
    }

    @GetMapping("/member")
    fun consultsBookUserMember(
        @RequestParam search: String,
        @PageableDefault pagination: Pageable
    ): Page<Books>{
        val book = bookManagementService.consultsBookUserMember(search, pagination)
        return book
    }

    @PostMapping
    fun registerBook(
        @RequestBody @Valid bookForm: BookFormDTO,
        uriBuilder: UriComponentsBuilder
    ) : ResponseEntity<Books>{
        val registeredBook = bookManagementService.registerBook(bookForm)

        var uri = uriBuilder.path("/books").build().toUri()

        return ResponseEntity.created(uri).body(registeredBook)
    }

    @PutMapping
    fun editBook(
        @RequestBody @Valid bookForm: BookUpdateFormDTO
    ): Books {
        return bookManagementService.editBook(bookForm)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(
        @PathVariable id: ObjectId
    ){
        bookManagementService.delete(id)
    }
}