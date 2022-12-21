package com.tjsse.jikespace.utils;

/**
 * @program: JiKeSpace_backend
 * @description: role for different user
 * @package_name: com.tjsse.jikespace.utils
 * @author: peng peng
 * @date: 2022/12/3
 **/
public enum Role {
    USER("user"),
    ADMIN("admin");

    private String role;
    Role(String role) {
        this.role = role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}