package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class confirmation2 extends AppCompatActivity {
    String hospital, city, bed, newbed, gmail, parts11;
    String oxygenbed, newoxybed, newoxygen, newnormal;
    int i, a, oxy, norm;
    TextView oxygen, normal;
    DatabaseReference databaseReference;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation2);
        hospital = getIntent().getStringExtra("hospital");
        city = getIntent().getStringExtra("city");
        gmail = getIntent().getStringExtra("email");
        String[] parts = gmail.split("(?=@)");
        parts11 = parts[0];
        databaseReference= FirebaseDatabase.getInstance().getReference("GMAIL OF HOSPITALS").child(parts11);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newoxygen= snapshot.child("Total no of Oxygen Beds").getValue(String.class);
                oxy=Integer.parseInt(newoxygen);
                newnormal=String.valueOf(norm);
                if (oxy<=0){
                    oxy=0;
                }else {
                    oxy=oxy-1;
                }

                newoxybed=String.valueOf(oxy);
                snapshot.getRef().child("Total no of Oxygen Beds").setValue(newoxybed);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference= FirebaseFirestore.getInstance().collection(city).document(hospital);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                oxygenbed = documentSnapshot.getString("Total no of Oxygen Beds");
                a = Integer.parseInt(oxygenbed);
                if (a <= 0) {
                    a = 0;
                } else {
                    a = a - 1;
                }
                newoxybed = String.valueOf(a);


                Map<String, Object> data = new HashMap<>();

                data.put("Total no of Oxygen Beds", newoxybed);
                reference.update(data);

            }
        });

    }
}