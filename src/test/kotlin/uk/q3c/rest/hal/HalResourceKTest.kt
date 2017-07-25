package uk.q3c.rest.hal

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveKey
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Created by David Sowerby on 24 Jul 2017
 */
class HalResourceKTest : Spek({
    given("basic resource") {
        val halResource = HalResource()
        val manufacturer = HalResource()
        manufacturer.self("/manufacturers/328764")
        manufacturer.link("homepage", HalLink("http://hoverdonkey.com"))
        manufacturer.setProperty("name", "Manufacturer Inc.")
        val review = Review(5, "pretty cool")

        on("when resource has embedded resources added") {
            halResource.self("/products/3")
            halResource.embed("manufacturer", manufacturer)
            halResource.embed("review", review)

            it("should contain embedded resources") {
                halResource.self().href shouldEqual "/products/3"
                halResource._embedded shouldHaveKey "manufacturer"
                halResource._embedded shouldHaveKey "review"
            }
        }
    }
})





