package USER;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {
    String hospital,city,bed,newbed,gmail,parts11;
    String oxygenbed,newoxybed,newo,newb;
    int i,a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        hospital=getIntent().getStringExtra("hospital");
        city=getIntent().getStringExtra("city");
        gmail=getIntent().getStringExtra("email");
        String[] parts = gmail.split("(?=@)");
        parts11 = parts[0];
        DocumentReference reference= FirebaseFirestore.getInstance().collection(city).document(hospital);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bed=documentSnapshot.getString("Total no of Normal Beds");
                i=Integer.parseInt(bed);
                if (i<=0){
                    i=0;
                }else {
                    i=i-1;
                }
                newbed=String.valueOf(i);
                oxygenbed=documentSnapshot.getString("Total no of Oxygen Beds");
                a=Integer.parseInt(oxygenbed);
                if (a<=0){
                    a=0;
                }else {
                    a=a-1;
                }
                newoxybed=String.valueOf(a);


                Map<String,Object> data=new HashMap<>();
                data.put("Total no of Normal Beds",newbed);
                data.put("Total no of Oxygen Beds",newoxybed);
                reference.update(data);

            }
        });
        newb=newbed;
        newo=newoxybed;

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("GMAIL OF HOSPITALS").child(parts11);
        Map<String,Object> data1=new HashMap<>();
        data1.put("Total no of Normal Beds",newb);
        data1.put("Total no of Oxygen Beds",newo);
        databaseReference.updateChildren(data1);


    }
}