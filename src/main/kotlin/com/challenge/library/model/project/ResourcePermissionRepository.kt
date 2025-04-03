package com.challenge.library.model.project

import com.challenge.library.model.Roles

interface ResourcePermissionRepository {
    fun findAllByProjectRole(projectRole: Roles): Set<ResourcePermission>
}