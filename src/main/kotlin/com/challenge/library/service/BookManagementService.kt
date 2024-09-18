package com.challenge.library.service

import com.challenge.library.controller.dto.BookFormDTO
import com.challenge.library.controller.dto.BookUpdateFormDTO
import com.challenge.library.mapper.BooksFormMapper
import com.challenge.library.model.Books
import com.challenge.library.repository.BookRepository
import org.bson.types.ObjectId
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookManagementService(
    private val bookRepository: BookRepository,
    private val booksFormMapper: BooksFormMapper
){

    @Cacheable(cacheNames = ["browseBooksByTitle"])
    fun consultBook(
        titulo: String,
        pagination: Pageable
    ): Page<Books>{
        val searchEdit = titulo.lowercase()
        return bookRepository.findAllByTitulo(searchEdit, pagination)
    }

    @Cacheable(cacheNames = ["Books"])
    fun consultsBookUserMember(
        search: String,
        pagination: Pageable
    ): Page<Books> {
        val searchEdit = search.lowercase()
        if(!bookRepository.findAllByTitulo(searchEdit, pagination).isEmpty()){
            return bookRepository.findAllByTitulo(searchEdit,pagination)
        }else if (!bookRepository.findAllByAutor(searchEdit,pagination).isEmpty()){
            return bookRepository.findAllByAutor(searchEdit, pagination)
        }else if(!bookRepository.findAllByCategoria(searchEdit, pagination).isEmpty()){
            return bookRepository.findAllByCategoria(searchEdit, pagination)
        }else{
            return bookRepository.findAllByISBN(searchEdit, pagination)
        }
    }

    @CacheEvict(cacheNames = ["browseBooksByTitle", "Books"], allEntries = true)
    fun registerBook(
        bookForm: BookFormDTO
    ): Books{
        val book = booksFormMapper.map(bookForm)
        bookRepository.save(book)

        return book
    }

    @CacheEvict(cacheNames = ["browseBooksByTitle", "Books"], allEntries = true)
    fun editBook(
        bookForm: BookUpdateFormDTO
    ): Books{
        var updatedBook = bookRepository.findById(bookForm.id).orElseThrow()
        updatedBook.titulo = bookForm.titulo
        updatedBook.resumo = bookForm.resumo
        updatedBook.autor = bookForm.autor
        updatedBook.ISBN = bookForm.ISBN
        updatedBook.categoria = bookForm.categoria

        bookRepository.save(updatedBook)

        return updatedBook
    }

    @CacheEvict(cacheNames = ["browseBooksByTitle", "Books"], allEntries = true)
    fun delete(
        id: ObjectId
    ){
        bookRepository.deleteById(id)
    }
}