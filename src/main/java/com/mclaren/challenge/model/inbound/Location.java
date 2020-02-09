package com.mclaren.challenge.model.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {
    @JsonProperty("lat")
    private Float latitude;
    @JsonProperty("long")
    private Float longitude;
}
