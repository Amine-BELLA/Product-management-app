package com.product.trial.service

import com.product.trial.entity.Product
import com.product.trial.entity.ShoppingCart
import com.product.trial.exception.InsufficientStockException
import com.product.trial.repository.ProductRepository
import com.product.trial.repository.ShoppingCartRepository
import spock.lang.Specification
import spock.lang.Subject
import com.product.trial.exception.ProductNotFoundException

class ShoppingCartServiceSpec extends Specification {

    @Subject
    ShoppingCartService shoppingCartService

    ProductRepository productRepository = Mock()
    ShoppingCartRepository shoppingCartRepository = Mock()

    def setup() {
        shoppingCartService = new ShoppingCartService(shoppingCartRepository, productRepository)
    }

    def "should add item to cart when product exists and is in stock"() {
        given:
        def userEmail = "user@example.com"
        def productId = 1L
        def quantity = 2
        def product = new Product(id: productId, quantity: 10)
        def cart = new ShoppingCart(items: [])

        when:
        productRepository.findById(productId) >> Optional.of(product)
        shoppingCartRepository.findByUserEmail(userEmail) >> Optional.of(cart)
        shoppingCartRepository.save(cart) >> cart

        def result = shoppingCartService.addItemToCart(userEmail, productId, quantity)

        then:
        result.items.size() == 1
        result.items[0].product.id == productId
        result.items[0].quantity == quantity
    }

    def "should throw ProductNotFoundException when product does not exist"() {
        given:
        def userEmail = "user@example.com"
        def productId = 99L
        def cart = new ShoppingCart(items: [])

        when:
        productRepository.findById(productId) >> Optional.empty()
        shoppingCartRepository.findByUserEmail(userEmail) >> Optional.of(cart)
        shoppingCartService.addItemToCart(userEmail, productId, 1)
        then:
        thrown(ProductNotFoundException)
    }

    def "should throw InsufficientStockException when product is out of stock"() {
        given:
        def userEmail = "user@example.com"
        def productId = 1L
        def product = new Product(id: productId, quantity: 0)
        def cart = new ShoppingCart(items: [])

        when:
        productRepository.findById(productId) >> Optional.of(product)
        shoppingCartRepository.findByUserEmail(userEmail) >> Optional.of(cart)
        shoppingCartService.addItemToCart(userEmail, productId, 1)

        then:
        thrown(InsufficientStockException)
    }

}
