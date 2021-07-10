package USER;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.covidcare.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import HOSPITAL.HospitalShowProfile;

public class NavigationActivity extends AppCompatActivity {

    String gmail,name;
    TextView textview5;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth;
    ImageView hospitals,update,guidelines,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setUpToolbar();
        gmail=getIntent().getStringExtra("gmailid");
        hospitals=findViewById(R.id.im1);
        update=findViewById(R.id.im2);
        guidelines=findViewById(R.id.im3);
        history=findViewById(R.id.im4);

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        firebaseAuth=FirebaseAuth.getInstance();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.showprofile:

                        Intent intent = new Intent(NavigationActivity.this, Showuser.class);
                        intent.putExtra("gmail",gmail);
                        startActivity(intent);
                        break;

                    case R.id.searchhosp:
                        Toast.makeText(NavigationActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.update:
                        Toast.makeText(NavigationActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.booking:
                        Toast.makeText(NavigationActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.logout:
                        firebaseAuth.signOut();
                        Intent intent4=new Intent(getApplicationContext(),R_AND_L.class);
                        intent4.putExtra("gmail",gmail);
                        startActivity(intent4);
                        finish();
                        break;

                   


                    default:
                        Intent intent5=new Intent(getApplicationContext(),NavigationActivity.class);
                        startActivity(intent5);
                        break;






//Paste your privacy policy link

//                    case  R.id.nav_Policy:{
//
//                        Intent browserIntent  = new Intent(Intent.ACTION_VIEW , Uri.parse(""));
//                        startActivity(browserIntent);
//
//                    }
                    //       break;


                }
                return false;
            }
        });
    }
    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open , R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
