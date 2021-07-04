package HOSPITAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HOSPITALDETAILS extends AppCompatActivity implements OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener, AdapterView.OnItemSelectedListener {

    public static int countryID, stateID;
    private Button pickStateButton, pickCountry, pickCity;
    private TextView stateNameTextView, countryName,countryCode,countryPhoneCode,countryCurrency,cityName;
    private ImageView flagImage;
    // Pickers
    private CountryPicker countryPicker;
    private StatePicker statePicker;
    private CityPicker cityPicker;
    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;
    CardView adddata;
    EditText hospname, address;
    DatabaseReference ref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public String citynamenext;
    public String gmail,password;
    public String parts1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_o_s_p_i_t_a_l_d_e_t_a_i_l_s);


        adddata = findViewById(R.id.addata);
        hospname = findViewById(R.id.hospname);

        countryName = findViewById(R.id.countryNameTextView);
        stateNameTextView = findViewById(R.id.state_name);
        cityName = (TextView) findViewById(R.id.city_name);
        ref=database.getReference("HOSPITAL DETAILS");
        gmail=getIntent().getStringExtra("gmail");
        password=getIntent().getStringExtra("password");

        // initialize view
        initView();
        // get state from assets JSON
        try {
            getStateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // get City from assets JSON
        try {
            getCityJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // initialize country picker
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // initialize listeners
        setListener();
        setCountryListener();
        setCityListener();

        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savevalue(countryName.getText().toString(),stateNameTextView.getText().toString(),cityName.getText().toString());
            }
        });

    }

    private void savevalue(String toString, String toString1, String toString2) {
        String hop = hospname.getText().toString();
        if (toString.isEmpty()){
            countryName.setError("Please Select Country");
            return;
        }
        if (toString1.isEmpty()){
            stateNameTextView.setError("Please Select State");
            return;
        }
        if (toString2.isEmpty()){
            cityName.setError("Please Select City");
            return;
        }
        if (hop.isEmpty()){
            hospname.setError("Please Enter Hospital Name");
            return;
        }




        String id = ref.push().getKey();
        ref.child(toString).child(toString1).child(toString2).child(id).setValue(hop);
        Toast.makeText(this, "value saved", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),HospitalProfile.class);
        intent.putExtra("keyname",toString2);
        intent.putExtra("keyname2",hop);
        intent.putExtra("Email",gmail);
        intent.putExtra("password",password);
        startActivity(intent);

    }


// INIT VIEWS

    public void initView(){
        //Buttons
        pickStateButton = (Button) findViewById(R.id.pickState);
        //set state picker invisible
        pickStateButton.setVisibility(View.INVISIBLE);
        pickCountry = (Button) findViewById(R.id.pickCountry);
        pickCity = (Button) findViewById(R.id.pick_city);
        // set city picker invisible
        pickCity.setVisibility(View.INVISIBLE);
        // Text Views
        countryName = (TextView) findViewById(R.id.countryNameTextView);

        stateNameTextView = (TextView) findViewById(R.id.state_name);
        //set state name text view invisible
        stateNameTextView.setVisibility(View.INVISIBLE);
        cityName = (TextView) findViewById(R.id.city_name);
        //set state name text view invisible
        cityName.setVisibility(View.INVISIBLE);

        // ImageView
        flagImage = (ImageView) findViewById(R.id.flag_image);

        // initiate state object, parser, and arrays
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();
    }

    // SET STATE LISTENER
    private void setListener() {
        pickStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    //SET COUNTRY LISTENER
    private void setCountryListener() {
        pickCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    //SET CITY LISTENER
    private void setCityListener() {
        pickCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    // ON SELECTED COUNTRY ADD STATES TO PICKER
    @Override
    public void onSelectCountry(Country country) {
        // get country name and country ID
        countryName.setText(country.getName());
        countryID = country.getCountryId();
        statePicker.equalStateObject.clear();
        cityPicker.equalCityObject.clear();

        //set state name text view and state pick button invisible
        pickStateButton.setVisibility(View.VISIBLE);
        stateNameTextView.setVisibility(View.VISIBLE);
        // set text on main view

        flagImage.setBackgroundResource(country.getFlag());


        // GET STATES OF SELECTED COUNTRY
        for(int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {

                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }
    }
    // ON SELECTED STATE ADD CITY TO PICKER
    @Override
    public void onSelectState(State state) {
        pickCity.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
        cityPicker.equalCityObject.clear();

        stateNameTextView.setText(state.getStateName());
        stateID = state.getStateId();



        for(int i = 0; i < cityObject.size(); i++) {
            cityPicker = new CityPicker.Builder().with(this).listener(this).build();
            City cityData = new City();
            if (cityObject.get(i).getStateId() == stateID) {
                cityData.setCityId(cityObject.get(i).getCityId());
                cityData.setCityName(cityObject.get(i).getCityName());
                cityData.setStateId(cityObject.get(i).getStateId());

                cityPicker.equalCityObject.add(cityData);
            }
        }
    }
    // ON SELECTED CITY
    @Override
    public void onSelectCity(City city) {
        cityName.setText(city.getCityName());
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    // GET STATE FROM ASSETS JSON
    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();

            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }
    // GET CITY FROM ASSETS JSON
    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();
            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}