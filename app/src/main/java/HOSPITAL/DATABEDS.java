package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DATABEDS extends AppCompatActivity {
     Button save;
     EditText add,norbed,oxybed;

     DocumentReference doc;
     public String personEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_a_t_a_b_e_d_s);
        save = findViewById(R.id.sav);
        add = findViewById(R.id.dress);
        norbed = findViewById(R.id.normalbed);
        oxybed = findViewById(R.id.oxybed);



        String city = getIntent().getStringExtra("keyname");
        String hosp = getIntent().getStringExtra("keyname2");
        String mal = getIntent().getStringExtra("Email");
        Map<String, Object> data = new HashMap<>();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ad = add.getText().toString();
                String nor = norbed.getText().toString();
                String oxy = oxybed.getText().toString();
                data.put("Address of My Hospital",ad);
                data.put("Total no of Normal Beds",nor);
                data.put("Total no of Oxygen Beds",oxy);


                FirebaseFirestore mfire = FirebaseFirestore.getInstance();
                mfire.collection(city).document(hosp).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DATABEDS.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

}
