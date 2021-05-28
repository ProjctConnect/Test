package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.HomeActivity;
import com.example.covidcare.MainActivity;
import com.example.covidcare.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Showdet extends AppCompatActivity {

    DatabaseReference ref1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String gmail;
    public String parts1;
    TextView noofN,noofO;
    Button mod,lout;
    GoogleApiClient gac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbeds);

        noofN = findViewById(R.id.noofnorbeds1);
        noofO = findViewById(R.id.noofoxybeds2);
        mod = findViewById(R.id.mod1);
        lout = findViewById(R.id.Logout1);
        String dress = getIntent().getStringExtra("add");
        String mal = getIntent().getStringExtra("norm");
        String gen = getIntent().getStringExtra("oxyg");

        Map<String, Object> data = new HashMap<>();
        data.put("Address of My Hospital",dress);
        data.put("Total no of Normal Beds",mal);
        data.put("Total no of Oxygen Beds",gen);

        gmail = getIntent().getStringExtra("Email");
        String[] parts = gmail.split("(?=@)");
        parts1 = parts[0];
        ref1 = database.getReference("GMAIL OF HOSPITALS");
        ref1.child(parts1).setValue(data);

        noofN.setText(mal);
        noofO.setText(gen);

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),DATABEDS.class);
                startActivity(intent1);
                finish();
            }
        });
        lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(gac).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()){
                            Intent intent2 =new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent2);
                            finish();
                        }else {
                            Toast.makeText(Showdet.this, "LogOut Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}