package com.example.helptheneedy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helptheneedy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.util.ArrayList;
//import java.util.List;

public class usersign extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;
    private Spinner spin;
    private Button signUp;
    private Button login;
    private TextView userName;
    private TextView email;
    private TextView password;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersign);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        spin = (Spinner) findViewById(R.id.spin);
        signUp = (Button) findViewById(R.id.signupButton);
        login  = (Button) findViewById(R.id.loginButton);
        email = (TextView) findViewById(R.id.editTextTextEmailAddress2);
        password = (TextView) findViewById(R.id.editTextTextPassword2);
        userName = (TextView) findViewById(R.id.userName);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrouoID);
        //String utype = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

       /* List<String> cities = new ArrayList<String>();
        cities.add("Bhopal");
        cities.add("Indore");
        cities.add("Ujjain");*/

        spin.setOnItemSelectedListener(this);

        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        String city = spin.getSelectedItem().toString();
        /*signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(usersign.this, "Registered Succeessfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(usersign.this, homePage.class);
                startActivity(intent);
            }
        });*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(usersign.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uname = userName.getText().toString().trim();
                final String emailString = email.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                final String utype = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                final String city = spin.getSelectedItem().toString();

                if(!emailString.equals("") && !pwd.equals("") && !city.equals("City")){
                    mProgressDialog.setMessage("Creating Account...");
                    mProgressDialog.show();
                    mAuth.createUserWithEmailAndPassword(emailString,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult!= null){
                                String userid = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDB = mDatabaseReference.child(userid);
                                currentUserDB.child("Username").setValue(uname);
                                currentUserDB.child("UserType").setValue(utype);
                                currentUserDB.child("City").setValue(city);
                                currentUserDB.child("Email").setValue(emailString);
                                //currentUserDB.child("Image").setValue("@drawable/donator_logo");
                                mProgressDialog.dismiss();

                                Intent intent = new Intent(usersign.this, homePage.class);
                                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(usersign.this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(v, "Please fill out all the fields!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.print("Hello");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}