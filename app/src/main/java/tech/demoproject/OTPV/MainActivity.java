package tech.demoproject.OTPV;
import android.content.Intent; // Required for navigation
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    private EditText nameInput, usernameInput, passwordInput;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize UI components and Firebase reference
        nameInput = findViewById(R.id.etName);
        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void Next(View view) {
        String name = nameInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (isInputInvalid(name, username, password)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the username is unique
        Query usernameQuery = usersRef.orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already exists
                    Toast.makeText(MainActivity.this, "Username already exists. Choose another.", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is unique, proceed with registration
                    String userId = usersRef.push().getKey(); //Creates a unique key under the usersRef node.
                    if (userId != null) {
                        User newUser = new User(name, username, password);
                        usersRef.child(userId).setValue(newUser);

                        // Navigate to the next activity after successful registration
                        Intent intent = new Intent(MainActivity.this, Incom.class);
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error registering user", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isInputInvalid(String name, String username, String password) {
        return name.isEmpty() || username.isEmpty() || password.isEmpty();
    }
}
