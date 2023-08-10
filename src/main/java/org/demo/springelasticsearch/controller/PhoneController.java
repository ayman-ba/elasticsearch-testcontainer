package org.demo.springelasticsearch.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.demo.springelasticsearch.model.PhoneIndex;
import org.demo.springelasticsearch.request.PhoneRequest;
import org.demo.springelasticsearch.service.PhoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/phones")
@RequiredArgsConstructor(
        access = AccessLevel.PACKAGE
)
public class PhoneController {

    private final PhoneService productService;

    @PostMapping
    public ResponseEntity<String> savePhone(@RequestBody PhoneRequest phoneRequest) {
        return ResponseEntity.ok(productService.savePhone(phoneRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneIndex> getPhoneById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getPhoneById(id));
    }

    @GetMapping
    public ResponseEntity<List<PhoneIndex>> getPhones(){
        return ResponseEntity.ok(productService.getPhones());
    }
}
