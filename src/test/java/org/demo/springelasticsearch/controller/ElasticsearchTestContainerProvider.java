package org.demo.springelasticsearch.controller;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.Objects;

public final class ElasticsearchTestContainerProvider {

    private static final String ELASTICSEARCH_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:8.7.1";
    private static ElasticsearchContainer elasticsearchContainer;

    public static ElasticsearchContainer getInstance() {
        if (Objects.isNull(elasticsearchContainer)) {
            try (ElasticsearchContainer container = new ElasticsearchContainer(ELASTICSEARCH_IMAGE_NAME)) {
                elasticsearchContainer = container;
            }
        }
        return elasticsearchContainer;
    }

    private ElasticsearchTestContainerProvider() {
    }
}
