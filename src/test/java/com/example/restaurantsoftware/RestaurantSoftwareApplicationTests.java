package com.example.restaurantsoftware;

import com.example.restaurantsoftware.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestConfig.class})
class RestaurantSoftwareApplicationTests {

    @Test
    void contextLoads() {
    }

}
