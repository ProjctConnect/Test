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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Finaldata extends AppCompatActivity {

    TextView noofN1,noofO1;
    DatabaseReference ref2;

    String googleMail;
    Button mod1,lout1;
    GoogleApiClient gac1;
    String parts11;
    String tty,losptal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaldata);
        noofN1 = findViewById(R.id.noofnorbeds14);
        noofO1 = findViewById(R.id.noofoxybeds26);
        mod1 = findViewById(R.id.mod10);
        lout1 = findViewById(R.id.Logout19);
        googleMail = getIntent().getStringExtra("GsignMail");
        String[] parts = googleMail.split("(?=@)");
        parts11 = parts[0];
        FirebaseDatabase db1 = FirebaseDatabase.getInstance();
        ref2 = db1.getReference("GMAIL OF HOSPITALS");
        ref2.child(parts11).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String add1 = snapshot.child("Address of My Hospital").getValue(String.class);
                String norm1 = snapshot.child("Total no of Normal Beds").getValue(String.class);
                String oxy2 = snapshot.child("Total no of Oxygen Beds").getValue(String.class);
                losptal = snapshot.child("My Hospital Name").getValue(String.class);
                tty = snapshot.child("City of MY Hospital").getValue(String.class);

                noofN1.setText(norm1);
                noofO1.setText(oxy2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mod1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),Modify.class);
                intent1.putExtra("hospmail",parts11);
                intent1.putExtra("cityname1",tty);
                intent1.putExtra("hospname234",losptal);
                startActivity(intent1);
                finish();
            }
        });
        lout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(gac1).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()){
                            Intent intent2 =new Intent(getApplicationContext(), HomeActivity.class);

                            startActivity(intent2);
                            finish();
                        }else {
                            Toast.makeText(Finaldata.this, "LogOut Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}