package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
      TextView verify,confirmation,sent;
      Button vbtn;
      String mailgid,pass;
      RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verify = findViewById(R.id.verify);
        confirmation=findViewById(R.id.confirmation);
        relativeLayout=findViewById(R.id.relative);
        sent=findViewById(R.id.sent);
        vbtn = findViewById(R.id.verifybtn);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mailgid =getIntent().getStringExtra("gmail");
        pass  = getIntent().getStringExtra("password");
        relativeLayout.setVisibility(View.INVISIBLE);
        sent.setVisibility(View.INVISIBLE);
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
                                    Intent intent1 = new Intent(getApplicationContext(),Profile.class);
                                    intent1.putExtra("gmail",mailgid);
                                    startActivity(intent1);
                                }else{
                                    user.sendEmailVerification();
                                    vbtn.setText("CONTINUE");
                                    confirmation.setText("After Successful Verification,click Continue button");
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    sent.setVisibility(View.VISIBLE);
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