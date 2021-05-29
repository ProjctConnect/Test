package USER;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class content extends AppCompatActivity {
    TextView name,beds,oxygen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        name=findViewById(R.id.name);
        beds=findViewById(R.id.beds);
        oxygen=findViewById(R.id.oxygen);
        DocumentReference ref= FirebaseFirestore.getInstance().collection(getIntent().
                getStringExtra("city")).document(getIntent().getStringExtra("hosp"));
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               if(documentSnapshot.exists()){
                   name.setText(documentSnapshot.getString("Address of My Hospital") );
                   //+" NORMAL BEDS/n "+documentSnapshot.getString("Total no of Normal Beds")+"OXYGEN CYLINDER \n   "+documentSnapshot.getString("Total no of Oxygen Beds")
               beds.setText(documentSnapshot.getString("Total no of Normal Beds"));
               oxygen.setText(documentSnapshot.getString("Total no of Oxygen Beds"));
               }
            }
        });

    }

}