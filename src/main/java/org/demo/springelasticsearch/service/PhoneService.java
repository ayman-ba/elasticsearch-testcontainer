package org.demo.springelasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.demo.springelasticsearch.config.properties.ElasticsearchProperties;
import org.demo.springelasticsearch.model.PhoneIndex;
import org.demo.springelasticsearch.request.PhoneRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(
        access = AccessLevel.PACKAGE
)
public class PhoneService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneService.class);
    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchProperties elasticsearchProperties;

    public String savePhone(PhoneRequest phoneRequest) {
        try {
            var phoneIndex = PhoneIndex.builder()
                    .id(UUID.randomUUID())
                    .brand(phoneRequest.getBrand())
                    .model(phoneRequest.getModel())
                    .price(phoneRequest.getPrice())
                    .build();
            var phoneIndexRequest = new IndexRequest.Builder<PhoneIndex>()
                    .index(elasticsearchProperties.getIndexNames().getPhonesIndex())
                    .id(phoneIndex.getId().toString())
                    .document(phoneIndex)
                    .build();
            var response = elasticsearchClient.index(phoneIndexRequest);
            LOGGER.info("Document with id: {} was successfully saved", response.id());
            return response.id();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PhoneIndex getPhoneById(UUID id) {
        try {
            var response = elasticsearchClient.get(g -> g
                            .index(elasticsearchProperties.getIndexNames().getPhonesIndex())
                            .id(id.toString()), PhoneIndex.class);
            if (response.found()) {
                LOGGER.info("Document with id : {} retrieved successfully", response.id());
                return response.source();
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PhoneIndex> getPhones(){
        var searchRequest = SearchRequest.of(s -> s.index(
                elasticsearchProperties.getIndexNames().getPhonesIndex()
        ));
        try {
            var searchResponse = elasticsearchClient.search(searchRequest, PhoneIndex.class);
            var responseHits = searchResponse.hits();
            LOGGER.info( "Fetching {} document(s) from index {} as total",
                    searchResponse.hits().total().value(),
                    elasticsearchProperties.getIndexNames().getPhonesIndex()
            );
            return responseHits.hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
