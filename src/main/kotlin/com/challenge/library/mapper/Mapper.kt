package com.challenge.library.mapper

interface Mapper<T,U>{
    fun map(t:T):U
}