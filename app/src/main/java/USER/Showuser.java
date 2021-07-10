package USER;

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

public class Showuser extends AppCompatActivity {
    TextView username,userage,usernumber,useraddress,usermail;
    ImageView profile;
    StorageReference storageReference;
    DocumentReference documentReference;
    String gmailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showuser);
        username=findViewById(R.id.tvname);
        userage=findViewById(R.id.tvage);
        usernumber=findViewById(R.id.tvnumber);
        useraddress=findViewById(R.id.tvaddress);
        usermail=findViewById(R.id.tvmail);
        profile=findViewById(R.id.tvprofile);
        documentReference= FirebaseFirestore.getInstance().collection("user").document();
        gmailid=getIntent().getStringExtra("gmail");

        storageReference =FirebaseStorage.getInstance().getReference().child(gmailid+"/profile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile);
            }
        });

        documentReference=FirebaseFirestore.getInstance().collection("user").document(gmailid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("name");
                String age=documentSnapshot.getString("age");
                String number=documentSnapshot.getString("phone");
                String address=documentSnapshot.getString("address");
                String mail=documentSnapshot.getString("email");
                username.setText(name);
                userage.setText(age);
                usernumber.setText(number);
                useraddress.setText(address);
                usermail.setText(mail);

            }
        });
    }


}