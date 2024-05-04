package tech.demoproject.OTPV;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button btnLogin;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components and Firebase reference
        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (isInputInvalid(username, password)) {
                Toast.makeText(LoginActivity.this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the username and password combination exists in Firebase
            Query query = usersRef.orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        boolean isValidLogin = false;

                        // Iterate through the results to check if the username and password match
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String dbPassword = userSnapshot.child("password").getValue(String.class);
                            if (dbPassword != null && dbPassword.equals(password)) {
                                isValidLogin = true;
                                break;
                            }
                        }

                        if (isValidLogin) {
                            // Login successful, navigate to the next activity
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close LoginActivity after successful login
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Username not found. Please sign up.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isInputInvalid(String username, String password) {
        return username.isEmpty() || password.isEmpty();
    }
}
