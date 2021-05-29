package HOSPITAL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.covidcare.HomeActivity;
import com.example.covidcare.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FinalView extends AppCompatActivity {

    TextView nor13,oxy13;
    Button mod123,logout124;
    String mal34,gen34,gmail09;
    DatabaseReference ref12;
    FirebaseDatabase db12 =  FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_view);

        nor13 = findViewById(R.id.noofnorbeds14);
        oxy13 = findViewById(R.id.noofoxybeds26);
        mod123 = findViewById(R.id.mod10);
        logout124 = findViewById(R.id.Logout19);
        String dress = getIntent().getStringExtra("nam3");
        String hospital = getIntent().getStringExtra("nam4");
        String citt = getIntent().getStringExtra("nam5");
        mal34 = getIntent().getStringExtra("nam1");
        gen34 = getIntent().getStringExtra("nam2");
        gmail09 = getIntent().getStringExtra("nam6");

        Map<String, Object> data = new HashMap<>();
        data.put("City of MY Hospital",citt);
        data.put("My Hospital Name",hospital);
        data.put("Address of My Hospital",dress);
        data.put("Total no of Normal Beds",mal34);
        data.put("Total no of Oxygen Beds",gen34);

        ref12 = db12.getReference("GMAIL OF HOSPITALS");
        ref12.child(gmail09).setValue(data);

        nor13.setText(mal34);
        oxy13.setText(gen34);


        mod123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent12 = new Intent(getApplicationContext(),Modify.class);
                intent12.putExtra("hospmail",gmail09);
                intent12.putExtra("hospname234",hospital);
                intent12.putExtra("cityname1",citt);
                startActivity(intent12);
                finish();
            }
        });

        logout124.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent21 = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent21);
                finish();

            }
        });

    }
}