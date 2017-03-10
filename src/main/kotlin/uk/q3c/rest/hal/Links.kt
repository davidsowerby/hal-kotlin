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

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

/**
 * Created by David Sowerby on 28 Feb 2017
 */
class Links @JsonCreator constructor(var self: HalLink = HalLink()) {
    private var links: MutableMap<String, HalLink> = mutableMapOf()
    private var lists: MutableMap<String, List<HalLink>> = mutableMapOf()

    fun add(propertyName: String, link: HalLink) {
        links.put(propertyName, link)
        // avoid duplicate property name
        lists.remove(propertyName)
    }

    fun add(propertyName: String, href: String) {
        add(propertyName, HalLink(href))
    }

    fun add(propertyName: String, links: List<HalLink>) {
        lists.put(propertyName, links)
        // avoid duplicate property name
        this.links.remove(propertyName)
    }

    fun hasLink(propertyName: String): Boolean {
        return links.get(propertyName) != null
    }

    fun hasLinks(propertyName: String): Boolean {
        return lists.get(propertyName) != null
    }

    /**
     * Serialization
     */
    @JsonAnyGetter
    private fun members(): Map<String, Any> {
        val copy: MutableMap<String, Any> = mutableMapOf()
        copy.putAll(links)
        copy.putAll(lists)
        return copy
    }

    /**
     * Deserialization
     */
    @Suppress("UNCHECKED_CAST")
    @JsonAnySetter
    private fun setProperty(name: String, value: Any) {
        if (value is Map<*, *>) {
            val link = createLink(value as Map<String, String>)
            links.put(name, link)
        } else {
            if (value is ArrayList<*>) {
                val listOfMaps = value as ArrayList<Map<String, String>>
                val listOfLinks: MutableList<HalLink> = mutableListOf()
                for (entry in listOfMaps) {
                    val link = createLink(entry)
                    listOfLinks.add(link)
                }
                lists.put(name, listOfLinks)
            }
        }
    }

    private fun createLink(linkMap: Map<String, String>): HalLink {
        val link = HalLink()
        val href: String? = linkMap.get("href")
        if (href != null) {
            link.href = href
        }
        return link
    }


    /**
     * Returns true if at least one property is set.  'self' is ignored
     */
    @JsonIgnore
    fun isNotEmpty(): Boolean {
        return links.isNotEmpty() || lists.isNotEmpty()
    }

    /**
     * Returns true if there are no properties set. 'self' is ignored
     */
    @JsonIgnore
    fun isEmpty(): Boolean {
        return links.isEmpty() && lists.isEmpty()
    }

    /**
     * Removes the entry for [propertyName]
     */
    fun remove(propertyName: String) {
        links.remove(propertyName)
        lists.remove(propertyName)
    }


    /**
     * Returns a [HalLink], or throws an exception if there is no entry for [propertyName]
     *
     * @throws LinkException if there is no such property.
     */
    fun link(propertyName: String): HalLink {
        val result = links.get(propertyName)
        if (result == null) {
            throw LinkException("There is no property named '$propertyName'")
        } else {
            return result
        }
    }

    /**
     * Returns a list of [HalLink] associated with [propertyName].  An empty list may be returned
     *
     * @throws LinkException if there is no such property.
     */
    fun links(propertyName: String): List<HalLink> {
        val result = lists.get(propertyName)
        if (result == null) {
            throw LinkException("There is no property named '$propertyName'")
        } else {
            return result
        }
    }


}

class LinkException(msg: String) : RuntimeException(msg)

