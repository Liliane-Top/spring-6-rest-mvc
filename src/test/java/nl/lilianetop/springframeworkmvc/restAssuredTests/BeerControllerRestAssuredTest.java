package nl.lilianetop.springframeworkmvc.restAssuredTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(BeerControllerRestAssuredTest.TestConfig.class)
@ComponentScan(basePackages ="nl.lilianetop.springframeworkmvc")
public class BeerControllerRestAssuredTest {
    @Configuration
    public static class TestConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeRequests(requests -> {
                requests.anyRequest().permitAll();
            });
            return http.build();
        }
    }

    @LocalServerPort
    Integer localPort;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
    }

    @Test
    void testListBeer() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/beer")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
