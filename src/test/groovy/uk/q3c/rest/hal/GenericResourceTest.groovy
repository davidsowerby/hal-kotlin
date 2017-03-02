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
/**
 * Created by David Sowerby on 28 Feb 2017
 */
class GenericResourceTest extends JsonTest {


    def setup() {
        resource = new GenericResource()

    }


    def "round trip"() {
        when: "default state"
        true

        then:
        resource.isEmpty()
        !resource.isNotEmpty()

        when:
        resource.setProperty("a", 1)

        then:
        !resource.isEmpty()
        resource.isNotEmpty()

        when:
        objectMapper.writeValue(sw, resource)
        GenericResource result = objectMapper.readValue(sw.toString(), GenericResource)

        then:
        result.getProperty("a") == 1
    }
}
