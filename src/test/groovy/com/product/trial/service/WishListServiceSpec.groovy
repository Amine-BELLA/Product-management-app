package com.product.trial.service

import com.product.trial.entity.Product
import com.product.trial.entity.WishList
import com.product.trial.repository.ProductRepository
import com.product.trial.repository.WishListRepository
import spock.lang.Specification
import com.product.trial.exception.ProductNotFoundException
import spock.lang.Subject

class WishListServiceSpec extends Specification {

    @Subject
    WishListService wishlistService

    WishListRepository wishlistRepository = Mock()
    ProductRepository productRepository = Mock()

    def setup() {
        wishlistService = new WishListService(wishlistRepository, productRepository)
    }

    def "should add item to wishlist when product exists"() {
        given:
        def userEmail = "user@example.com"
        def productId = 1L
        def product = new Product(id: productId)
        def wishlist = new WishList(items: [])

        when:
        productRepository.findById(productId) >> Optional.of(product)
        wishlistRepository.findByUserEmail(userEmail) >> Optional.of(wishlist)
        wishlistRepository.save(wishlist) >> wishlist

        def result = wishlistService.addProductToWishlist(userEmail, productId)

        then:
        result.items.size() == 1
        result.items[0].product.id == productId
    }

    def "should throw ProductNotFoundException when product does not exist"() {
        given:
        def userEmail = "user@example.com"
        def productId = 99L
        def wishlist = new WishList(items: [])

        when:
        productRepository.findById(productId) >> Optional.empty()
        wishlistRepository.findByUserEmail(userEmail) >> Optional.of(wishlist)
        wishlistService.addProductToWishlist(userEmail, productId)

        then:
        thrown(ProductNotFoundException)
    }
}
