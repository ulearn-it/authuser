package com.ncourses.authuser.test.builder;

import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.model.UserEntity;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUserEntityBuilder {

    public static TestUserEntityBuilder builder() {
        return new TestUserEntityBuilder();
    }

    public static ListBuilder listBuilder() {
        return new ListBuilder();
    }

    private UserEntity user;

    private TestUserEntityBuilder() {
        user = TestRandomUtils.randomObject(UserEntity.class);
    }

    public UserEntity build() {
        return user;
    }

    public static class ListBuilder {

        private int size;

        private ListBuilder() {
        }

        public ListBuilder withSize(int size) {
            this.size = size;
            return this;
        }

        public List<UserEntity> buildList() {
            return Stream.generate(this::build)
                    .limit(size > 0 ? size : RandomUtils.nextInt(1, 10))
                    .collect(Collectors.toList());
        }

        private UserEntity build() {
            TestUserEntityBuilder builder = new TestUserEntityBuilder();
            return builder.build();
        }
    }

}
