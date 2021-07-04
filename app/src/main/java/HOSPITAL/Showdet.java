package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.HomeActivity;
import com.example.covidcare.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Showdet extends AppCompatActivity {

    DatabaseReference ref1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String gmail;
    public String parts1;
    String address,name,city,mail,oxygen,normal,splittedmail,google,date,time;
    TextView normalbed,oxygenbed,updatetime,updatedate;
    Button mod,lout;
    FirebaseAuth fire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbeds);

        normalbed = findViewById(R.id.normalbed);
        oxygenbed = findViewById(R.id.oxygenbed);
        updatedate=findViewById(R.id.updatedate);
        updatetime=findViewById(R.id.updatetime);
        mod = findViewById(R.id.mod10);
        lout = findViewById(R.id.Logout19);
        fire=FirebaseAuth.getInstance();


        google=getIntent().getStringExtra("mailid");
        String[] parts = google.split("(?=@)");
        splittedmail = parts[0];


        ref1=database.getReference("GMAIL OF HOSPITALS");
        ref1.child(splittedmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                address=snapshot.child("Address of My Hospital").getValue(String.class);
                mail = snapshot.child("Gmail of Hospital").getValue(String.class);
                normal = snapshot.child("Total no of Normal Beds").getValue(String.class);
                oxygen = snapshot.child("Total no of Oxygen Beds").getValue(String.class);
                name = snapshot.child("My Hospital Name").getValue(String.class);
                city = snapshot.child("City of MY Hospital").getValue(String.class);
                time=snapshot.child("time").getValue(String.class);
                date=snapshot.child("date").getValue(String.class);

                setdats();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });







        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),DATABEDS.class);
                intent1.putExtra("address",address);
                intent1.putExtra("keyname",city);
                intent1.putExtra("keyname2",name);
                intent1.putExtra("Email",mail);
                startActivity(intent1);
                finish();
            }
        });
        lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fire.signOut();
                Intent logout=new Intent(getApplicationContext(),REGandLOG.class);
                startActivity(logout);
                finish();


            }
        });


    }

    private void setdats() {
        normalbed.setText(normal);
        oxygenbed.setText(oxygen);
        updatetime.setText(time);
        updatedate.setText(date);

    }
}