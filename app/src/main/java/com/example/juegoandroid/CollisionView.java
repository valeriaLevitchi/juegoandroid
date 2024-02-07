package com.example.juegoandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class CollisionView extends View {

//variables paint pra circulos//naves
    private Paint circulomovimiento;
    private Paint circulorandom;
//cordenadas y dimensiones de rectangulos
    private RectF circulomovimiento1;
    private RectF circulorandom1;
//estado juego
    private boolean gameOver = false;
//movimiento circulo
    private Handler handler;
    private Runnable moveRandomCircleRunnable;
//notificar colision
    private CollisionListener collisionListener;
    //cancion
    private MediaPlayer colisioncancion;
    //rastrear tiempo
    private long startTime;
    private long elapsedTime;
//velocidad incial y en aumento
    private static final int INITIAL_SPEED = 6; // Velocidad inicial del círculo azul
    private static final float SPEED_INCREASE_FACTOR = 0.025f; // Factor de aumento de velocidad
//constructor
    public CollisionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializacion();

    }

    public interface CollisionListener {
        void onCollision();
    }

    public void setCollisionListener(CollisionListener listener) {
        this.collisionListener = listener;

    }
//inicializacion
    private void inicializacion() {

        circulomovimiento = new Paint();
        circulomovimiento.setColor(Color.RED);
        circulorandom = new Paint();
        circulorandom.setColor(Color.BLUE);
        circulomovimiento1 = new RectF();
        circulorandom1= new RectF();


        // circulo rojo a la derecha
        circulomovimiento1.set(800, 200, 900, 300);

        // circulo azul izquierda
        resetearcirculo();
        handler = new Handler();
        moveRandomCircleRunnable = new Runnable() {
            @Override
            public void run() {
                mueverandomcirulo();
                checkCollision(); // Verifica la colisión después de cada movimiento
                handler.postDelayed(this, 16);
                startTime = System.currentTimeMillis();
                elapsedTime = 0;
            }
        };

        // Inicia el movimiento del círculo azul
        startMovingRandomCircle();
        // Inicializa el sonido de colisión
        colisioncancion = MediaPlayer.create(getContext(), R.raw.colision);

    }


//para que se reinicie el circulo en posicion aleatoria
    private void resetearcirculo() {
        Random random = new Random();
        int screenHeight = getHeight();
        float initialY = random.nextFloat() * (screenHeight - 100);
        circulorandom1.set(0, initialY, 100, initialY + 100);
    }

    private void mueverandomcirulo() {
        if (!gameOver) {
            long currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - startTime;
            float speedFactor = 1.0f + elapsedTime * SPEED_INCREASE_FACTOR;
            circulorandom1.left += INITIAL_SPEED * speedFactor;
           circulorandom1.right += INITIAL_SPEED * speedFactor;

            // Verifica si el círculo azul ha alcanzado el lado derecho de la pantalla
            if (circulorandom1.right > getWidth()) {
                resetearcirculo();
            }
            postInvalidate(); //  actualización de la vista
        }
    }


    private void checkCollision() {
        // Obtiene las posiciones de los centros de los círculos
        float movingCircleX = circulomovimiento1.centerX();
        float movingCircleY = circulomovimiento1.centerY();

        float randomCircleX = circulorandom1.centerX();
        float randomCircleY = circulorandom1.centerY();

        // Obtiene las distancias entre los centros de los círculos
        float distanceX = Math.abs(movingCircleX - randomCircleX);
        float distanceY = Math.abs(movingCircleY - randomCircleY);

        // Obtiene los radios de los círculos
        int movingCircleRadius = (int) circulomovimiento1.width() / 2;
        int randomCircleRadius = (int) circulorandom1.width() / 2;

        // Verifica si hay colisión comparando la distancia con la suma de los radios
        if (distanceX < movingCircleRadius + randomCircleRadius &&
                distanceY < movingCircleRadius + randomCircleRadius) {
            // Hay colisión, realiza las acciones necesarias
            gameOver = true;
            stopMovingRandomCircle();
            if (collisionListener != null) {
                collisionListener.onCollision();
            }

        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dibuja el círculo rojo
        canvas.drawOval(circulomovimiento1, circulomovimiento);

        // Dibuja el círculo azul
        canvas.drawOval(circulorandom1, circulorandom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameOver && event.getAction() == MotionEvent.ACTION_MOVE) {
            // Actualiza la posición del círculo rojo al tocar y mover
            circulomovimiento1.set(800, event.getY(), 900, event.getY() + 100);

            postInvalidate(); // Solicita una actualización de la vista
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Si cambia el tamaño de la vista, reinicia la posición del círculo azul
        resetearcirculo();
    }

    // Método para iniciar el movimiento del círculo azul
    public void startMovingRandomCircle() {
        handler.post(moveRandomCircleRunnable);
    }

    // Método para detener el movimiento del círculo azul
    public void stopMovingRandomCircle() {
        handler.removeCallbacks(moveRandomCircleRunnable);
    }

        }


