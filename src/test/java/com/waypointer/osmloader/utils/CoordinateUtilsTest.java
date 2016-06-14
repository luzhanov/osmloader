package com.waypointer.osmloader.utils;

import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class CoordinateUtilsTest {

    //todo: make test for polygon in + and - area

    @Test
    public void testCalculateCentroid1() throws Exception {
        Coordinate result = CoordinateUtils.calculateCentroid(Arrays.asList(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 1.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 0.0)
        ));

        assertThat(result.getLatitude()).isEqualTo(0.5);
        assertThat(result.getLongitude()).isEqualTo(0.5);
    }

    @Test
    public void testCalculateCentroid2() throws Exception {
        Coordinate result = CoordinateUtils.calculateCentroid(Arrays.asList(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, -2.0),
                new Coordinate(-2.0, -2.0),
                new Coordinate(-2.0, 0.0)
        ));

        assertThat(result.getLatitude()).isEqualTo(-1.0);
        assertThat(result.getLongitude()).isEqualTo(-1.0);
    }

    @Test
    public void testCalculateArea1() throws Exception {
        double result = CoordinateUtils.calculateArea(Arrays.asList(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 1.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 0.0)
        ));

        assertThat(result).isEqualTo(1.0);
    }

    @Test
    public void testCalculateCentroid3() throws Exception {
        Coordinate result = CoordinateUtils.calculateCentroid(Arrays.asList(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 0.5),
                new Coordinate(0.0, 1.0),
                new Coordinate(0.5, 1.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 0.0)
        ));

        assertThat(result.getLatitude()).isEqualTo(0.5);
        assertThat(result.getLongitude()).isEqualTo(0.5);
    }

    @Test
    public void testCalculateCentroidReal() throws Exception {
        Coordinate result = CoordinateUtils.calculateCentroid(Arrays.asList(
                new Coordinate(48.793, 27.1),
                new Coordinate(48.792, 27.096),
                new Coordinate(48.7925, 27.085),
                new Coordinate(48.7931, 27.101)
        ));

        assertThat(result.getLatitude()).isEqualTo(48.793, Offset.offset(0.001));
        assertThat(result.getLongitude()).isEqualTo(27.094, Offset.offset(0.001));
    }

    @Test
    public void testCalculateCentroidRealNegative() throws Exception {
        Coordinate result = CoordinateUtils.calculateCentroid(Arrays.asList(
                new Coordinate(-48.793, 27.1),
                new Coordinate(-48.792, 27.096),
                new Coordinate(-48.7925, 27.085),
                new Coordinate(-48.7931, 27.101)
        ));

        assertThat(result.getLatitude()).isEqualTo(-48.793, Offset.offset(0.001));
        assertThat(result.getLongitude()).isEqualTo(27.094, Offset.offset(0.001));
    }

    @Test
    public void testCalculateCentroidRealNegativeAll() throws Exception {
        Coordinate result = CoordinateUtils.calculateCentroid(Arrays.asList(
                new Coordinate(-48.793, -27.1),
                new Coordinate(-48.792, -27.096),
                new Coordinate(-48.7925, -27.085),
                new Coordinate(-48.7931, -27.101)
        ));

        assertThat(result.getLatitude()).isEqualTo(-48.793, Offset.offset(0.001));
        assertThat(result.getLongitude()).isEqualTo(-27.094, Offset.offset(0.001));
    }

    @Test
    public void testCalculateArea2() throws Exception {
        double result = CoordinateUtils.calculateArea(Arrays.asList(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, -2.0),
                new Coordinate(-2.0, -2.0),
                new Coordinate(-2.0, 0.0)
        ));

        assertThat(result).isEqualTo(4.0);
    }
}