package com.challenge.library.configuration.security


import com.challenge.library.service.LibraryAuthorizationService
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class CustomPermissionEvaluator(
    val libraryAuthorizationService: LibraryAuthorizationService
): PermissionEvaluator{
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        return false
    }

    override fun hasPermission(
        authentication: Authentication,
        targetId: Serializable?,
        targetType: String,
        permission: Any?
    ): Boolean {
        if (targetId !is String ||  permission !is String){
            return false
        }

        val principal = authentication.principal as UserAuthenticationPrincipal
        return libraryAuthorizationService.hasPermission(
            userId = principal.id,
            resourceId = targetId,
            resourceType = targetType,
            permission = permission
        )
    }
}