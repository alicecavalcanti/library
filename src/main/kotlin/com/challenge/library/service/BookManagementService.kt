package com.challenge.library.service

import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.mapper.BooksRequestMapper
import com.challenge.library.model.Book
import com.challenge.library.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookManagementService @Autowired constructor(
    private val bookRepository: BookRepository,
    private val booksRequestMapper: BooksRequestMapper
) {

    @Cacheable(cacheNames = ["book"])
    fun consultBook(
        search: String,
        pagination: Pageable
    ): Page<Book> {
        return bookRepository.findBook(search, pagination)
    }

    @CacheEvict(cacheNames = ["book"], allEntries = true)
    fun registerBook(
        bookRequestDTO: BookRequestDTO
    ): Book {
        val book = booksRequestMapper.map(bookRequestDTO)
        bookRepository.save(book)

        return book
    }

    @CacheEvict(cacheNames = ["book"], allEntries = true)
    fun editBook(
        bookRequestDTO: BookUpdateRequestDTO
    ): Book {
        val book = bookRepository.findById(bookRequestDTO.id).orElseThrow()
        book.updateWith(
            bookRequestDTO.titulo,
            bookRequestDTO.resumo,
            bookRequestDTO.autor,
            bookRequestDTO.ISBN,
            bookRequestDTO.categoria
        )
        bookRepository.save(book)
        return book
    }

    @CacheEvict(cacheNames = ["book"], allEntries = true)
    fun delete(
        id: String
    ) {
        bookRepository.deleteById(id)
    }
}