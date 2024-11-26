package com.challenge.library.repository

import com.challenge.library.model.StatisticalReport
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StatisticalReportRepository: MongoRepository<StatisticalReport, String>