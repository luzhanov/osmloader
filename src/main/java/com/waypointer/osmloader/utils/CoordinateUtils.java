package com.waypointer.osmloader.utils;

import java.util.List;

/**
 * @author Igor Luzhanov, 10.01.2015
 */
public class CoordinateUtils {

    public static Coordinate calculateCentroid(List<Coordinate> boundingPoints) {
        double xLon = 0;
        double yLat = 0;

        int pointSize = boundingPoints.size();
        int jVar = pointSize - 1;
        for (int i = 0; i < pointSize; jVar = i, i++) {
            Coordinate point1 = boundingPoints.get(i);
            Coordinate point2 = boundingPoints.get(jVar);

            double fVar = point1.getLongitude() * point2.getLatitude() - point2.getLongitude() * point1.getLatitude();
            xLon += (point1.getLongitude() + point2.getLongitude()) * fVar;
            yLat += (point1.getLatitude() + point2.getLatitude()) * fVar;
        }

        double f2 = calculateAreaForCentroid(boundingPoints) * 6;

        return new Coordinate(yLat / f2, xLon / f2);
    }

    private static double calculateAreaForCentroid(List<Coordinate> boundingPoints) {
        double area = 0;

        int pointSize = boundingPoints.size();
        int jVar = pointSize - 1;
        for (int i = 0; i < pointSize; i++) {
            Coordinate point1 = boundingPoints.get(i);
            Coordinate point2 = boundingPoints.get(jVar);
            area += point1.getLongitude() * point2.getLatitude();
            area -= point1.getLatitude() * point2.getLongitude();
            jVar = i;
        }

        area = area / 2;
        return area;
    }

    public static double calculateArea(List<Coordinate> boundingPoints) {
        int pointSize = boundingPoints.size();

        double sumPre = 0;

        for (int i = 0; i < (pointSize - 1); i++) {
            double xi = boundingPoints.get(i).getLongitude();
            double yi = boundingPoints.get(i).getLatitude();
            double xi1 = boundingPoints.get(i + 1).getLongitude();
            double yi1 = boundingPoints.get(i + 1).getLatitude();

            sumPre += xi * yi1 + yi * xi1;
        }
        double x0 = boundingPoints.get(0).getLongitude();
        double y0 = boundingPoints.get(0).getLatitude();
        double xN = boundingPoints.get(pointSize - 1).getLongitude();
        double yN = boundingPoints.get(pointSize - 1).getLatitude();

        sumPre += xN * y0 + yN * x0;

        return Math.abs(sumPre) / 2.0;
    }


}
