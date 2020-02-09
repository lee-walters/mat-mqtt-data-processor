package com.mclaren.challenge;

import org.junit.Test;

import static com.mclaren.challenge.utils.Utils.calculateDistanceInMeters;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UtilsTest {

    @Test
    public void shouldCalculateDistanceBetweenTwoGpsCoordinates() {
        double lat1 = 52.0699453113489;
        double long1 = -1.0110300220549107;
        double lat2 = 52.06396282877499;
        double long2 = -1.0165441408753395;

        double lengthMetres = calculateDistanceInMeters(lat1, long1, lat2, long2);

        assertThat(format("%.2f", lengthMetres), is(equalTo("764.59")));
    }
}
