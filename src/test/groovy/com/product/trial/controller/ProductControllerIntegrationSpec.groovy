package com.product.trial.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.product.trial.TrialApplication
import com.product.trial.entity.Product
import com.product.trial.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.hamcrest.Matchers.containsString
import org.springframework.http.MediaType
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = TrialApplication.class)
class ProductControllerIntegrationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ProductRepository productRepository

    def setup() {
        productRepository.deleteAll()
    }

    def "should load trial application context without issues"() {
        expect:
        true
    }

    def "should create and retrieve all products"() {
        given:
        def productJson = '''
        {
          "code": "PRD001",
          "name": "Wireless Headphones",
          "description": "Noise-cancelling Bluetooth headphones",
          "image": "https://example.com/images/headphones.jpg",
          "category": "Electronics",
          "price": 129.99,
          "quantity": 50,
          "internalReference": "WH-2025-01",
          "shellId": 10,
          "inventoryStatus": "INSTOCK",
          "rating": 4.5
        }'''

        when:
        def postResult = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))

        then:
        postResult.andExpect(status().isCreated())

        when:
        def getResult = mockMvc.perform(get("/products"))

        then:
        getResult.andExpect(status().isOk())
        getResult.andExpect(jsonPath('$[0].code').value("PRD001"))
    }

    def "should retrieve and patch product by ID"() {
        given:
        def productJson = '''
            {
              "code": "PRD002",
              "name": "Bluetooth Speaker",
              "description": "Portable Bluetooth speaker",
              "image": "https://example.com/images/speaker.jpg",
              "category": "Electronics",
              "price": 59.99,
              "quantity": 20,
              "internalReference": "BS-2025-02",
              "shellId": 20,
              "inventoryStatus": "INSTOCK",
              "rating": 4.0
            }'''

        def postResult = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andReturn()
        def createdProductId = new ObjectMapper().readValue(postResult.getResponse().getContentAsString(), Product.class).getId()

        when:
        def url = "/products/" + createdProductId
        def getResult = mockMvc.perform(get(url))

        then:
        getResult.andExpect(status().isOk())
        getResult.andExpect(jsonPath('$.code').value("PRD002"))

        when: "Patching a product"
        def patchJson = '''
                {
                  "price": 49.99,
                  "quantity": 30,
                  "code": "PRD002",
                  "internalReference": "BS-2025-02"
                }
                '''

        def patchResult = mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchJson))

        then: "Verify patch response has updated fields"
        patchResult.andExpect(status().isOk())
                .andExpect(jsonPath('$.price').value(49.99))
                .andExpect(jsonPath('$.quantity').value(30))

        when: "Retrieve the product again to verify update"
        def getUpdated = mockMvc.perform(get(url))

        then: "Updated fields should be present"
        getUpdated.andExpect(status().isOk())
                .andExpect(jsonPath('$.price').value(49.99))
                .andExpect(jsonPath('$.quantity').value(30))

    }


    def "should delete product by ID"() {
        given:
        def productJson = '''
            {
              "code": "PRD002",
              "name": "Bluetooth Speaker",
              "description": "Portable Bluetooth speaker",
              "image": "https://example.com/images/speaker.jpg",
              "category": "Electronics",
              "price": 59.99,
              "quantity": 20,
              "internalReference": "BS-2025-02",
              "shellId": 20,
              "inventoryStatus": "INSTOCK",
              "rating": 4.0
            }'''

        def postResult = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andReturn()
        def createdProductId = new ObjectMapper().readValue(postResult.getResponse().getContentAsString(), Product.class).getId()

        when: "Deleting and retrieving the product again"
        def url = "/products/" + createdProductId
        mockMvc.perform(delete(url))
        def getUpdated = mockMvc.perform(get(url))

        then: "It should return not found"
        getUpdated.andExpect(status().isNotFound()).andExpect(jsonPath('$.error', containsString("not found")))
    }

}

