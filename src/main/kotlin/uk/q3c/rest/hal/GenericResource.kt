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

open class GenericResource<T> @JsonCreator constructor() {

    // and then "other" stuff:
    private var map: MutableMap<String, T> = HashMap()

    fun getProperty(name: String): T? {
        return map[name]
    }

    // "any getter" needed for serialization;  not ideal as this exposes the property
    @JsonAnyGetter
    protected fun members(): Map<String, T> {
        return map
    }

    @JsonAnySetter
    fun setProperty(name: String, value: T) {
        map.put(name, value)
    }

    @JsonIgnore
    fun isNotEmpty(): Boolean {
        return map.isNotEmpty()
    }

    @JsonIgnore
    fun isEmpty(): Boolean {
        return map.isEmpty()
    }
}