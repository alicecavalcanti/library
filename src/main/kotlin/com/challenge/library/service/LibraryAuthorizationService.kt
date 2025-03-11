package com.challenge.library.service

import com.challenge.library.model.project.ResourcePermission
import com.challenge.library.model.project.ResourcePermissionRepository
import org.springframework.stereotype.Service

@Service
class LibraryAuthorizationService(
    val userService: UserService,
    val loanService: LoanService,
    val resourcePermissionRepository: ResourcePermissionRepository
){
    fun hasPermission(userId: String, resourceId: String, resourceType: String, permission: String): Boolean {
        when{
            resourceType == ResourcePermission.RESOURCE_LOAN
                    && listOf(ResourcePermission.ACTION_GAB_BOOK, ResourcePermission.ACTION_BOOK_RETURN).contains(
                permission
            ) -> {
                return isLoanRequester(userId, resourceId)
            }

            resourceType == ResourcePermission.RESOURCE_LOAN || resourceType == ResourcePermission.RESOURCE_BOOK
                    && listOf(
                ResourcePermission.ACTION_READ,
                ResourcePermission.ACTION_CREATE,
                ResourcePermission.ACTION_UPDATE,
                ResourcePermission.ACTION_DELETE,
                ResourcePermission.ACTION_APPROVE_LOAN,
                ResourcePermission.ACTION_APPROVE_RETURN
            ).contains(permission) -> {
                return hasRequiredRole(userId, resourceType, permission)
            }
        }
        return false
    }

    fun hasRequiredRole(userId: String, resourceType: String, permission: String): Boolean {
        val user = userService.findUserById(userId)
        val permissions: Set<ResourcePermission> =
            user.roles.flatMap {
                resourcePermissionRepository.findAllByProjectRole(it)
            }.toSet()
        return permissions.contains(ResourcePermission(resourceType, permission))
    }

    fun isLoanRequester(userId: String, resourceId: String): Boolean {
        val loan = loanService.findLoanById(resourceId)
        return loan.userId == userId
    }
}