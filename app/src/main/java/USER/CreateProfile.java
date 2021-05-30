package USER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.Continuation;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class CreateProfile extends AppCompatActivity {


    EditText et_name,et_age,et_bio,et_email,et_website;
    String Gmail;
    Button button;
    public String profile;
    ProgressBar progressBar;
    private Uri imageUri;
    private static final int PICK_IMAGE= 1;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        imageView = findViewById(R.id.imageview_cp);
        et_name = findViewById(R.id.name_et_cp);
        et_age = findViewById(R.id.age_et_cp);
        et_bio = findViewById(R.id.bio_et_cp);
        et_email = findViewById(R.id.email_et_cp);
        et_website = findViewById(R.id.website_et_cp);
        button = findViewById(R.id.save_profile_btn_cp);
        progressBar = findViewById(R.id.progressbar_cp);
        Gmail=getIntent().getStringExtra("gmail");


        documentReference = db.collection("user").document(Gmail);
        storageReference = firebaseStorage.getInstance().getReference("profile images");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadData();

            }
        });

    }

    public void ChooseImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                data != null || data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(imageView);
        }

    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadData(){

        String name = et_name.getText().toString();
        String age= et_age.getText().toString();
        String bio = et_bio.getText().toString();
        String website = et_website.getText().toString();
        String email = et_email.getText().toString();

        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(age) || !TextUtils.isEmpty(bio) ||
                !TextUtils.isEmpty(website) || !TextUtils.isEmpty(email) || imageView != null ){

            progressBar.setVisibility(View.VISIBLE);
            final  StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));

            uploadTask = reference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                Map<String ,String > profile = new HashMap<>();
                                profile.put("name",name);
                                profile.put("age",age);
                                profile.put("bio",bio);
                                profile.put("email",email);
                                profile.put("website",website);
                                profile.put("url",downloadUri.toString());

                                documentReference.set(profile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(CreateProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(CreateProfile.this,NavigationActivity.class);
                                                intent.putExtra("gmailid",Gmail);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CreateProfile.this, "failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else {
            Toast.makeText(this, "All Fields required", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            String name_result = task.getResult().getString("name");
                            String age_result = task.getResult().getString("age");
                            String bio_result = task.getResult().getString("bio");
                            String email_result = task.getResult().getString("email");
                            String web_result = task.getResult().getString("website");
                            String Url = task.getResult().getString("url");


                            Picasso.get().load(Url).into(imageView);
                            et_name.setText(name_result);
                            et_age.setText(age_result);
                            et_bio.setText(bio_result);
                            et_email.setText(email_result);
                            et_website.setText(web_result);


                        }else {
                            Toast.makeText(CreateProfile.this, "No Profile exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}