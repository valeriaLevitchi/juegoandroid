package com.example.juegoandroid;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class menuprincipal extends AppCompatActivity {

    int difficulty;

    private EditText nombrejugador;
    private EditText dificultad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menuprincipal);

        // conexion
        nombrejugador = findViewById(R.id.nombre);
        dificultad = findViewById(R.id.dificultad);

        // Configura el botón para iniciar el juego
        Button btnStartGame = findViewById(R.id.boton);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciojuego();
            }
        });
    }

    private void iniciojuego() {
        // Obtén el número de dificultad introducido por el jugador
        EditText editTextDifficulty = findViewById(R.id.dificultad);
        String difficultyString = editTextDifficulty.getText().toString();

        if (!difficultyString.isEmpty()) {
            try {
                 difficulty = Integer.parseInt(difficultyString);

                // orden activity
                Intent gameIntent = new Intent(menuprincipal.this, MainActivity.class);
                gameIntent.putExtra("DIFFICULTY", difficulty);
                startActivity(gameIntent);
//exepciones
            } catch (NumberFormatException e) {
                // Maneja la excepción si la dificultad no es un número válido
                Toast.makeText(this, "Por favor, introduce un nivel de dificultad válido", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Muestra un mensaje si la casilla de dificultad está vacía
            Toast.makeText(this, "Por favor, introduce un nivel de dificultad", Toast.LENGTH_SHORT).show();
        }
    }



}
