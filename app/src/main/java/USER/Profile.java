package USER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covidcare.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    EditText username,userage,usernumber,useraddress,usermail;
    String name,number,age,address,mail;
    Button save;
    String gmail;
    ImageView profilephoto;
    ProgressBar progressBar;
    DocumentReference documentReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username=findViewById(R.id.username);
        userage=findViewById(R.id.userage);
        usernumber=findViewById(R.id.usernumber);
        useraddress=findViewById(R.id.useraddress);
        usermail=findViewById(R.id.usermail);
        save=findViewById(R.id.profile);
        progressBar = (ProgressBar)findViewById(R.id.progressbar1);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        profilephoto=findViewById(R.id.profilephoto);
        storageReference=FirebaseStorage.getInstance().getReference();


        gmail=getIntent().getStringExtra("gmail");
        usermail.setText(gmail);
        StorageReference profile=storageReference.child(gmail+"/profile.jpg");






        save.setOnClickListener(new View.OnClickListener() {
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
                userprofile.put("email",mail);
                userprofile.put("phone",number);
                userprofile.put("address",address);
                documentReference= FirebaseFirestore.getInstance().collection("user").document(gmail);
                documentReference.set(userprofile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(Profile.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                        intent.putExtra("gmailid",gmail);
                        intent.putExtra("name",name);
                        startActivity(intent);


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
        final  StorageReference file=storageReference.child(gmail+"/profile.jpg");
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
                Toast.makeText(Profile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}