package com.challenge.library.service

import com.challenge.library.controller.dto.BookRequestDTO
import com.challenge.library.controller.dto.BookUpdateRequestDTO
import com.challenge.library.exception.UserNotAllowedException
import com.challenge.library.mapper.BooksRequestMapper
import com.challenge.library.model.Book
import com.challenge.library.model.Token
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
    private val booksRequestMapper: BooksRequestMapper,
    private val tokenValue: Token,
    private val userNotAllowedException: UserNotAllowedException
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
    ): Book {
        if(token.equals(tokenValue.TOKEN_MEMBER)){
            throw userNotAllowedException
        }
        val book = booksRequestMapper.map(bookRequestDTO)
        bookRepository.save(book)
        return book
    }

    @CacheEvict(cacheNames = ["book_edit"], allEntries = true)
    fun editBook(
        bookRequestDTO: BookUpdateRequestDTO,
        token: String
    ): Book{
        if(token.equals(tokenValue.TOKEN_MEMBER)){
          throw userNotAllowedException
        }
        val book = bookRepository.findById(bookRequestDTO.id).orElseThrow()
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
        token: String
    ){
        if (token.equals(tokenValue.TOKEN_MEMBER)){
           throw userNotAllowedException
        }
        bookRepository.deleteById(id)

    }


}