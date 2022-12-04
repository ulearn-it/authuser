package com.ncourses.authuser.test.builder;

import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.test.util.TestRandomUtils;

public class TestRoleEntityBuilder {

    public static TestRoleEntityBuilder builder() {
        return new TestRoleEntityBuilder();
    }

    private RoleEntity role;

    private TestRoleEntityBuilder() {
        role = TestRandomUtils.randomObject(RoleEntity.class);
    }

    public RoleEntity build() {
        return role;
    }

}
