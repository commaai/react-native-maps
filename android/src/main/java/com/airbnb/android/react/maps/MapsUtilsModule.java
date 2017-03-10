package com.airbnb.android.react.maps;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.util.ArrayList;
import java.util.List;

public class MapsUtilsModule extends ReactContextBaseJavaModule {
	public MapsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
    	return "MapsUtilsModule";
    }

    private List<LatLng> polylineCoordsToLatLngList(ReadableArray polylineCoords) {
    	ArrayList<LatLng> list = new ArrayList<LatLng>();

    	for(int i = 0; i < polylineCoords.size(); i++) {
    		ReadableArray coord = polylineCoords.getArray(i);

    		list.add(new LatLng(coord.getDouble(0), coord.getDouble(1)));
    	}

    	return list;
    }

    private LatLng latlngFromReadableArray(ReadableArray coordinate) {
        return new LatLng(coordinate.getDouble(0), coordinate.getDouble(1)); 
    }

    @ReactMethod
    public void indexOfLocationOnPolyline(ReadableArray coordinate, ReadableArray polylineCoords, double toleranceMeters, Callback callback) {
    	List<LatLng> polylineCoordsList = this.polylineCoordsToLatLngList(polylineCoords);

    	int index = PolyUtils.locationIndexOnEdgeOrPath(new LatLng(coordinate.getDouble(0), coordinate.getDouble(1)),
												 polylineCoordsList,
				                                 false,
    											 false,
    											 toleranceMeters);

    	callback.invoke(index);
    }

    @ReactMethod
    public void computeOffset(ReadableArray from, double distance, double heading, Promise promise) {
       LatLng fromCoord = this.latlngFromReadableArray(from);
       LatLng offsetCoord = SphericalUtil.computeOffset(fromCoord, distance, heading);
       WritableNativeArray coords = new WritableNativeArray();
       coords.pushDouble(offsetCoord.latitude);
       coords.pushDouble(offsetCoord.longitude);

       promise.resolve(coords);
     }
	/**
	 * Returns haversine(angle-in-radians).
	 * hav(x) == (1 - cos(x)) / 2 == sin(x / 2)^2.
	 */

}
