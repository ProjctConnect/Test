package HOSPITAL;

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

import USER.Profile;

public class HospitalVerification extends AppCompatActivity {
    TextView verify,confirmation,sent;
    Button vbtn;
    String mailgid,pass,hospitalname,city,address;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_verification);
        verify = findViewById(R.id.verify1);
        confirmation=findViewById(R.id.confirmation1);
        relativeLayout=findViewById(R.id.relative1);
        sent=findViewById(R.id.sent1);
        vbtn = findViewById(R.id.verifybtn1);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mailgid =getIntent().getStringExtra("Email");
        pass  = getIntent().getStringExtra("password");
        hospitalname=getIntent().getStringExtra("keyname2");
        city=getIntent().getStringExtra("keyname");
        address=getIntent().getStringExtra("address");


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
                                    Intent intent1 = new Intent(getApplicationContext(), DATABEDS.class);
                                    intent1.putExtra("Email",mailgid);
                                    intent1.putExtra("keyname2",hospitalname);
                                    intent1.putExtra("keyname",city);
                                    intent1.putExtra("address",address);
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