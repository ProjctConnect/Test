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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Modify extends AppCompatActivity {
     EditText bednor,bedoxy,dress1;
     Button sav1;
     String hosptal,gsigninn,ttyci,googlemail;
     DatabaseReference ref12;
     FirebaseDatabase db12 =  FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        bednor = findViewById(R.id.norbed123);
        bedoxy = findViewById(R.id.oxybed123);
        dress1 = findViewById(R.id.dress1);
        sav1 = findViewById(R.id.sav1);
        hosptal = getIntent().getStringExtra("hospname234");
        gsigninn = getIntent().getStringExtra("hospmail");
        ttyci = getIntent().getStringExtra("cityname1");
        googlemail=getIntent().getStringExtra("gmail1");

        Map<String, Object> data = new HashMap<>();

        sav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ad1 = dress1.getText().toString();
                String nor1 = bednor.getText().toString();
                String oxy1 = bedoxy.getText().toString();
                data.put("City of MY Hospital",ttyci);
                data.put("My Hospital Name",hosptal);
                data.put("Address of My Hospital",ad1);
                data.put("Total no of Normal Beds",nor1);
                data.put("Total no of Oxygen Beds",oxy1);
                data.put("Gmail of Hospital",googlemail);

                FirebaseFirestore mfire1 = FirebaseFirestore.getInstance();
                mfire1.collection(ttyci).document(hosptal).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(Modify.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent int2 = new Intent(getApplicationContext(),FinalView.class);
                        int2.putExtra("nam1",nor1);
                        int2.putExtra("nam2",oxy1);
                        int2.putExtra("nam3",ad1);
                        int2.putExtra("nam4",hosptal);
                        int2.putExtra("nam5",ttyci);
                        int2.putExtra("nam6",gsigninn);
                        int2.putExtra("nam7",googlemail);
                        startActivity(int2);
                        finish();




                    }
                });

            }
        });


    }
}