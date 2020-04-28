package com.kpi.testing.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

public class SinglModelMapper {

    public ModelMapper mapper;

    private SinglModelMapper(){
        mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
    }

    public ModelMapper getMapper(){
        return mapper;
    }

    private static class LazyHolder {
        static final SinglModelMapper INSTANCE = new SinglModelMapper();
    }
    public static SinglModelMapper getInstance() {
        return LazyHolder.INSTANCE;
    }
}


