package com.example.juegoandroid;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CollisionView collisionView;
    private TextView gameOverText;
    private MediaPlayer colisioncancion;
    private MediaPlayer cancion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//conexiones necesarias
        collisionView = findViewById(R.id.collisionView);
        gameOverText = findViewById(R.id.gameover);
        colisioncancion = MediaPlayer.create(this, R.raw.colision);
        cancion =MediaPlayer.create(this, R.raw.cancion1);
        //cancion de inicio
cancion.start();
//game over
        gameOverText.setVisibility(View.INVISIBLE);
//conficgurando collisionview para que tenga un "escuchador"
        collisionView.setCollisionListener(new CollisionView.CollisionListener() {
            @Override
            public void onCollision() {
                // acciones cuando hay colision
                gameOverText.setVisibility(View.VISIBLE);
                cancion.stop();
                colisioncancion.start();
            }
        });



    }
}

