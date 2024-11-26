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
    @GetMapping("/catalogo")
    fun catalogoLivros(
        @PageableDefault pagination: Pageable
    ):Page<Book>{
        return bookManagementService.listAll(pagination)
    }

    @GetMapping
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
        @RequestHeader token: String,
        uriBuilder: UriComponentsBuilder,
    ) : ResponseEntity<String>{

        val registeredBookTrueOrFalse = bookManagementService.registerBook(bookForm, token)

        if (registeredBookTrueOrFalse){
            return ResponseEntity.status(401).body("")
        }

        var uri = uriBuilder.path("/books").build().toUri()

        return ResponseEntity.created(uri).body("")
    }

    @PutMapping
    fun editBook(
        @RequestBody @Valid bookRequestDTO: BookUpdateRequestDTO,
        @RequestHeader token: String
    ): ResponseEntity<String> {
        val editYesOrNot= bookManagementService.editBook(bookRequestDTO, token)
        if(editYesOrNot){
           return ResponseEntity.status(200).body("")
        }
        return ResponseEntity.status(401).body("")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(
        @PathVariable id: String,
        @RequestHeader token: String
    ): ResponseEntity<String>{
       val deleteTrueOrFalse= bookManagementService.delete(id, token)
       if(deleteTrueOrFalse){
           return ResponseEntity.status(204).body("")
       }
        return ResponseEntity.status(401).body("")
    }
}