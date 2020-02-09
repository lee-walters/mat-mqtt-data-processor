package com.mclaren.challenge.model.inbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarCoordinate {
    private Long timestamp;
    private Integer carIndex;
    private Location location;
}
