package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import USER.Login;
import USER.Verification;

public class HospitalRegister extends AppCompatActivity {

    EditText email,password,confirmpassword;
    Button register,login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_register);
        email=findViewById(R.id.googlemail1);
        password=findViewById(R.id.pass1);
        confirmpassword=findViewById(R.id.confpass1);
        register=findViewById(R.id.regi1);
        login=findViewById(R.id.logi1);
        firebaseAuth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), HospitalLogin.class);
                startActivity(i);
                finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                String confirm=confirmpassword.getText().toString().trim();


                if (gmail.isEmpty()){
                    email.setError("Enter your E-Mail ID");
                    return;
                }
                if (pass.isEmpty()){
                    password.setError("Enter your Password");
                    return;
                }
                if (confirm.isEmpty()){
                    confirmpassword.setError("Enter your Confirm Password");
                    return;
                }
                if (!pass.equals(confirm)){
                    confirmpassword.setError("Password mismatch");
                    return;
                }



                firebaseAuth.createUserWithEmailAndPassword(gmail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String p="Hospital";
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(p).build();
                        user.updateProfile(profileChangeRequest);
                        Intent intent=new Intent(getApplicationContext(), HOSPITALDETAILS.class);
                        intent.putExtra("gmail",gmail);
                        intent.putExtra("password",pass);
                        startActivity(intent);
                        finish();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}