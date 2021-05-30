package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DATABEDS extends AppCompatActivity {
     Button save;
     EditText add,norbed,oxybed;
     DocumentReference doc;
     public String mailid;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_a_t_a_b_e_d_s);
        save = findViewById(R.id.sav1);
        add = findViewById(R.id.dress1);
        norbed = findViewById(R.id.norbed123);
        oxybed = findViewById(R.id.oxybed123);
        String city = getIntent().getStringExtra("keyname");
        String hosp = getIntent().getStringExtra("keyname2");
        String mal = getIntent().getStringExtra("Email");
        Map<String, Object> data = new HashMap<>();
        mailid=getIntent().getStringExtra("Email");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ad = add.getText().toString();
                String nor = norbed.getText().toString();
                String oxy = oxybed.getText().toString();
                data.put("City of MY Hospital",city);
                data.put("My Hospital Name",hosp);
                data.put("Address of My Hospital",ad);
                data.put("Total no of Normal Beds",nor);
                data.put("Total no of Oxygen Beds",oxy);
                data.put("Gmail of Hospital",mailid);


                FirebaseFirestore mfire = FirebaseFirestore.getInstance();
                mfire.collection(city).document(hosp).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DATABEDS.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Showdet.class);
                        intent.putExtra("add",ad);
                        intent.putExtra("norm",nor);
                        intent.putExtra("oxyg",oxy);
                        intent.putExtra("Email",mailid);
                        intent.putExtra("losp",hosp);
                        intent.putExtra("cityy",city);
                        startActivity(intent);
                    }
                });

            }


        });


    }

}
