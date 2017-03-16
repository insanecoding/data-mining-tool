package com.me;

import com.me.core.service.dao.DbCreationUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataMiningApplication {

    public static void main(String[] args) throws Exception {
        DbCreationUtility.createDbIfAbsent();
        SpringApplication.run(DataMiningApplication.class, args);
    }
}
