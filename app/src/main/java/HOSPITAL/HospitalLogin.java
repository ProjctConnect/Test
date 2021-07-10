package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HospitalLogin extends AppCompatActivity {
    Button createacc,login;
    EditText mail,password;
    String user,email;
    TextView resetpassword;
    FirebaseAuth firebaseAuth;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);
        createacc=findViewById(R.id.reglog1);
        login=findViewById(R.id.login11);
        mail=findViewById(R.id.email1);
        resetpassword=findViewById(R.id.reset1);
        password=findViewById(R.id.password1);
        user=mail.getText().toString().trim();
        firebaseAuth=FirebaseAuth.getInstance();

        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), HospitalRegister.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail=mail.getText().toString().trim();
                String password1=password.getText().toString().trim();
                if (gmail.isEmpty()){
                    mail.setError("Enter E-Mail id");
                    return;
                }
                if (password1.isEmpty()){
                    password.setError("Enter Password");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(gmail,password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String hospital="Hospital";
                        if(user.isEmailVerified()){
                            if (user.getDisplayName().equals(hospital)){
                                Intent intent1 = new Intent(getApplicationContext(), HospitalNavigationActivity.class);
                                intent1.putExtra("mailid",gmail);
                                startActivity(intent1);
                                finish();

                            }else {
                                Toast.makeText(HospitalLogin.this, "The current user belong to Patient end", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Intent intent=new Intent(getApplicationContext(),HOSPITALDETAILS.class);
                            intent.putExtra("gmail",gmail);
                            intent.putExtra("password",password1);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "verification link has been sent", Toast.LENGTH_SHORT).show();
                        }





                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });




    }

    private void resetpassword() {
        String mailid=mail.getText().toString().trim();
        if (mailid.isEmpty()){
            mail.setError("Enter E-Mail");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mailid).matches()){
            mail.setError("Provide Valid E-Mail ID");
            return;
        }
        firebaseAuth.sendPasswordResetEmail(mailid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Password Reset Mail sent", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}