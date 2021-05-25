package USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.covidcare.R;

public class otp extends AppCompatActivity {
    public CardView continuebutton;
    public EditText phone_input;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        continuebutton=findViewById(R.id.next);
        phone_input=findViewById(R.id.phone);

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), otpscreen.class);
                number = phone_input.getText().toString();
                intent.putExtra("number", number);
                startActivity(intent);



            }
        });
    }

}

