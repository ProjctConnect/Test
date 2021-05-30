package USER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UpdateUser extends AppCompatActivity {

    EditText et_name,et_age,et_bio,et_email,et_website;
    Button button;
    ProgressBar progressBar;
    String gmailid;
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
        setContentView(R.layout.activity_update_user);


        imageView = findViewById(R.id.imageview1);
        gmailid=getIntent().getStringExtra("gmail");
        et_name = findViewById(R.id.edittext1);
        et_age = findViewById(R.id.edittext2);
        et_bio = findViewById(R.id.edittext3);
        et_email = findViewById(R.id.edittext4);
        et_website = findViewById(R.id.edittext5);
        button = findViewById(R.id.savebutton);
        progressBar = findViewById(R.id.progressbar);

        documentReference = db.collection("user").document(gmailid);
        storageReference = firebaseStorage.getInstance().getReference("profile images");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Updateprofile();
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

    private void Updateprofile(){
        String name = et_name.getText().toString();
        String age= et_age.getText().toString();
        String bio = et_bio.getText().toString();
        String website = et_website.getText().toString();
        String email = et_email.getText().toString();

        if (imageUri != null){
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

                                final DocumentReference sfDocRef = db.collection("user").document(gmailid);

                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);



                                        //  transaction.update(sfDocRef, "population", newPopulation);

                                        transaction.update(sfDocRef,"name",name);
                                        transaction.update(sfDocRef,"age",age);
                                        transaction.update(sfDocRef,"bio",bio);
                                        transaction.update(sfDocRef,"email",email);
                                        transaction.update(sfDocRef,"website",website);
                                        transaction.update(sfDocRef,"url",downloadUri.toString());


                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

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

            final DocumentReference sfDocRef = db.collection("user").document(gmailid);

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);



                    //  transaction.update(sfDocRef, "population", newPopulation);

                    transaction.update(sfDocRef,"name",name);
                    transaction.update(sfDocRef,"age",age);
                    transaction.update(sfDocRef,"bio",bio);
                    transaction.update(sfDocRef,"email",email);
                    transaction.update(sfDocRef,"website",website);



                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



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
                            //    String Url = task.getResult().getString("url");


                            //  Picasso.get().load(Url).into(imageView);
                            et_name.setText(name_result);
                            et_age.setText(age_result);
                            et_bio.setText(bio_result);
                            et_email.setText(email_result);
                            et_website.setText(web_result);


                        }else {
                            Toast.makeText(UpdateUser.this, "No Profile exist", Toast.LENGTH_SHORT).show();
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