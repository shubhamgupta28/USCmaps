package usc.com.uscmaps.example1.shubham.uscmaps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;


//    }

/**
 * Created by Shubham on 2/18/17.
 *
 * https://gist.github.com/quydm/a458d908c4da2496672f83372304f417... by following this
 *
 *
 * Basic methodlogy and all the meanings of the Google Maps stuff -
 * http://blog.teamtreehouse.com/beginners-guide-location-android
 *
 *
 * Removed Kundal ka GetNewInstance() waala method..didnt knew the use of it
 */


public class Tab2Map extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener{

    private Address searchAddress;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    Context context;
    String strtext;
    BroadcastReceiver br;
    String destinationFromSearch;
    String buildingNameFromSearch;
    boolean onReceiveCalled = false;
    public static final String TAG = Tab2Map.class.getSimpleName();

//    public static Tab2Map newInstance() {
//
//        Bundle args = new Bundle();
//
//        Tab2Map fragment = new Tab2Map();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);



        setHasOptionsMenu(true);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "Inside onRecieve Broadcast checkk");
                String destination = intent.getStringExtra("message");
                String buildingName = intent.getStringExtra("buildingName");
//                Toast.makeText(getActivity(), destination, Toast.LENGTH_SHORT).show();
                destinationFromSearch = destination;
                buildingNameFromSearch = buildingName;
                onReceiveCalled = true;
                call(destinationFromSearch, onReceiveCalled);
            }
        };
        getContext().registerReceiver(br, new IntentFilter("com.broadcast.searchQuery"));
    }

    public void call(String destinationFromSearch, boolean onReceiveCalled) {
        Log.e(TAG, destinationFromSearch + " - " + onReceiveCalled);

        if (onReceiveCalled) {
            searchBuilding(destinationFromSearch);
        }

    }

    private void setUpMap() {
        if (mGoogleMap == null)
            mMapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                }
            });
    }

    public void searchBuilding(String locationAddress) {
//        Log.e(TAG, "Inside searchBuilding");
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String result = null;
        try {
            List addressList = geocoder.getFromLocationName(locationAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                searchAddress = (Address) addressList.get(0);
                StringBuilder sb = new StringBuilder();
                sb.append(searchAddress.getLatitude()).append("\n");
                sb.append(searchAddress.getLongitude()).append("\n");
                result = sb.toString();
                LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());

                mGoogleMap.clear();
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(buildingNameFromSearch)
                        .snippet("Click to start Naviagtion")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
                marker.showInfoWindow();

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                mGoogleMap.setOnInfoWindowClickListener(this);
//                mGoogleMap.setInfoWindowAdapter(this);
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.getUiSettings().setCompassEnabled(true);

            }
        } catch (IOException e) {
            Log.e(TAG, "Unaable tooooo connect to Geocoder", e);
        } finally {
            if (result != null) {

                result = "Address: " + locationAddress +
                        "\n\nLatitude and Longitude :\n" + result;
                Log.i(TAG, "Address: " + result);
            } else {
                result = "Address: " + locationAddress +
                        "\n Unable to get Latitude and Longitude for this address location.";
                Log.i(TAG, "Address: " + result);
                mGoogleMap.clear();
            }
        }
    }


    private void markNewLocation(Location location) {
//        Log.e(TAG, "Inside handleLocation: ");

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You are here!")
        );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        marker.showInfoWindow();
    }

    public void fetchLocation(){
//        Log.e(TAG, "Inside FetchLocation");

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location not granted");
        }
        else{
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.e(TAG, "In FetchLocation"+location.toString());
            if (location == null) {
                Log.e(TAG, "Location null");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else {
//                Log.e(TAG, "Location not Null, calling markNewLocation");
                markNewLocation(location);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        return rootView;
    }
    @Override
    public void onResume() {
//        Log.d(TAG, "onResume");

        super.onResume();
        // To hide the keyboard when the user comes back from Google Maps Activity
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("gh", "Permission check required");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.e(TAG, "Rationale");
//                        showMessageOKCancel("You need to allow access to Camera");

            } else {
                Log.e(TAG, "New2");
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // FOR older versions, permission is already granted
        else {
            //DO WHATEVER YOU WANT WITH GOOGLEMAP
            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permission problem... returning");
                return;
            }
            mGoogleApiClient.connect();
            mMapView.onResume();
            setUpMap();
        }

    }

    @Override
    public void onPause() {
//        Log.d(TAG, "onPause");

        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
//        Log.d(TAG, "onDestroy");

        super.onDestroy();
        mGoogleMap.clear();
        mMapView.onDestroy();
        getContext().unregisterReceiver(br);


    }

    @Override
    public void onLowMemory() {
//        Log.d(TAG, "onLowMemory");

        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Log.e(TAG, "In onConnected");

        if (onReceiveCalled) {
            searchBuilding(destinationFromSearch);
        }else{
            fetchLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
//        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.d(TAG, "onConnectionFailed");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), 1);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.d(TAG, "onLocationChanged");
        markNewLocation(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.e(TAG, "In onRequestPermissionsResult");
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e(TAG, "In if");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.e(TAG, "In else");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    /**
     * to listen to click events on an info window
     * https://developers.google.com/maps/documentation/android-api/infowindows
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "Info window clicked, Opening Navigation", Toast.LENGTH_SHORT).show();

        String queryString= null;
        try {
            queryString = URLEncoder.encode(destinationFromSearch, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "queryString: "+queryString);
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+queryString+"&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
