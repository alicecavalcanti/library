package com.challenge.library.controller.dto

import com.challenge.library.model.QuantityEachTypeUsers

data class UserStatisticsDTO (
    var totalNumberQuantityEachTypeUsers: QuantityEachTypeUsers,
    val newUsersLastMonth:UserGrowthDTO
)
