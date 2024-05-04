package tech.demoproject.OTPV;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {
//The old files with firebase configetion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent loginIntent = new Intent(Start.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        btnSignup.setOnClickListener(v -> {
            // Navigate to SignupActivity
            Intent signupIntent = new Intent(Start.this, SendOTPActivity.class);
            startActivity(signupIntent);
        });
    }
}
