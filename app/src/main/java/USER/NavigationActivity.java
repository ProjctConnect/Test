package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.covidcare.R;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity {

    String gmail;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setUpToolbar();
        gmail=getIntent().getStringExtra("gmailid");
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.showprofile:

                        Intent intent = new Intent(NavigationActivity.this, Showprofile.class);
                        intent.putExtra("gmail",gmail);
                        startActivity(intent);
                        break;

                    case R.id.searchhosp:
                        Intent intent1=new Intent(getApplicationContext(),details.class);
                        intent1.putExtra("gmail",gmail);
                        startActivity(intent1);
                        break;

                    case R.id.update:
                        Intent intent2=new Intent(getApplicationContext(),UpdateUser.class);
                        intent2.putExtra("gmail",gmail);
                        startActivity(intent2);
                        break;

                    case R.id.booking:
                        Intent intent3=new Intent(getApplicationContext(),BookingHistory.class);
                        intent3.putExtra("gmail",gmail);
                        startActivity(intent3);
                        break;

                   


                    default:
                        Intent intent4=new Intent(getApplicationContext(),NavigationActivity.class);
                        startActivity(intent4);
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
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.nav_open , R.string.nav_close);
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
}
