package USER;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covidcare.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    EditText username,userage,usernumber,useraddress,usermail;
    String name,number,age,address,mail;
    ImageView profilephoto;
    Button update;
    StorageReference storageReference;
    DocumentReference documentReference;
    ProgressBar progressBar;
    String gmailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        username=findViewById(R.id.etname);
        userage=findViewById(R.id.etage);
        usernumber=findViewById(R.id.etnumber);
        useraddress=findViewById(R.id.etaddress);
        usermail=findViewById(R.id.etmail);
        profilephoto=findViewById(R.id.etprofile);
        progressBar = (ProgressBar)findViewById(R.id.progressbar2);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        update=findViewById(R.id.updateprofile);
        documentReference= FirebaseFirestore.getInstance().collection("user").document();
        gmailid=getIntent().getStringExtra("gmail");
        usermail.setText(gmailid);
        progressBar.setVisibility(View.VISIBLE);

        storageReference =FirebaseStorage.getInstance().getReference().child(gmailid+"/profile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilephoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
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

        gmailid=getIntent().getStringExtra("gmail");







        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=username.getText().toString();
                number=usernumber.getText().toString();
                age=userage.getText().toString();
                address=useraddress.getText().toString();
                mail=usermail.getText().toString();

                if (name.isEmpty()){
                    username.setError("Enter your Name");
                    return;
                }
                if (number.isEmpty() || number.length()!=10){
                    usernumber.setError("Enter your Mobile Number");
                    return;
                }
                if (age.isEmpty()){
                    userage.setError("Enter your Age");
                    return;
                }
                if (address.isEmpty()){
                    useraddress.setError("Enter Your Address");
                    return;
                }
                if (mail.isEmpty()){
                    usermail.setError("Enter your E-Mail ID");
                    return;
                }
                Map<String, Object> userprofile = new HashMap<>();
                userprofile.put("name",name);
                userprofile.put("age",age);
                userprofile.put("phone",number);
                userprofile.put("address",address);
                userprofile.put("email",mail);
                documentReference= FirebaseFirestore.getInstance().collection("user").document(gmailid);
                documentReference.set(userprofile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(UpdateProfile.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                        intent.putExtra("gmailid",gmailid);
                        intent.putExtra("name",name);
                        startActivity(intent);
                        finish();


                    }
                });

            }

        });

        profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,1000);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                Uri imageuri=data.getData();
                //profilephoto.setImageURI(imageuri);
                uploaddatatofirebase(imageuri);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void uploaddatatofirebase(Uri imageuri) {
        final  StorageReference file=storageReference;
        file.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilephoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(UpdateProfile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


