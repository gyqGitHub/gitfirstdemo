package com.example.baidumaptest;

import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private BMapManager manager;
	private MapView mapView;
	private LocationManager locationManager;
	private String provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = new BMapManager(this);
		manager.init("B1UF29GMCGWgwL3oq3W1IDBQeUrlr0bp", null);
		setContentView(R.layout.activity_main);
		mapView = (MapView)findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList = locationManager.getProviders(true);
		if(providerList.contains(LocationManager.GPS_PROVIDER)){
			provider = LocationManager.GPS_PROVIDER;
		}else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
			provider = LocationManager.NETWORK_PROVIDER;
		}else{
			Toast.makeText(MainActivity.this, "No location provider to use", Toast.LENGTH_SHORT).show();
			return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if(location != null){
			navigateTo(location);
		}
	
	}

	private void navigateTo(Location location) {
		// TODO Auto-generated method stub
		MapController controller = mapView.getController();
		controller.setZoom(16);
		GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6),(int)(location.getLongitude() * 1E6));
		controller.setCenter(point);
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mapView);
		LocationData locationData = new LocationData();
		locationData.latitude = location.getLatitude();
		locationData.longitude = location.getLongitude();
		myLocationOverlay.setData(locationData);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.refresh();
		
	}

	@Override
	protected void onResume(){
		mapView.onResume();
		if(manager != null){
			manager.start();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		mapView.onPause();
		if(manager != null){
			manager.stop();
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy(){
		mapView.destroy();
		if(manager !=null){
			manager.destroy();
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
