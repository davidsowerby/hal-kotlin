package uk.q3c.rest.hal

import spock.lang.Specification

import java.time.OffsetDateTime

/**
 * Created by David Sowerby on 15 Apr 2017
 */
class HalMapperTest extends Specification {

    HalMapper mapper

    def setup() {
        mapper = new HalMapper()
    }

    def "Using Java HalResource round trip, with Optional and Java 8 Date fields included"() {
        given:
        OffsetDateTime dateTime = OffsetDateTime.now()
        TestResource testResource = new TestResource()
        testResource.dateTime = dateTime

        when:
        StringWriter sw = new StringWriter()
        mapper.writeValue(sw, testResource)
        TestResource result = mapper.readValue(sw.toString(), TestResource.class)

        then:
        result.integer == 3
        result.string == "any old rubbish"
        result.dateTime.isEqual(dateTime)
    }
}
