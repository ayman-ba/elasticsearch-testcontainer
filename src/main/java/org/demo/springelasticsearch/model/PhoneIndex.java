package org.demo.springelasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "#{@elasticsearch-config.index-names.phones-index}")
public class PhoneIndex {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, name = "model")
    private String model;

    @Field(type = FieldType.Text, name = "brand")
    private String brand;

    @Field(type = FieldType.Double, name = "price")
    private Double price;
}
