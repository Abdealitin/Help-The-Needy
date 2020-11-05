package com.example.helptheneedy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helptheneedy.Data.RequestRecyclerAdapter;
import com.example.helptheneedy.DonateItemsDetails;
import com.example.helptheneedy.Model.Request;
import com.example.helptheneedy.Model.User;
import com.example.helptheneedy.R;
import com.example.helptheneedy.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.widget.Toast.LENGTH_SHORT;

public class  homePage extends AppCompatActivity {
    //private Button fabButton;
    private DatabaseReference mDatabaseReference;
    private TextView userName;
    private TextView userEmail;
    private ImageView profilePic;
    private TextView utype;
    private RequestRecyclerAdapter requestRecyclerAdapter;
    private List<Request> requestList;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button signout;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        /*utype = (TextView) findViewById(R.id.utypeText);
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userMail);
        signout = (Button) findViewById(R.id.action_signout);*/
        //utype.setText("Donator");



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String user= mUser.getEmail();
        //userEmail.setText(user);

        /*signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(homePage.this, MainActivity.class));

            }
        });*/
        /*DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("Users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                        userName.setText(dataSnapshot.child("Username").getValue().toString());
                        utype.setText(dataSnapshot.child("UserType").getValue().toString());
                        userEmail.setText(dataSnapshot.child("Email").getValue().toString());
                        String userID = String.valueOf(dataSnapshot.child("Username").getValue());
                        System.out.print(userID);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //Toast.makeText(homePage.this, "Fab Pressed", Toast.LENGTH_SHORT).show();
                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference.child("Users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if(dataSnapshot.exists()){
                             String userID = dataSnapshot.child("Username").getValue().toString();
                             String type=dataSnapshot.child("UserType").getValue().toString();
                             String ema= dataSnapshot.child("Email").getValue().toString();

                             Toast.makeText(homePage.this, "Username:" + userID + "\n Email: "+ema+"\nType: "+type, LENGTH_SHORT).show();
                        /*for(DataSnapshot userDetails : dataSnapshot.getChildren()){
                            /*userName.setText(userDetails.child("Username").getValue().toString());
                            utype.setText(userDetails.child("UserType").getValue().toString());
                            userEmail.setText(userDetails.child("Email").getValue().toString());*/
                            //String userID = userDetails.child("Username").getValue().toString();
                            /*String em=userDetails.child("UserType").getValue().toString();
                            c*/
                            //Toast.makeText(homePage.this, "Username:" + userID /*+ "\n Email: "+em+"\nType: "+type*/, LENGTH_SHORT).show();
                        //}
                        }else{
                             String uID=mUser.getUid();
                             String ema=mUser.getEmail();
                             Toast.makeText(homePage.this, "Username:" + uID + "\n Email: "+ema, LENGTH_SHORT).show();
                         }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                if(mUser != null && mAuth != null){
                    startActivity(new Intent(homePage.this, makeRequest.class));
                    finish();
                }

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        utype = (TextView) headerView.findViewById(R.id.utypeText);
        userName = (TextView) headerView.findViewById(R.id.userName);
        userEmail = (TextView) headerView.findViewById(R.id.userMail);
        signout = (Button) headerView.findViewById(R.id.action_signout);
        /*signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(homePage.this, MainActivity.class));

            }
        });*/

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("Users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    userName.setText(dataSnapshot.child("Username").getValue().toString());
                    utype.setText(dataSnapshot.child("UserType").getValue().toString());
                    userEmail.setText(dataSnapshot.child("Email").getValue().toString());
                    String userID = String.valueOf(dataSnapshot.child("Username").getValue());
                    System.out.print(userID);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}