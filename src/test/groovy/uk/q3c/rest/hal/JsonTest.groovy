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

import com.fasterxml.jackson.databind.SerializationFeature
import org.apache.commons.io.FileUtils
import org.skyscreamer.jsonassert.JSONAssert
import spock.lang.Specification
import uk.q3c.util.testutil.TestResource

/**
 * Created by David Sowerby on 06 Feb 2017
 */
abstract class JsonTest extends Specification {

    final String href = "/users/1"

    Object resource
    Object resource2
    HalMapper objectMapper
    StringWriter sw


    def setup() {
        objectMapper = new HalMapper()
        sw = new StringWriter()
    }

    protected void asExpected(String expected) {
        println "EXPECTED >>>>"
        println expected
        sw = new StringWriter()
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        objectMapper.writeValue(sw, resource)
        String actual = sw.toString()
        println "\nACTUAL >>>>"
        println sw.toString()
        JSONAssert.assertEquals(expected, actual, true)
    }

    protected void asExpectedFromFile(String fileName) {
        File f = TestResource.resource(this, fileName)
        String expected = FileUtils.readFileToString(f, "UTF-8")
        asExpected(expected)
    }

    protected void prettyOut(String expected, String title) {
        println title
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        def s = objectMapper.readValue(expected, resource.getClass())
        objectMapper.writeValue(sw, s)
        println sw.toString() + "\n\n"

    }

    protected String inputFromFile(String filename) {
        File f = TestResource.resource(this, filename)
        String s = FileUtils.readFileToString(f, "UTF-8")
        println "Input file:\n"
        println s
        return s

    }

    /**
     * Writes the resource to JSON, then reads it back into resource2.  Note that this only tests writing and reading JSON,
     * HalResource has no way of reconstructing objects held as values in the map
     *
     * @param resourceClass
     * @return
     */
    protected <T> boolean validateRoundTrip(Class<T> resourceClass) {
        objectMapper.writeValue(sw, resource)
        T result = objectMapper.readValue(sw.toString(), T)
        StringWriter sw2 = new StringWriter()
        objectMapper.writeValue(sw2, result)
        resource2 = result
        println "\nRETURNED >>>>"
        println sw2.toString()
        JSONAssert.assertEquals(sw.toString(), sw2.toString(), true)
        return true
    }
}
