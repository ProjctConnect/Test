package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button createacc,login;
    EditText mail,password;
    String user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        createacc=findViewById(R.id.reglog);
        login=findViewById(R.id.login1);
        mail=findViewById(R.id.email);
        password=findViewById(R.id.password);
        user=mail.getText().toString().trim();
        firebaseAuth=FirebaseAuth.getInstance();
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mail.getText().toString().isEmpty()){
                    mail.setError("Enter E-Mail id");
                    return;
                }
                if (password.getText().toString().isEmpty()){
                    password.setError("Enter Password");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(mail.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                Intent intent1 = new Intent(getApplicationContext(),NavigationActivity.class);
                                intent1.putExtra("gmail",mail.getText().toString().trim());
                                startActivity(intent1);
                            }else{
                                user.sendEmailVerification();
                                Toast.makeText(Login.this, "verification link has been sent", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent i=new Intent(getApplicationContext(),NavigationActivity.class);
            i.putExtra("gmailid",mail.getText().toString());
            startActivity(i);
            finish();
        }
    }
}