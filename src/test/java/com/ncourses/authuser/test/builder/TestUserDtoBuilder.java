package com.ncourses.authuser.test.builder;

import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.enums.UserType;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUserDtoBuilder {

    public static TestUserDtoBuilder builder() {
        return new TestUserDtoBuilder();
    }

    public static ListBuilder listBuilder() {
        return new ListBuilder();
    }

    private UserDto dto;

    private TestUserDtoBuilder() {
        dto = TestRandomUtils.randomObject(UserDto.class);
    }

    public TestUserDtoBuilder withUserType(UserType userType) {
        dto.setUserType(userType);
        return this;
    }

    public UserDto build() {
        return dto;
    }

    public static class ListBuilder {

        private int size;

        private ListBuilder() {
        }

        public ListBuilder withSize(int size) {
            this.size = size;
            return this;
        }

        public List<UserDto> buildList() {
            return Stream.generate(this::build)
                    .limit(size > 0 ? size : RandomUtils.nextInt(1, 10))
                    .collect(Collectors.toList());
        }

        private UserDto build() {
            TestUserDtoBuilder builder = new TestUserDtoBuilder();
            return builder.build();
        }
    }

}
