package HOSPITAL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covidcare.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Showdet extends AppCompatActivity {

    DatabaseReference ref1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String gmail;
    public String parts1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdet);
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
    }
}