package HOSPITAL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DATABEDS extends AppCompatActivity {
     Button save;
     String parts1;
     String address;
    DatabaseReference ref1;
     EditText norbed,oxybed;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
     DocumentReference doc;
     public String mailid;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_a_t_a_b_e_d_s);
        save = findViewById(R.id.sav1);
        norbed = findViewById(R.id.norbed123);
        oxybed = findViewById(R.id.oxybed123);
        address=getIntent().getStringExtra("address");
        String city = getIntent().getStringExtra("keyname");
        String hosp = getIntent().getStringExtra("keyname2");
        Date currentTime = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm.ss aa");
        String time = dateFormat.format(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String date = df.format(c.getTime());
        Map<String, Object> data = new HashMap<>();

        mailid=getIntent().getStringExtra("Email");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nor = norbed.getText().toString();
                String oxy = oxybed.getText().toString();
                if (nor.isEmpty()){
                    norbed.setError("Enter The Normal Bed");
                    return;
                }
                if (oxy.isEmpty()){
                    oxybed.setError("Enter The Oxygen Bed");
                    return;
                }
                data.put("City of MY Hospital",city);
                data.put("My Hospital Name",hosp);
                data.put("Address of My Hospital",address);
                data.put("Total no of Normal Beds",nor);
                data.put("Total no of Oxygen Beds",oxy);
                data.put("Gmail of Hospital",mailid);
                data.put("time",time);
                data.put("date",date);

                String[] parts = mailid.split("(?=@)");
                parts1 = parts[0];
                ref1 = database.getReference("GMAIL OF HOSPITALS");
                ref1.child(parts1).setValue(data);


                FirebaseFirestore mfire = FirebaseFirestore.getInstance();
                mfire.collection(city).document(hosp).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DATABEDS.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HospitalNavigationActivity.class);


                        intent.putExtra("mailid",mailid);
                        startActivity(intent);
                        finish();
                    }
                });

            }


        });


    }

}
