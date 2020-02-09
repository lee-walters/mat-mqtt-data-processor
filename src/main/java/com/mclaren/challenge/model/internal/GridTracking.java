package com.mclaren.challenge.model.internal;

import com.mclaren.challenge.model.inbound.CarCoordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GridTracking {
    private CarCoordinate contextCar;
    private LinkedList<Integer> originalCarPositions;
    private LinkedList<Integer> newCarPositions;
}
