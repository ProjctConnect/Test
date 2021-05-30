package USER;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class content extends AppCompatActivity {
    TextView name,beds,oxygen;
    Button book;
    String hospital;
    String patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        name=findViewById(R.id.name);
        beds=findViewById(R.id.beds);
        oxygen=findViewById(R.id.oxygen);
        book=findViewById(R.id.book);
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
                    hospital=documentSnapshot.getString("Gmail of Hospital");
                }
            }
        });
        patient=getIntent().getStringExtra("gmail");

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senEmail();
                Toast.makeText(content.this, "Confirmation E-Mail has been sent", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void senEmail() {
        String mEmail =hospital;
        String  mSubject = "Booking Notification";
        String mMessage = "hello";


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();

        String mEmail1 = patient;
        mSubject = "Booking request";
        mMessage = "Book";

        JavaMailAPI javaMailAPI1 = new JavaMailAPI(this, mEmail1, mSubject, mMessage);

        javaMailAPI1.execute();
    }
}