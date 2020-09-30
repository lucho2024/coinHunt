package com.example.coinhunt.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.coinhunt.R;
import com.example.coinhunt.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText EName;
    Button btnStart;
    String nick;
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();

        EName = findViewById(R.id.textName);
        btnStart = findViewById(R.id.buttonStart);
        //Cambiar tipo de fuente

        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        EName.setTypeface(typeface);
        btnStart.setTypeface(typeface);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = EName.getText().toString();
                if(nick.isEmpty()){
                    EName.setError("The name is empty");
                }else if(nick.length()<3){
                    EName.setError("must be long 3");
                }else{
                    addNickAndStart();

                }
            }
        });
    }

    private void addNickAndStart() {
        db.collection("users").whereEqualTo("nick",nick)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0){
                            EName.setError("El nick no esta disponible");
                        }else{
                            addNickToFire();
                        }
                    }
                });

    }

    private void addNickToFire() {
        db.collection("users")
                .add(new User(nick,0))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        EName.setText("");
                        Intent i = new Intent(LoginActivity.this, GameActivity.class);
                        i.putExtra("name",nick);
                        i.putExtra("id",documentReference.getId());
                        startActivity(i);
                    }
                });

    }
}