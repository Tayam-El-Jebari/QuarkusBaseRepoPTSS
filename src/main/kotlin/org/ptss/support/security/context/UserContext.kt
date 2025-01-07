package org.ptss.support.security.context

import org.ptss.support.domain.enums.Role
import java.util.UUID

data class UserContext(
    val userId: UUID,
    val groupId: UUID?, // Could be empty for Role.ADMIN and Role.HCP
    val roles: Set<Role>,
    val hasPin: Boolean
)