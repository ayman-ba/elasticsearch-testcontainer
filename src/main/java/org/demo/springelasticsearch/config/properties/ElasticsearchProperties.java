package org.demo.springelasticsearch.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("elasticsearch-config")
public class ElasticsearchProperties {

    private String connectionUrl;
    private IndexNameProperties indexNames;
}
