package com.finance.backend.dto;

import com.finance.backend.entity.Role;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class UpdateRolesRequest {

    @NotEmpty(message = "Roles cannot be empty")
    private Set<Role> roles;

    public UpdateRolesRequest() {
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
