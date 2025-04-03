package com.challenge.library.repository

import com.challenge.library.model.Roles
import com.challenge.library.model.project.ResourcePermission
import com.challenge.library.model.project.ResourcePermission.Companion.ACTION_APPROVE_LOAN
import com.challenge.library.model.project.ResourcePermission.Companion.ACTION_APPROVE_RETURN
import com.challenge.library.model.project.ResourcePermission.Companion.ACTION_CREATE
import com.challenge.library.model.project.ResourcePermission.Companion.ACTION_DELETE
import com.challenge.library.model.project.ResourcePermission.Companion.ACTION_READ
import com.challenge.library.model.project.ResourcePermission.Companion.ACTION_UPDATE
import com.challenge.library.model.project.ResourcePermission.Companion.RESOURCE_BOOK
import com.challenge.library.model.project.ResourcePermission.Companion.RESOURCE_LOAN
import com.challenge.library.model.project.ResourcePermissionRepository
import org.springframework.stereotype.Repository

@Repository
class ResourcePermissionRepositoryInMemory: ResourcePermissionRepository {
    override fun findAllByProjectRole(projectRole: Roles): Set<ResourcePermission> {
        return resourcePermissionMap[projectRole] ?: setOf()
    }

    companion object {
        private val resourcePermissionMap = mapOf(
            Roles.ADMIN to setOf(
                ResourcePermission(RESOURCE_BOOK, ACTION_CREATE),
                ResourcePermission(RESOURCE_BOOK, ACTION_UPDATE),
                ResourcePermission(RESOURCE_BOOK, ACTION_DELETE),
                ResourcePermission(RESOURCE_LOAN, ACTION_READ),
                ResourcePermission(RESOURCE_LOAN, ACTION_APPROVE_LOAN),
                ResourcePermission(RESOURCE_LOAN, ACTION_APPROVE_RETURN),
                ResourcePermission(RESOURCE_LOAN, ACTION_CREATE)
            ),
            Roles.LIBRARY to setOf(
                ResourcePermission(RESOURCE_BOOK, ACTION_DELETE),
                ResourcePermission(RESOURCE_BOOK, ACTION_UPDATE),
                ResourcePermission(RESOURCE_BOOK, ACTION_CREATE),
                ResourcePermission(RESOURCE_LOAN, ACTION_READ),
                ResourcePermission(RESOURCE_LOAN, ACTION_APPROVE_LOAN),
                ResourcePermission(RESOURCE_LOAN, ACTION_APPROVE_RETURN),
                ResourcePermission(RESOURCE_LOAN, ACTION_CREATE)
            )
        )
    }
}