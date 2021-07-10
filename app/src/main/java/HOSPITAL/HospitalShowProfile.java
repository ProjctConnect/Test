package HOSPITAL;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HospitalShowProfile extends AppCompatActivity {
    TextView username,usernumber,useraddress,usermail;
    ImageView profile;
    StorageReference storageReference;
    DocumentReference documentReference;
    String gmailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_show_profile);
        username=findViewById(R.id.hname);
        usernumber=findViewById(R.id.hnumber);
        useraddress=findViewById(R.id.haddress);
        usermail=findViewById(R.id.hmail);
        profile=findViewById(R.id.hprofile);
        gmailid=getIntent().getStringExtra("gmail");
        documentReference= FirebaseFirestore.getInstance().collection("hospital").document(gmailid);

        storageReference =FirebaseStorage.getInstance().getReference().child(gmailid+"/hospitalprofile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile);
            }
        });

        documentReference=FirebaseFirestore.getInstance().collection("hospital").document(gmailid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("name");
                String number=documentSnapshot.getString("phone");
                String address=documentSnapshot.getString("address");
                String mail=documentSnapshot.getString("email");
                username.setText(name);
                usernumber.setText(number);
                useraddress.setText(address);
                usermail.setText(mail);

            }
        });
    }


}