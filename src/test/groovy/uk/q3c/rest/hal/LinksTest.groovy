package uk.q3c.rest.hal

import com.google.common.collect.ImmutableList

/**
 * Created by David Sowerby on 08 Mar 2017
 */
class LinksTest extends JsonTest {

    Links links


    def setup() {
        links = new Links()
        sw = new StringWriter()
        resource = links
    }

    def "write / read Json"() {
        given:
        HalLink linka = new HalLink("a")
        HalLink linkb = new HalLink("b")
        HalLink linkc = new HalLink("c")
        HalLink linkd = new HalLink("d")

        List<HalLink> family = ImmutableList.of(linkb, linkc)

        when:
        links.add("single", linka)
        links.add("another single", linkd)
        links.add("multi", family)
        objectMapper.writeValue(sw, resource)
        Links r = objectMapper.readValue(sw.toString(), Links.class)

        then:
        validateRoundTrip(Links.class)
        r.link("single").href == "a"
        r.link("another single").href == "d"
        r.links("multi").get(0).href == "b"
        r.links("multi").get(1).href == "c"
        r.hasLink("single")
        !r.hasLink("multi")
        !r.hasLinks("single")
        r.hasLinks("multi")

    }

    def "remove"() {
        given:
        HalLink linka = new HalLink("a")
        HalLink linkb = new HalLink("b")
        HalLink linkc = new HalLink("c")
        List<HalLink> family = ImmutableList.of(linkb, linkc)

        when:
        links.add("single", linka)
        links.add("multi", family)
        links.remove("single")
        links.link("single")

        then:
        thrown LinkException

        when:
        links.remove("multi")
        links.links("multi")

        then:
        thrown LinkException
    }

    def "add with same property name does not duplicate property"() {
        given:
        HalLink linka = new HalLink("a")
        HalLink linkb = new HalLink("b")
        HalLink linkc = new HalLink("c")
        List<HalLink> family = ImmutableList.of(linkb, linkc)

        when:
        links.add("single", linka)
        links.add("single", family)

        then:
        links.links("single") == family

        when:
        links.link("single")

        then:
        thrown LinkException

        when:
        links.add("single", linka)

        then:
        links.link("single") == linka

        when:
        links.links("single")

        then:
        thrown LinkException
    }

    def "empty or not - single link"() {
        given:
        HalLink linka = new HalLink("a")

        when: "empty"
        true

        then:
        links.isEmpty()
        !links.isNotEmpty()

        when:
        links.add("single", linka)

        then:
        !links.isEmpty()
        links.isNotEmpty()
    }

    def "empty or not - list of links"() {
        given:
        HalLink linkb = new HalLink("b")
        HalLink linkc = new HalLink("c")
        List<HalLink> family = ImmutableList.of(linkb, linkc)

        when: "empty"
        true

        then:
        links.isEmpty()
        !links.isNotEmpty()

        when:
        links.add("multi", family)

        then:
        !links.isEmpty()
        links.isNotEmpty()
    }

    def "add as href"() {
        given:
        String href = "users/1"
        String propertyName = "userRef"

        when:
        links.add(propertyName, href)

        then:
        links.hasLink(propertyName)
        links.link(propertyName).href == href
    }
}
