package com.challenge.library.mapper

import com.challenge.library.controller.dto.LoanBookDTO
import com.challenge.library.model.LoanAndReturn
import com.challenge.library.model.LoanStatus
import org.springframework.stereotype.Component

@Component
class LoanRequestMapper : Mapper<LoanBookDTO, LoanAndReturn>{
    override fun map(t: LoanBookDTO): LoanAndReturn {
        return LoanAndReturn(
            idBook = t.idBook,
            idUser = t.idUser,
            status = LoanStatus.REQUESTED
        )
    }
}