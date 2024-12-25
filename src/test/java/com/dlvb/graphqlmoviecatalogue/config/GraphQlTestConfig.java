package com.dlvb.graphqlmoviecatalogue.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;

@TestConfiguration
public class GraphQlTestConfig {

    @Bean
    public WebGraphQlHandler webGraphQlHandler(ExecutionGraphQlService graphQlService) {
        return WebGraphQlHandler.builder(graphQlService).build();
    }

    @Bean
    public GraphQlTester graphQlTester(WebGraphQlHandler webGraphQlHandler) {
        return WebGraphQlTester.builder(webGraphQlHandler).build();
    }

}
