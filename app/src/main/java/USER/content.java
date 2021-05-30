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

    String names,age,phone,address,email;
    String hospita;
    String hospaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        name=findViewById(R.id.name);
        beds=findViewById(R.id.beds);
        oxygen=findViewById(R.id.oxygen);
        book=findViewById(R.id.book);
        hospita=getIntent().getStringExtra("hosp");
        DocumentReference ref= FirebaseFirestore.getInstance().collection(getIntent().
                getStringExtra("city")).document(hospita);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("Address of My Hospital") );
                    hospaddress=documentSnapshot.getString("Address of My Hospital");
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
        DocumentReference ref1=FirebaseFirestore.getInstance().collection("user").document(patient);

        ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                names=documentSnapshot.getString("name");
                 age=documentSnapshot.getString("age");
                 email=documentSnapshot.getString("email");
                phone=documentSnapshot.getString("phone");
                address=documentSnapshot.getString("address");

            }
        });
        String mEmail =hospital;
        String  mSubject = "Booking Notification";
        String mMessage = "A seat has been booked in your hospital\n"+"Name:"+names+"\nAge:"+age+"\nPhone Number:"+phone+"\nEmail:"+email+"\nAddress:"+address;


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();

        String mEmail1 = patient;
        mSubject = "Booking request";
        mMessage = "A seat has been booked in"+hospita+"hospital\n"+"Address:"+hospaddress+"\n show this email in the hospital in order to claim your seats";

        JavaMailAPI javaMailAPI1 = new JavaMailAPI(this, mEmail1, mSubject, mMessage);

        javaMailAPI1.execute();
    }
}