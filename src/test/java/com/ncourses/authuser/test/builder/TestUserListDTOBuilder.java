package com.ncourses.authuser.test.builder;

import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.model.dtos.UserListDto;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUserListDTOBuilder {

    public static TestUserListDTOBuilder builder() {
        return new TestUserListDTOBuilder();
    }

    public static ListBuilder listBuilder() {
        return new ListBuilder();
    }

    private UserListDto dto;

    private TestUserListDTOBuilder() {
        dto = TestRandomUtils.randomObject(UserListDto.class);
    }

    public UserListDto build() {
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

        public List<UserListDto> buildList() {
            return Stream.generate(this::build)
                    .limit(size > 0 ? size : RandomUtils.nextInt(1, 10))
                    .collect(Collectors.toList());
        }

        private UserListDto build() {
            TestUserListDTOBuilder builder = new TestUserListDTOBuilder();
            return builder.build();
        }
    }

}
