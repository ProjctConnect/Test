package USER;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.covidcare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        address=getIntent().getStringExtra("address1");
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map =googleMap;
        String location=address;
        List<Address> addressList=null;
        Geocoder geocoder=new Geocoder(Map.this);
        try {
            addressList=geocoder.getFromLocationName(location,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address=addressList.get(0);
        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng).title(location));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

}