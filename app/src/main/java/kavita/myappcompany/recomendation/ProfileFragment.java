package kavita.myappcompany.recomendation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    View view;
    int ans=0;
    String idToken, msg;
    private EditText email_login, password_login;
    private TextView newAccount, forgot_password;

    private FirebaseAuth mAuth;
    private Button login;


    private static final String TAG = "Cannot invoke method length() on null object";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        forgot_password=view.findViewById(R.id.forgetpwd);

        email_login = view.findViewById(R.id.email);
        password_login = view.findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
         MainActivity mainActivity = (MainActivity) getActivity();
        login = view.findViewById(R.id.Login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        if (ans==0){
            String emailText = email_login.getText().toString();
            String password = password_login.getText().toString();



            if(email_login.getText().toString().isEmpty()) {
                email_login.setError("Email Required");
                email_login.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email_login.getText().toString()).matches()) {
                email_login.setError("Valid Email Required");
                email_login.requestFocus();
                return;
            }
            if(password_login.getText().toString().isEmpty()) {
                password_login.setError("Password Required");
                password_login.requestFocus();
                return;
            }
            if(password_login.getText().toString().length()<6) {
                password_login.setError("Min 6 char required");
                password_login.requestFocus();
                return;
            }

ans=1;
            registerUser(emailText,password);
        }
        else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String emailText = email_login.getText().toString();
                    String passWord = password_login.getText().toString();

                    if (email_login.getText().toString().isEmpty()) {
                        email_login.setError("Email Required");
                        email_login.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email_login.getText().toString()).matches()) {
                        email_login.setError("Valid Email Required");
                        email_login.requestFocus();
                        return;
                    }
                    if (password_login.getText().toString().isEmpty()) {
                        password_login.setError("Password Required");
                        password_login.requestFocus();
                        return;
                    }
                    if (password_login.getText().toString().length() < 6) {
                        password_login.setError("Min 6 char required");
                        password_login.requestFocus();
                        return;
                    }

                    User(emailText, passWord);


                }
            });
        }
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_login.getText().toString().isEmpty()) {
                    email_login.setError("Email Required");
                    email_login.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email_login.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(getContext(), "Reset Link has been sent to your mail", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getContext(), "Unable to send reset link...Please try again later!!", Toast.LENGTH_SHORT).show();


                            }
                        });
            }
        });



    }


    public void User(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), "Invalid Email / Password", Toast.LENGTH_SHORT).show();

                    } else {

                        VerificationCheck(email);
                    }
                });
    }


    private void VerificationCheck(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (user.isEmailVerified()) {
            // sp.edit().putBoolean("logged",true).apply();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("Email", email);

            //startActivity(intent);
            //mainActivity.finish();
            Toast.makeText(getContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();


        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(getContext(), "Please Verify your Email to continue", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Please verify email to continue", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("failure", "Email not sent" + e.getMessage());
                            }
                        });

//                        //SignUp success
//                        Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
//                        intent.putExtra("Email",email);
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(getContext(), "Email ID already exists..", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    }
