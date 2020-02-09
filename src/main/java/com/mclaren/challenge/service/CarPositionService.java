package com.mclaren.challenge.service;

import com.mclaren.challenge.model.internal.GridTracking;
import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.outbound.CarStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.mclaren.challenge.utils.Constants.POSITION;

@Service
public class CarPositionService extends AbstractCarStatusService {

    /**
     * Calculate the distance travelled for the car in metres and generate position
     *
     * @param car
     * @param distance
     */
    public synchronized GridTracking updateCarStatusPosition(final CarCoordinate car, final double distance) {
        LinkedList<Integer> originalCarPositions = new LinkedList<>();
        LinkedList<Integer> newCarPositions = new LinkedList<>();

        if (!distanceTravelledTracker.containsKey(car.getCarIndex())) {
            distanceTravelledTracker.put(car.getCarIndex(), distance);
        } else {
            // Keep track of distance travelled and car index, by descending order, we can extract positions
            Double previousDistanceTravelled = distanceTravelledTracker.get(car.getCarIndex());

            // Shallow copy map
            LinkedHashMap<Integer, Double> duplicateComparisonMap = new LinkedHashMap<>(distanceTravelledTracker);
            originalCarPositions = new LinkedList<>(duplicateComparisonMap.keySet());

            // Trigger change
            distanceTravelledTracker.replace(car.getCarIndex(), previousDistanceTravelled + distance);

            // Sort positions
            distanceTravelledTracker = distanceTravelledTracker.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            newCarPositions = new LinkedList<>(distanceTravelledTracker.keySet());
        }

        return GridTracking.builder()
            .contextCar(car)
            .originalCarPositions(originalCarPositions)
            .newCarPositions(newCarPositions)
            .build();
    }

    /**
     * Build list of car position status objects for MQTT Publisher
     *
     * @return
     */
    public List<CarStatus> fetchCarStatusPositions() {
        List<CarStatus> carStatuses = new ArrayList<>();

        if (!CollectionUtils.isEmpty(distanceTravelledTracker)) {
            Set<Integer> cars = distanceTravelledTracker.keySet();
            int position = 1;

            for (Integer car : cars) {
                CarStatus currentCarStatusPosition = CarStatus.builder()
                    .carIndex(car)
                    .timestamp(System.currentTimeMillis())
                    .type(POSITION)
                    .value(position++)
                    .build();

                carStatuses.add(currentCarStatusPosition);
            }
        }

        return carStatuses;
    }
}
