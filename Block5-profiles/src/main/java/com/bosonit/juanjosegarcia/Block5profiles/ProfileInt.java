package com.bosonit.juanjosegarcia.Block5profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("INT")
public class ProfileInt implements GenericProfile{

    @Value("${bd.url}")
    private String databaseUrl;

    @Override
    public String getDataBaseUrl() {
        return this.databaseUrl;
    }
}
