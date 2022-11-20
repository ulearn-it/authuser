package com.ncourses.authuser.test.util;

import lombok.experimental.UtilityClass;
import org.jeasy.random.EasyRandom;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class TestRandomUtils {

    private final EasyRandom EASY_RANDOM = new EasyRandom();

    public <T> T randomObject(Class<T> type) {
        return EASY_RANDOM.nextObject(type);
    }

    public <T> List<T> randomList(Class<T> classType, int size) {
        return EASY_RANDOM.objects(classType, size)
                .collect(Collectors.toList());
    }

    public <T extends Enum<T>> T randomEnum(Class<T> type) {
        return EASY_RANDOM.nextObject(type);
    }

}
