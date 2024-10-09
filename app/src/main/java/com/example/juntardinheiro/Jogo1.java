package com.example.juntardinheiro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Jogo1 extends AppCompatActivity {
    private SnakeGameView snakeGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jogo1);  // Certifique-se de que o layout jogo1.xml existe

        snakeGameView = findViewById(R.id.snakeGameView);  // Ligando o SurfaceView no layout
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeGameView.resumeGame();  // Retoma o jogo quando a activity está ativa
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeGameView.pauseGame();  // Pausa o jogo quando a activity não está visível
    }
}
