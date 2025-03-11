package com.challenge.library.model.project


data class ResourcePermission (val resourceType:String, val action: String){
    companion object{
        const val RESOURCE_BOOK = "book"
        const val RESOURCE_LOAN = "loan"

        const val ACTION_CREATE = "create"
        const val ACTION_DELETE = "delete"
        const val ACTION_UPDATE = "update"
        const val ACTION_READ = "read"

        const val ACTION_APPROVE_LOAN = "approve-loan"
        const val ACTION_APPROVE_RETURN = "approve-return"
        const val ACTION_GAB_BOOK = "gab-book"
        const val ACTION_BOOK_RETURN = "book-return"
    }
}