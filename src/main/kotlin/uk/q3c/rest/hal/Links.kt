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

/**
 * Created by David Sowerby on 28 Feb 2017
 */
class Links @JsonCreator constructor(var self: HalLink = HalLink()) : GenericResource<Any>() {

    fun add(propertyName: String, link: HalLink) {
        setProperty(propertyName, link)
    }

    fun add(propertyName: String, links: List<HalLink>) {
        setProperty(propertyName, links)
    }


}