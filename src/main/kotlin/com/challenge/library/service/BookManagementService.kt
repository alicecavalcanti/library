package com.challenge.library.service

import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.controller.dto.UserRequestDTO
import com.challenge.library.mapper.BooksRequestMapper
import com.challenge.library.model.Book
import com.challenge.library.model.Roles
import com.challenge.library.model.Token
import com.challenge.library.repository.BookRepository
import com.challenge.library.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class BookManagementService @Autowired constructor(
    private val bookRepository: BookRepository,
    private val booksRequestMapper: BooksRequestMapper,
    private val tokenValue: Token
) {
    @Cacheable(cacheNames = ["list_book"])
    fun listAll(pagination: Pageable): Page<Book> {
        return bookRepository.findAll(pagination)
    }
    @Cacheable(cacheNames = ["book"])
    fun consultBook(
        search: String,
        pagination: Pageable
    ): Page<Book> {
        return bookRepository.findBook(search, pagination)
    }

    @CacheEvict(cacheNames = ["book_register"], allEntries = true)
    fun registerBook(
        bookRequestDTO: BookRequestDTO,
        token: String
    ): Boolean {
        val book = booksRequestMapper.map(bookRequestDTO)
        if(token.equals(tokenValue.TOKEN_ADMIN) || token.equals(tokenValue.TOKEN_LIBRARY)){
            bookRepository.save(book)
            return true
        }
        return false
    }

    @CacheEvict(cacheNames = ["book_edit"], allEntries = true)
    fun editBook(
        bookRequestDTO: BookUpdateRequestDTO,
        token: String
    ): Boolean {
        val book = bookRepository.findById(bookRequestDTO.id).orElseThrow()
        if(token.equals(tokenValue.TOKEN_ADMIN) || token.equals(tokenValue.TOKEN_LIBRARY)){
            book.updateWith(
                bookRequestDTO.titulo,
                bookRequestDTO.resumo,
                bookRequestDTO.autor,
                bookRequestDTO.ISBN,
                bookRequestDTO.categoria
            )

            bookRepository.save(book)
            return true
        }
        return false
    }

    @CacheEvict(cacheNames = ["book_delete"], allEntries = true)
    fun delete(
        id: String,
        token: String
    ): Boolean{
        if (token.equals(tokenValue.TOKEN_ADMIN) || token.equals(tokenValue.TOKEN_LIBRARY) ){
           bookRepository.deleteById(id)
            return true
        }
        return false
    }


}