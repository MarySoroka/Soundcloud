package com.soundcloud.role;


import java.util.Objects;

public class Role {
    private RoleType name;
    private Long roleId;
    private Long userId;

    public Role() {
    }

    public Role(RoleType name, Long id, Long userId) {
        this.name = name;
        this.roleId = id;
        this.userId = userId;
    }

    public Role(Role role) {
        this.name = role.getName();
        this.roleId = role.getRoleId();
        this.userId = role.getUserId();

    }

    public Role(RoleType unsignedUser) {
        this.name = unsignedUser;
    }


    public Role(Long userId, Long roleId) {
        this.userId = userId;
        RoleType roleType = RoleType.of(roleId);
        this.name = roleType;
        this.roleId = roleType.getRoleTypeId();
    }



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name == role.name &&
                Objects.equals(roleId, role.roleId) &&
                Objects.equals(userId, role.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, roleId, userId);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
