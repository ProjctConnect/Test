package USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.covidcare.R;import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class otp extends AppCompatActivity {
    public CardView continuebutton;
    public EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        continuebutton=findViewById(R.id.next);
        phone=findViewById(R.id.phone);

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(otp.this,otpscreen.class);
                final String number=phone.getText().toString();
                intent.putExtra("number",number);
                startActivity(intent);
            }
        });
    }
}