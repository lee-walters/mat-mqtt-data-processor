package com.mclaren.challenge;

import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.inbound.Location;
import com.mclaren.challenge.model.internal.CarSpeedDistance;
import com.mclaren.challenge.service.CarSpeedService;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CarSpeedServiceTest {

    private CarSpeedService carSpeedService = new CarSpeedService();

    @Test
    public void testUpdateCarStatusSpeed() {
        float lat1 = 52.0699453113489f;
        float long1 = -1.0110300220549107f;
        float lat2 = 52.06396282877499f;
        float long2 = -1.0165441408753395f;

        long timestamp = System.currentTimeMillis();

        CarCoordinate previousCarLocation = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(lat1)
                .longitude(long1)
                .build())
            .timestamp(timestamp)
            .build();

        CarCoordinate currentCarLocation = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(lat2)
                .longitude(long2)
                .build())
            .timestamp(timestamp + 8000)
            .build();

        CarSpeedDistance carSpeedDistance = carSpeedService.updateCarStatusSpeed(currentCarLocation, previousCarLocation);
        assertThat(carSpeedDistance.getDistance(), is(equalTo(764.8520892094028)));
        assertThat(carSpeedDistance.getCarStatus().getValue(), is(equalTo(213)));
    }
}
