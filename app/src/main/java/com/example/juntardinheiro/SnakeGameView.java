package com.example.juntardinheiro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private Paint paint;
    private SurfaceHolder holder;

    private ArrayList<int[]> snake;  // Corpo da cobra (lista de segmentos)
    private int[] food;              // Comida (posição)
    private int direction = 0;       // Direção (0: para cima, 1: direita, 2: baixo, 3: esquerda)
    private int screenWidth, screenHeight;
    private final int BLOCK_SIZE = 50;  // Tamanho do bloco
    private Handler handler = new Handler();

    public SnakeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paint = new Paint();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new int[]{5, 5});  // Posição inicial da cobra

        food = new int[2];
        generateFood();
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                drawGame(canvas);
                holder.unlockCanvasAndPost(canvas);

                updateGame();
                try {
                    Thread.sleep(200);  // Velocidade do jogo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawGame(Canvas canvas) {
        // Limpar a tela
        canvas.drawColor(Color.BLACK);

        // Desenhar a cobra
        paint.setColor(Color.GREEN);
        for (int[] part : snake) {
            canvas.drawRect(part[0] * BLOCK_SIZE, part[1] * BLOCK_SIZE,
                    (part[0] * BLOCK_SIZE) + BLOCK_SIZE, (part[1] * BLOCK_SIZE) + BLOCK_SIZE, paint);
        }

        // Desenhar a comida
        paint.setColor(Color.RED);
        if (food != null) {
            canvas.drawRect(food[0] * BLOCK_SIZE, food[1] * BLOCK_SIZE,
                    (food[0] * BLOCK_SIZE) + BLOCK_SIZE, (food[1] * BLOCK_SIZE) + BLOCK_SIZE, paint);
        }
    }

    private void updateGame() {
        // Atualizar posição da cobra baseado na direção
        int[] head = snake.get(0).clone();
        switch (direction) {
            case 0: head[1]--; break;  // Para cima
            case 1: head[0]++; break;  // Direita
            case 2: head[1]++; break;  // Para baixo
            case 3: head[0]--; break;  // Esquerda
        }

        // Verificar colisão com comida
        if (head[0] == food[0] && head[1] == food[1]) {
            snake.add(0, head);  // Aumenta a cobra
            generateFood();
        } else {
            snake.add(0, head);  // Move a cabeça
            snake.remove(snake.size() - 1);  // Remove o último segmento
        }

        // Verificar colisão com as bordas da tela
        if (head[0] < 0 || head[0] >= screenWidth / BLOCK_SIZE ||
                head[1] < 0 || head[1] >= screenHeight / BLOCK_SIZE) {
            isPlaying = false;  // Fim de jogo
        }

        // Verificar colisão com o próprio corpo
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i)[0] == head[0] && snake.get(i)[1] == head[1]) {
                isPlaying = false;  // Fim de jogo
            }
        }
    }

    private void generateFood() {
        if (screenWidth <= 0 || screenHeight <= 0) {
            return; // Não gera comida se as dimensões da tela não forem válidas
        }

        Random random = new Random();
        food[0] = random.nextInt(screenWidth / BLOCK_SIZE);
        food[1] = random.nextInt(screenHeight / BLOCK_SIZE);
    }

    public void resumeGame() {
        isPlaying = true;
        initGame();  // Inicializa o jogo ao retomar
        thread = new Thread(this);
        thread.start();
    }

    public void pauseGame() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        initGame(); // Inicializa o jogo com o tamanho da tela
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            // Determina a nova direção com base na posição do toque
            if (x > screenWidth / 2) {
                if (direction != 3) direction = 1;  // Direita
            } else {
                if (direction != 1) direction = 3;  // Esquerda
            }
            if (y > screenHeight / 2) {
                if (direction != 0) direction = 2;  // Para baixo
            } else {
                if (direction != 2) direction = 0;  // Para cima
            }
        }
        return true;
    }
}
