//package tagbin.in.myapplication;
//
//import android.graphics.Color;
//import android.location.Location;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
///**
// * Created by admin pc on 11-01-2016.
// */
//public class DraggableCircle {
//
//    private final Marker centerMarker;
//
//    private final Marker radiusMarker;
//
//    private final Circle circle;
//    public static final double RADIUS_OF_EARTH_METERS = 6371009;
//
//
//    private double radius;
//    GoogleMap mMap;
//
//    public DraggableCircle(LatLng center, double radius,GoogleMap mMap) {
//        this.radius = radius;
//        this.mMap=mMap;
//        centerMarker = mMap.addMarker(new MarkerOptions()
//                .position(center)
//                .draggable(true));
//        radiusMarker = mMap.addMarker(new MarkerOptions()
//                .position(toRadiusLatLng(center, radius))
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.defaultMarker(
//                        BitmapDescriptorFactory.HUE_AZURE)));
//        circle = mMap.addCircle(new CircleOptions()
//                .center(center)
//                .radius(radius)
//                .strokeWidth(100)
//                .strokeColor(Color.BLUE)
//                .fillColor(Color.BLUE));
//    }
//
//    public DraggableCircle(LatLng center, LatLng radiusLatLng) {
//        this.radius = toRadiusMeters(center, radiusLatLng);
//        centerMarker = mMap.addMarker(new MarkerOptions()
//                .position(center)
//                .draggable(true));
//        radiusMarker = mMap.addMarker(new MarkerOptions()
//                .position(radiusLatLng)
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.defaultMarker(
//                        BitmapDescriptorFactory.HUE_AZURE)));
//        circle = mMap.addCircle(new CircleOptions()
//                .center(center)
//                .radius(radius)
//                .strokeWidth(10)
//                .strokeColor(Color.BLACK));
//    }
//
//    public boolean onMarkerMoved(Marker marker) {
//        if (marker.equals(centerMarker)) {
//            circle.setCenter(marker.getPosition());
//            radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
//            return true;
//        }
//        if (marker.equals(radiusMarker)) {
//            radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
//            circle.setRadius(radius);
//            return true;
//        }
//        return false;
//    }
//
//
//    private static LatLng toRadiusLatLng(LatLng center, double radius) {
//        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
//                Math.cos(Math.toRadians(center.latitude));
//        return new LatLng(center.latitude, center.longitude + radiusAngle);
//    }
//
//    private static double toRadiusMeters(LatLng center, LatLng radius) {
//        float[] result = new float[1];
//        Location.distanceBetween(center.latitude, center.longitude,
//                radius.latitude, radius.longitude, result);
//        return result[0];
//    }
//
//}