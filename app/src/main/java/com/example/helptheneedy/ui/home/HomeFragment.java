package com.example.helptheneedy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helptheneedy.Activities.homePage;
import com.example.helptheneedy.Data.RequestRecyclerAdapter;
import com.example.helptheneedy.Model.Request;
import com.example.helptheneedy.Model.User;
import com.example.helptheneedy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.Toast.*;

public class HomeFragment extends Fragment {
    private TextView userName;
    private TextView userEmail;
    private ImageView profilePic;
    private TextView utype;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;
    private RecyclerView recyclerView;
    private RequestRecyclerAdapter requestRecyclerAdapter;
    private List<Request> requestList;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_header_main, container, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        utype = (TextView) view.findViewById(R.id.utypeText);
        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userMail);




        mDatabaseReference = mDatabase.getReference().child("Requests");
        mDatabaseReference.keepSynced(true);


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        requestList = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request request = dataSnapshot.getValue(Request.class);
                requestList.add(request);
                Collections.reverse(requestList);
                requestRecyclerAdapter = new RequestRecyclerAdapter(recyclerView.getContext(), requestList);
                recyclerView.setAdapter(requestRecyclerAdapter);
                requestRecyclerAdapter.notifyDataSetChanged();
                mDatabaseReference1 = mDatabase.getInstance().getReference();
                mDatabaseReference1.child("Users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                    /*for(DataSnapshot userDetails : dataSnapshot.getChildren()){
                        userName.setText(userDetails.child("Username").getValue().toString());
                        utype.setText(userDetails.child("UserType").getValue().toString());
                        userEmail.setText(userDetails.child("Email").getValue().toString());
                    }*/
                    /*userName.setText(dataSnapshot.child("Username").getValue().toString());
                    utype.setText(dataSnapshot.child("UserType").getValue().toString());
                    userEmail.setText(dataSnapshot.child("Email").getValue().toString());*/
                            String UName= dataSnapshot.child("Username").getValue().toString();
                            User user= new User();
                            user.setUsername(UName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}