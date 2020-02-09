package com.mclaren.challenge.model.internal;

import com.mclaren.challenge.model.outbound.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarSpeedDistance {
    private Double distance;
    private CarStatus carStatus;
}
