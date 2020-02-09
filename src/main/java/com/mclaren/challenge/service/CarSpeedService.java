package com.mclaren.challenge.service;

import com.mclaren.challenge.model.internal.CarSpeedDistance;
import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.inbound.Location;
import com.mclaren.challenge.model.outbound.CarStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.mclaren.challenge.utils.Constants.SPEED;
import static com.mclaren.challenge.utils.Utils.calculateDistanceInMeters;

@Service
public class CarSpeedService {

    /**
     * Calculate current speed of the car based of GPS coordinates
     *
     * @param currentCarLocation
     * @param previousCarLocation
     * @return
     */
    public CarSpeedDistance updateCarStatusSpeed(final CarCoordinate currentCarLocation, final CarCoordinate previousCarLocation) {
        Location currentLocation = currentCarLocation.getLocation();
        Location previousLocation = previousCarLocation.getLocation();

        // Firstly calculate distance between last and current position
        double distance = calculateDistanceInMeters(
            previousLocation.getLatitude(),
            previousLocation.getLongitude(),
            currentLocation.getLatitude(),
            currentLocation.getLongitude());

        // Take the time difference from last recording
        long milliseconds =  new Timestamp(currentCarLocation.getTimestamp()).getTime() - new Timestamp(previousCarLocation.getTimestamp()).getTime();
        float seconds = (float) milliseconds / 1000;

        // Conversion to desired metric
        double kph = ( distance / 1000.0f ) / ( seconds / 3600.0f );
        double mph = kph / 1.609f;

        // Build status object
        CarStatus carStatusSpeed = CarStatus.builder()
            .carIndex(currentCarLocation.getCarIndex())
            .timestamp(currentCarLocation.getTimestamp())
            .type(SPEED)
            .value((int) mph)
            .build();

        return CarSpeedDistance.builder()
                .distance(distance)
                .carStatus(carStatusSpeed)
                .build();
    }
}
