package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Verification extends AppCompatActivity {
      TextView verify;
      Button vbtn;
      String mailgid,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verify = findViewById(R.id.verify);
        vbtn = findViewById(R.id.verifybtn);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mailgid =getIntent().getStringExtra("gmail");
        pass  = getIntent().getStringExtra("password");
        verify.setText(mailgid);
        if (!mailgid.isEmpty()){
            Toast.makeText(this, "Verify Your mail to enter", Toast.LENGTH_SHORT).show();
            vbtn.setVisibility(View.VISIBLE);
            vbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signInWithEmailAndPassword(mailgid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified()){
                                Intent intent1 = new Intent(getApplicationContext(),CreateProfile.class);
                                intent1.putExtra("gmail",mailgid);
                                startActivity(intent1);
                                }else{
                                user.sendEmailVerification();
                                Toast.makeText(Verification.this, "verification link has been sent", Toast.LENGTH_SHORT).show();
                                }
                        }
                        }
                    });
                }
            });
        }else{
            vbtn.setVisibility(View.INVISIBLE);

        }

    }
}