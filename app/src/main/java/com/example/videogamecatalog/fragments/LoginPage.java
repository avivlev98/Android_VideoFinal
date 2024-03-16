package com.example.videogamecatalog.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videogamecatalog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPage extends Fragment {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;

    private Button loginButton;

    public LoginPage() {
        // Required empty public constructor
    }


    public static LoginPage newInstance(String param1, String param2) {
        LoginPage fragment = new LoginPage();
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
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);
        auth = FirebaseAuth.getInstance();
        loginEmail = view.findViewById(R.id.loginEmailInput);
        loginPassword = view.findViewById(R.id.loginPasswordInput);
        loginButton = view.findViewById(R.id.LogginButton);
        signupRedirectText = view.findViewById(R.id.signupRedirectText);

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_loginPage_to_signupPage);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String username = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if(!username.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                    if (!password.isEmpty()){
                        auth.signInWithEmailAndPassword(username,password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(view).navigate(R.id.action_loginPage_to_homePage);
                                        return;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "login Failed" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        loginPassword.setError("Password cannot be empty");
                    }
                }
                else if(username.isEmpty()){
                    loginEmail.setError("Email cannot be empty");
                }else {
                    loginEmail.setError("Please enter valid email");
                }

            }
        });

        return view;
    }
}