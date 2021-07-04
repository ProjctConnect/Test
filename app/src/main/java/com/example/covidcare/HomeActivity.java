package com.example.covidcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import HOSPITAL.HospitalNavigationActivity;
import HOSPITAL.REGandLOG;
import HOSPITAL.Showdet;
import USER.Login;
import USER.NavigationActivity;
import USER.R_AND_L;

public class HomeActivity extends AppCompatActivity {
    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageSlider=findViewById(R.id.imageslider);

        CardView hospital = (CardView) findViewById(R.id.h);
        CardView patient = (CardView) findViewById(R.id.p);
        ArrayList<SlideModel> images=new ArrayList<>();
        images.add(new SlideModel("https://i.ytimg.com/vi/nfsB5bfQgmo/maxresdefault.jpg","Thank you frontline warriors",null));
        images.add(new SlideModel("https://media.istockphoto.com/vectors/people-and-doctor-wearing-face-mask-fight-against-covid19-vector-id1212677454","Let's fight against covid",null));
        images.add(new SlideModel("https://i.ytimg.com/vi/hpyBleye9A8/maxresdefault.jpg","Stay Home Stay Safe",null));
        images.add(new SlideModel("https://i.pinimg.com/originals/d1/56/12/d1561289a20dff6f26ac53cfdd75ece4.png","Wear Mask",null));
        imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);


        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), REGandLOG.class);
                startActivity(intent);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), R_AND_L.class);
                startActivity(intent);
            }
        });




    }
    @Override
    protected void onStart(){
        super.onStart();
        String patient="patient";
        String hospital="Hospital";
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            if (user.isEmailVerified()){
                if (user.getDisplayName().equals(patient)){
                    String gmail= user.getEmail();
                    Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                    i.putExtra("gmailid", gmail);
                    startActivity(i);

                }
                if (user.getDisplayName().equals(hospital)){
                    String gmail= user.getEmail();
                    Intent i = new Intent(getApplicationContext(), HospitalNavigationActivity.class);
                    i.putExtra("mailid", gmail);
                    startActivity(i);

                }


            }


        }


    }

}