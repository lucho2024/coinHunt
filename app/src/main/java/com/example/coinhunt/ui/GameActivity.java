package com.example.coinhunt.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coinhunt.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tvCounter,tvTimer,tvNick;
    ImageView ivBtc;
    int counter=0;
    int ancho;
    int alto;
    Random random;
    boolean gameOver= false;
    String id;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        db = FirebaseFirestore.getInstance();

        initViewComponents();
        events();
        initP();
        moveCoin();
        initCuentaAtras();
    }

    private void initCuentaAtras() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText(millisUntilFinished / 1000+"s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver=true;
                mostrarDialogoGameOver();
                saveResult();

            }


        }.start();
    }

    private void saveResult() {
        db.collection("users")
                .document(id)
                .update(
                        "duck",counter
                );
    }

    private void mostrarDialogoGameOver() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Has conseguido cazar "+counter+" btc")
                .setTitle("Game over");
        builder.setPositiveButton("reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                counter=0;
                tvCounter.setText("0");
                gameOver=false;
                initCuentaAtras();
                moveCoin();
                // User clicked OK button
            }
        });
        builder.setNegativeButton("Ver ranking", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                Intent i= new Intent(GameActivity.this,RankingActivity.class);
                startActivity(i);
            }
        });

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void initP() {
        //obtener tamaño de pantalla

        Display display=getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ancho=size.x;
        alto=size.y;
        //inicializamos el ramdon
        random= new Random();
    }

    private void events() {
        ivBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver){
                    counter++;
                    tvCounter.setText(String.valueOf(counter));
                    ivBtc.setImageResource(R.drawable.bitcoin3);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivBtc.setImageResource(R.drawable.bitcoin);
                            moveCoin();
                        }
                    },500);
                }


            }
        });
    }

    private void moveCoin() {
        int min=0;
        int maxX=ancho-ivBtc.getWidth();
        int maxY=alto-ivBtc.getHeight();

        //generamos 2 numero aleatorios
        int randomx=random.nextInt(((maxX-min)+1));
        int randomy=random.nextInt(((maxY-min)+1));
        //UTILIZAMOS LOS NUMERO PARA SETERALOS EN LA IMAGEN
        ivBtc.setX(randomx);
        ivBtc.setY(randomy);


    }

    private void initViewComponents() {
        tvCounter=findViewById(R.id.textViewCounter);
        tvTimer=findViewById(R.id.textViewTimer);
        tvNick=findViewById(R.id.textViewNick);
        ivBtc=findViewById(R.id.imageViewBtc);

        //Cambiar tipo de letra
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        tvCounter.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);
        //Extras
        Bundle extras = getIntent().getExtras();
        String nick=extras.getString("name");
        id=extras.getString("id");
        tvNick.setText(nick);
    }
}