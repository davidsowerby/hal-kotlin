package uk.q3c.rest.hal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Created by David Sowerby on 15 Apr 2017
 */
public class TestResource {

    @JsonProperty
    private OffsetDateTime dateTime;
    @JsonProperty
    private Optional<Integer> optional = Optional.of(17);
    @JsonProperty
    private int integer = 3;
    @JsonProperty
    private String string = "any old rubbish";

    TestResource() {
    }

    public TestResource(OffsetDateTime dateTime, Optional<Integer> optional, int integer, String string) {
        this.dateTime = dateTime;
        this.optional = optional;
        this.integer = integer;
        this.string = string;
    }
}

