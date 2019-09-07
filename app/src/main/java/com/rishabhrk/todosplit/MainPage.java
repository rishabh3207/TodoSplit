package com.rishabhrk.todosplit;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URL;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    TextView user_name ,email_id;
    FirebaseAuth.AuthStateListener mAuthListner;
    ImageView photo;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListner);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mdrawerLayout=findViewById(R.id.drawer);
        mtoggle=new ActionBarDrawerToggle(this,mdrawerLayout,toolbar,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if( firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainPage.this,MainActivity.class));
                }
            }
        };
        View headView = navigationView.getHeaderView(0);
        photo = (ImageView) headView.findViewById(R.id.profile_image);
        user_name = (TextView) headView.findViewById(R.id.user_name);
        //email_id = headView.findViewById(R.id.email_id);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String str="WELCOME, ";
            String name = user.getDisplayName();
            name = name.toUpperCase();
            str = str.concat(name);
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            user_name.setText(str);
            Glide.with(this).load(photoUrl).into(photo);
           // photo.setImageURI(photoUrl);
            //email_id.setText(email);
        }
    }

    @Override
    public void onBackPressed() {

        if(mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mtoggle.onOptionsItemSelected(item))
        {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if( id == R.id.groups) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,new Todos())
                    .addToBackStack(null)
                    .commit();
        }

        if( id == R.id.logout ){
            Toast.makeText(MainPage.this,"Logout Successfully",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainPage.this,MainActivity.class));
            this.finish();
        }
        if( id == R.id.about ){

            fragmentTransaction = fragmentManager.beginTransaction();
            AboutUs f1 = new AboutUs();
            fragmentTransaction.add(R.id.fragment_container,f1)
                    .addToBackStack(null)
                    .commit();

        }

        if(mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
