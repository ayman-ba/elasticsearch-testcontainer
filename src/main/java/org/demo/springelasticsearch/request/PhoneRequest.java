package org.demo.springelasticsearch.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {

    private String model;
    private String brand;
    private Double price;
}
