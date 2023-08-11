package org.demo.springelasticsearch.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.springelasticsearch.config.properties.ElasticsearchProperties;
import org.demo.springelasticsearch.model.PhoneIndex;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.util.UUID;

@TestPropertySource(
        value = "classpath:/application-test.yml",
        properties = {
                "spring.profiles.active=test"
        })
@SpringBootTest
@AutoConfigureMockMvc
class PhoneControllerIT {

    private static final ElasticsearchContainer elasticsearchContainer = ElasticsearchTestContainerProvider.getInstance();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ElasticsearchProperties elasticsearchProperties;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private ObjectMapper objectMapper;
    private String phonesIndexName;

    private final static String ID_PHONE_1 = "e99aa912-8a24-4809-beaa-e4db6ca6e390";

    @BeforeEach
    void setup() throws IOException {
        this.phonesIndexName = elasticsearchProperties.getIndexNames().getPhonesIndex();
        elasticsearchClient.indices().create(fn -> fn.index(phonesIndexName));
    }

    @BeforeAll
    static void setupAll() {
        elasticsearchContainer.start();
    }

    @Test
    void should_phones_index_exists() throws IOException {
        var phonesIndex = elasticsearchClient.indices()
                .exists(fn -> fn.index(phonesIndexName));
        assertTrue(phonesIndex.value());
    }

    @Test
    void should_save_phone_return_201() throws Exception {
        var samsungIndex = PhoneIndex.builder()
                .id(UUID.fromString(ID_PHONE_1))
                .brand("SAMSUNG")
                .model("S10+")
                .price(562.0)
                .build();
        var samsungIndexRequest = new IndexRequest.Builder<PhoneIndex>()
                .index(phonesIndexName)
                .id(ID_PHONE_1)
                .document(samsungIndex)
                .build();

        var mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/phones")
                        .content(objectMapper.writeValueAsString(samsungIndexRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void should_get_phone_by_id_return_200() throws Exception {
        var iphoneIndex = PhoneIndex.builder()
                .id(UUID.fromString(ID_PHONE_1))
                .brand("APPLE")
                .model("IPHONE 12")
                .price(859.0)
                .build();
        var iphoneId = iphoneIndex.getId().toString();
        var iphoneIndexRequest = new IndexRequest.Builder<PhoneIndex>()
                .index(phonesIndexName)
                .id(iphoneId)
                .document(iphoneIndex)
                .build();

        elasticsearchClient.index(iphoneIndexRequest);
        elasticsearchClient.indices().refresh(fn -> fn.index(phonesIndexName));

        elasticsearchClient.get(fn -> fn.index(phonesIndexName)
                .id(iphoneId), PhoneIndex.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/phones/" + iphoneId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{\"id\":\"e99aa912-8a24-4809-beaa-e4db6ca6e390\",\"model\":\"IPHONE 12\",\"brand\":\"APPLE\",\"price\":859.0}"
                        , true));
    }

    @AfterEach
    void tearDown() throws IOException {
        elasticsearchClient.indices().delete(fn -> fn.index(phonesIndexName));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("elasticsearch-config.connection-url=",
                elasticsearchContainer::getHttpHostAddress);
    }

}
