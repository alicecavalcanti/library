package com.challenge.library.service

import com.challenge.library.controller.dto.*
import com.challenge.library.exception.BookNotFoundException
import com.challenge.library.mapper.BookRequestMapper
import com.challenge.library.model.Book
import com.challenge.library.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class BookService @Autowired constructor(
    private val bookRepository: BookRepository,
    private val bookRequestMapper: BookRequestMapper,
) {
    //@Cacheable(cacheNames = ["list_book"])
    fun listAllBooksLibrary(pagination: Pageable): Page<Book> {
        return bookRepository.findAll(pagination)
    }
    // @Cacheable(cacheNames = ["book"])
    fun consultBook(
        search: String,
        pagination: Pageable
    ): Page<Book> {
        return bookRepository.findBook(search, pagination)
    }

    fun findBookById(id: String): Book {
        return bookRepository.findById(id).orElseThrow { BookNotFoundException() }
    }

    @CacheEvict(cacheNames = ["book_register"], allEntries = true)
    fun registerBook(
        bookRequestDTO: BookRequestDTO,
    ): Book {
        val book = bookRequestMapper.map(bookRequestDTO)
        return bookRepository.save(book)
    }

    @CacheEvict(cacheNames = ["book_edit"], allEntries = true)
    fun editBook(
        bookRequestDTO: BookUpdateRequestDTO,
    ): Book {
        val book = findBookById(bookRequestDTO.id)
        book.updateWith(
            bookRequestDTO.titulo,
            bookRequestDTO.resumo,
            bookRequestDTO.autor,
            bookRequestDTO.isbn,
            bookRequestDTO.categoria
        )
        bookRepository.save(book)
        return book
    }

    @CacheEvict(cacheNames = ["book_delete"], allEntries = true)
    fun delete(
        id: String,
    ) {
        findBookById(id)
        bookRepository.deleteById(id)
    }

    fun addBookReview(
        bookReviewDTO: BookReviewDTO
    ): Book {
        val book = findBookById(bookReviewDTO.idBook)
        book.resenhas.add(bookReviewDTO.review)
        return bookRepository.save(book)
    }

    fun bestBookNotes(): List<AverageBookGradesDTO> {
        return bookRepository.findBestBookNotes()
    }
}