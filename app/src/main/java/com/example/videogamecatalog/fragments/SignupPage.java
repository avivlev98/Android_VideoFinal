package com.example.videogamecatalog.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videogamecatalog.Classes.User;
import com.example.videogamecatalog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignupPage extends Fragment {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;

    private Button signupButton;
    private TextView loginRedirectText;




    public SignupPage() {
        // Required empty public constructor
    }


    public static SignupPage newInstance(String param1, String param2) {
        SignupPage fragment = new SignupPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_page, container, false);
        auth = FirebaseAuth.getInstance();
        signupEmail = view.findViewById(R.id.emailInput);
        signupPassword = view.findViewById(R.id.passwordInput);
        signupButton = view.findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                if (username.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (password.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else {
                    User newUser = new User(username, password);
                    auth.createUserWithEmailAndPassword(newUser.getUsername(), newUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "SignUp Successful", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_signupPage_to_loginPage2);
                            } else {
                                Toast.makeText(getActivity(), "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_signupPage_to_loginPage2);
                            }
                        }
                    });

                }
            }
        });

        return view;
    }
}