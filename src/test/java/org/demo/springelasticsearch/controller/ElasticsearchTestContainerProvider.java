package org.demo.springelasticsearch.controller;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.Objects;

public final class ElasticsearchTestContainerProvider {

    private static final String ELASTICSEARCH_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:8.7.1";
    private static ElasticsearchContainer elasticsearchContainer;

    public static ElasticsearchContainer getInstance() {
        if (Objects.isNull(elasticsearchContainer)) {
            try (ElasticsearchContainer container = new ElasticsearchContainer(ELASTICSEARCH_IMAGE_NAME)) {
                container.getEnvMap().put("xpack.security.enabled", "false");
                container.waitingFor(
                        Wait.forLogMessage(".*started.*", 1)
                );
                elasticsearchContainer = container;
            }
        }
        return elasticsearchContainer;
    }

    private ElasticsearchTestContainerProvider() {
    }
}
