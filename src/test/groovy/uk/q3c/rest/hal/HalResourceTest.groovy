/*
 *
 *  *
 *  *  *Copyright (c) 2017.  David Sowerby
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  *  * specific language governing permissions and limitations under the License.
 *  *
 *
 */

package uk.q3c.rest.hal

import com.google.common.collect.ImmutableList
import uk.q3c.ion.rest.Review

/**
 * Created by David Sowerby on 01 Mar 2017
 */
class HalResourceTest extends JsonTest {

    HalResource halResource


    def setup() {
        halResource = new HalResource()
        resource = halResource
    }

    def "empty"() {

        when:
        true //do nothing

        then:
        asExpectedFromFile("emptyResource.json")
        then: "round tripped"
        validateRoundTrip(HalResource)
    }

    def "self set"() {
        when:
        halResource.self("/users/1")

        then:
        asExpectedFromFile("resourceWithSelf.json")

        then: "round tripped"
        validateRoundTrip(HalResource)
    }

    def "additional and multiple links"() {
        given:
        HalLink link1 = new HalLink("/orders/1")
        link1.setProperty("title", "latest order")
        HalLink link2 = new HalLink("/orders/2")
        HalLink link3 = new HalLink("/orders/3")
        List<HalLink> oldOrders = ImmutableList.of(link2, link3)

        when:
        halResource.link("latest", link1)
        halResource.link("oldOrders", oldOrders)

        then: "output in correct format"
        asExpectedFromFile("resourceWithLinks.json")

        then: "round tripped"
        validateRoundTrip(HalResource)
    }

    def "embedded"() {
        given:
        HalResource manufacturer = new HalResource()
        manufacturer.self("/manufacturers/328764")
        manufacturer.link("homepage", new HalLink("http://hoverdonkey.com"))
        manufacturer.setProperty("name", "Manufacturer Inc.")

        Review review = new Review(5, "pretty cool")

        when:
        halResource.self("/products/3")
        halResource.embed("manufacturer", manufacturer)

        then:
        halResource.self().href == "/products/3"
        asExpectedFromFile("resourceWithLinksAndEmbedded.json")

        then: "round tripped"
        validateRoundTrip(HalResource)

        when: "nested embed"
        manufacturer.embed("review", review)

        then:
        asExpectedFromFile("resourceWithLinksAndNestedEmbedded.json")

        then: "round tripped"
        validateRoundTrip(HalResource)


    }
}
