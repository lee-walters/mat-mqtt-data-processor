package com.mclaren.challenge.model.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarStatus {
    private Long timestamp;
    private Integer carIndex;
    private String type;
    private Integer value;
}
