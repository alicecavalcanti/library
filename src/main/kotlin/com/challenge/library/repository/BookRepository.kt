package com.challenge.library.repository

import com.challenge.library.controller.dto.AverageBookGradesDTO
import com.challenge.library.controller.dto.LoanAmountCategoryDTO
import com.challenge.library.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BookRepository : MongoRepository<Book, String>{

    @Query("{'\$or': [ {'titulo': {'\$regex': ?0, '\$options': 'i'}}, {'autor': {'\$regex': ?0, '\$options': 'i'}}, {'isbn': ?0}, {'categoria': ?0} ] }")
    fun findBook(search: String, pagination: Pageable): Page<Book>


    @Aggregation(
    "{\$unwind: '\$notas' }",
    "{\$group: { _id: '\$_id', gradeAverage:{\$avg: '\$notas'}}}",
    "{\$sort: {gradeAverage:-1}}",
    "{\$limit: 10}",
    "{\$project: {idBook: '\$_id', gradeAverage: '\$gradeAverage', _id:0 }}"
    )
    fun findBestBookNotes():List<AverageBookGradesDTO>


}