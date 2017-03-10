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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Created by David Sowerby on 28 Feb 2017
 */
open class HalResource @JsonCreator constructor(
        val _links: Links = Links(),
        @JsonInclude(JsonInclude.Include.NON_EMPTY) val _embedded: MutableMap<String, HalResource> = mutableMapOf())

    : GenericResource<Any>() {

    fun self(href: String): HalResource {
        _links.self = HalLink(href = href)
        return this
    }

    fun self(): HalLink {
        return _links.self
    }

    fun href(): String {
        return _links.self.href
    }

    /**
     * Adds a link
     */
    fun link(propertyName: String, link: HalLink): HalResource {
        _links.add(propertyName, link)
        return this
    }

    fun link(propertyName: String, links: List<HalLink>): HalResource {
        _links.add(propertyName, links)
        return this
    }

    fun embed(propertyName: String, resource: HalResource): HalResource {
        _embedded.put(propertyName, resource)
        return this
    }

    fun hasLink(propertyName: String): Boolean {
        return _links.hasLink(propertyName)
    }

    fun link(propertyName: String): HalLink? {
        return _links.link(propertyName)
    }

    fun links(propertyName: String): List<HalLink> {
        return _links.links(propertyName)
    }

    fun hasLinks(propertyName: String): Boolean {
        return _links.hasLinks(propertyName)
    }
}