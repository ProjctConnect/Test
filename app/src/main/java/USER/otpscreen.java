package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpscreen extends AppCompatActivity {
    public String num_entered_by_user,code_by_system;
    public CardView verify;
    public TextView resend;
    public PinView otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        Intent intent=getIntent();
        num_entered_by_user=intent.getStringExtra("number");
        verify=findViewById(R.id.verifybutton);
        resend=findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_otp(num_entered_by_user);
            }
        });
        otp=findViewById(R.id.pinview);
        sendcodetouser(num_entered_by_user);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_code();
            }
        });
    }

    private void resend_otp(String num_entered_by_user) {

        sendcodetouser(num_entered_by_user);
        Toast.makeText(this, "Resending code...", Toast.LENGTH_SHORT).show();
    }

    private void check_code() {
        String user_otp=otp.getText().toString();
        if (user_otp.isEmpty() || user_otp.length()<6){
            Toast.makeText(this, "Wrong OTP!", Toast.LENGTH_SHORT).show();
            return;
        }
        finish_everything(user_otp);
    }

    private void sendcodetouser(String num_entered_by_user) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+num_entered_by_user,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback


        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            code_by_system=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code !=null){
                finish_everything(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(otpscreen.this,e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void finish_everything(String code) {
        otp.setText(code);
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(code_by_system,code);
        sign_in(credential);
    }

    private void sign_in(PhoneAuthCredential credential) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnCompleteListener(otpscreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(otpscreen.this,"User signed in successfully!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(otpscreen.this,otpscreen.class));


                }else {
                    Toast.makeText(otpscreen.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}