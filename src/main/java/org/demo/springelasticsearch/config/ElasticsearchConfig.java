package org.demo.springelasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.demo.springelasticsearch.config.properties.ElasticsearchProperties;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfig.class);
    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public ElasticsearchClient elasticsearchClient(){
        var connectionUrl = elasticsearchProperties.getConnectionUrl();
        LOGGER.debug("Trying to connect to elastic instance at {} .", connectionUrl);
        RestClient restClient = RestClient.builder(
                HttpHost.create(connectionUrl))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
