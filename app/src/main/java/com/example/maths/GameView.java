package com.example.maths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private Paint paint = new Paint();
    private QuestionManager questionManager;
    private QuestionModel currentQuestion;

    private int[] optionsX;
    private int optionsY = 670;
    private int optionBoxWidth = 260;
    private int optionBoxHeight = 130;
    private int optionBoxSpacing = 70;

    private float ballX, ballY, ballRadius = 56;
    private boolean dragging = false;
    private float dragStartX, dragStartY, dragEndX, dragEndY;
    private float velocityX, velocityY;

    private int hitOptionIndex = -1; // -1 = not hit
    private Boolean lastHitCorrect = null;
    private boolean showBall = true;

    private String popupMessage = "";
    private long popupShownTime = 0;
    private final int POPUP_DURATION_MS = 1300;

    private final int[] optionColors = {
            Color.parseColor("#FFECB3"), // Pastel Yellow
            Color.parseColor("#B3E5FC"),
            Color.parseColor("#A5D6A7"),
            Color.parseColor("#FFCCBC")
    };
    private final int[] optionTextColors = {
            Color.parseColor("#AF8700"),
            Color.parseColor("#046A9B"),
            Color.parseColor("#256029"),
            Color.parseColor("#B13C0D")
    };

    private final int backgroundColor = Color.parseColor("#eaf6fd");
    private final int shadowColor = Color.argb(60, 40, 40, 95);

    private float effectScale = 1f;
    private boolean bounceGrow = false;

    private boolean initialized = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        questionManager = new QuestionManager();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!initialized && w > 0 && h > 0) {
            loadNextQuestion();
            initialized = true;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
        try {
            thread.join();
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        updateOptionPositions();
        resetBall();
    }

    private void updateOptionPositions() {
        int n = currentQuestion.options.length;
        int totalWidth = n * optionBoxWidth + (n - 1) * optionBoxSpacing;
        int startX = (getWidth() - totalWidth) / 2 + optionBoxWidth / 2;

        optionsX = new int[n];
        for (int i = 0; i < n; i++) {
            optionsX[i] = startX + i * (optionBoxWidth + optionBoxSpacing);
        }
    }

    public void update() {
        // Bounce effect
        if (effectScale < 1.13f && bounceGrow) {
            effectScale += 0.06f;
        } else if (effectScale > 1f) {
            bounceGrow = false;
            effectScale -= 0.04f;
            if (effectScale < 1f) effectScale = 1f;
        }

        if (!dragging && (velocityX != 0 || velocityY != 0) && showBall) {
            ballX += velocityX;
            ballY += velocityY;
            // Contain ball within the screen
            if (ballX - ballRadius < 0) {
                ballX = ballRadius;
                velocityX = -velocityX * 0.6f;
            }
            if (ballX + ballRadius > getWidth()) {
                ballX = getWidth() - ballRadius;
                velocityX = -velocityX * 0.6f;
            }
            if (ballY - ballRadius < 0) {
                ballY = ballRadius;
                velocityY = -velocityY * 0.6f;
            }
            if (ballY + ballRadius > getHeight()) {
                ballY = getHeight() - ballRadius;
                velocityY = -velocityY * 0.6f;
            }
            // Friction
            velocityX *= 0.98f;
            velocityY *= 0.98f;
            if (Math.abs(velocityX) < 0.5f) velocityX = 0;
            if (Math.abs(velocityY) < 0.5f) velocityY = 0;
            // Check collision with options, only if not already hit
            if (hitOptionIndex == -1 && !dragging) {
                for (int i = 0; i < currentQuestion.options.length; i++) {
                    float boxLeft = optionsX[i] - optionBoxWidth / 2f;
                    float boxTop = optionsY - optionBoxHeight / 2f;
                    float boxRight = optionsX[i] + optionBoxWidth / 2f;
                    float boxBottom = optionsY + optionBoxHeight / 2f;
                    if (circleIntersectsRect(ballX, ballY, ballRadius, boxLeft, boxTop, boxRight, boxBottom)) {
                        hitOptionIndex = i;
                        boolean correct = (i == currentQuestion.answerIndex);
                        lastHitCorrect = correct;
                        showBall = false;
                        popupMessage = correct ? "🎉 Correct!" : "Oops! Try Again";
                        popupShownTime = System.currentTimeMillis();
                        bounceGrow = true;
                        effectScale = 1.05f;
                        if (correct) {
                            postDelayed(this::loadNextQuestion, POPUP_DURATION_MS);
                        } else {
                            postDelayed(this::resetCurrentQuestion, POPUP_DURATION_MS);
                        }
                        break;
                    }
                }
            }
        }
        // Hide popup after duration
        if (!showBall && popupMessage.length() > 0) {
            if (System.currentTimeMillis() - popupShownTime > POPUP_DURATION_MS) {
                popupMessage = "";
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!showBall) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.hypot(ballX - event.getX(), ballY - event.getY()) < ballRadius && hitOptionIndex == -1) {
                    dragging = true;
                    dragStartX = ballX;
                    dragStartY = ballY;
                    dragEndX = event.getX();
                    dragEndY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    dragEndX = event.getX();
                    dragEndY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dragging) {
                    // Velocity based on the pull direction, capped for reasonable speed!
                    velocityX = (dragStartX - dragEndX) / 10f;
                    velocityY = (dragStartY - dragEndY) / 10f;
                    dragging = false;
                }
                break;
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 1. BACKGROUND GRADIENT
        RectF fullRect = new RectF(0, 0, getWidth(), getHeight());
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, getHeight(),
                Color.parseColor("#e3f8ff"),
                Color.parseColor("#bdf0fa"),
                Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
        canvas.drawRect(fullRect, paint);
        paint.setShader(null);

        // 2. INSTRUCTIVE PROMPT
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(8, 0, 6, shadowColor);
        float panelW = getWidth() * 0.76f, panelH = 125f;
        float panelX = (getWidth() - panelW) / 2f;
        float panelY = 58;
        RectF panelRect = new RectF(panelX, panelY, panelX + panelW, panelY + panelH);
        canvas.drawRoundRect(panelRect, 46, 46, paint);
        paint.clearShadowLayer();
        paint.setColor(Color.parseColor("#2B2B4A"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(45f);
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        canvas.drawText("Shoot the correct answer!", getWidth() / 2f, panelY + 72, paint);

        // 3. MATH QUESTION BUBBLE
        float qW = getWidth() * 0.60f;
        float qH = 130;
        float qX = (getWidth() - qW) / 2f;
        float qY = panelY + panelH + 38;
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(10, 0, 8, shadowColor);
        RectF qRect = new RectF(qX, qY, qX + qW, qY + qH);
        canvas.drawRoundRect(qRect, 46, 46, paint);
        paint.clearShadowLayer();
        paint.setColor(Color.parseColor("#3557a3"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(65f);
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        canvas.drawText(currentQuestion.question, getWidth() / 2f, qY + qH / 2f + 10, paint);

        // 4. OPTIONS (answer cards)
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(60);
        for (int i = 0; i < currentQuestion.options.length; i++) {
            int optX = optionsX[i];
            int optY = optionsY;
            float thisScale = (hitOptionIndex == i && effectScale > 1f) ? effectScale : 1f;
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(10, 0, 8, shadowColor);
            RectF box = new RectF(
                    optX - optionBoxWidth / 2 * thisScale,
                    optY - optionBoxHeight / 2 * thisScale,
                    optX + optionBoxWidth / 2 * thisScale,
                    optY + optionBoxHeight / 2 * thisScale
            );
            canvas.drawRoundRect(box, 60 * thisScale, 60 * thisScale, paint);
            paint.clearShadowLayer();

            // Filled color
            if (hitOptionIndex == i && lastHitCorrect != null) {
                paint.setColor(lastHitCorrect ? Color.parseColor("#81C784") : Color.parseColor("#ff8a80"));
            } else {
                paint.setColor(optionColors[i % optionColors.length]);
            }
            canvas.drawRoundRect(box, 60 * thisScale, 60 * thisScale, paint);

            paint.setColor(optionTextColors[i % optionTextColors.length]);
            paint.setTextSize(60 * thisScale);
            canvas.drawText(String.valueOf(currentQuestion.options[i]), optX, optY + 22 * thisScale, paint);
        }

        // 5. BALL (player) with DRAGGABLE arc and modern style
        if (showBall) {
            if (dragging) {
                // a. Draw the dotted / faded drag trajectory arc
                drawDottedTrajectory(canvas, ballX, ballY, dragEndX, dragEndY);
            }

            // b. Draw a soft shadow under the ball
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(90, 44, 44, 44));
            canvas.drawOval(ballX - ballRadius * 0.82f, ballY + ballRadius * 0.55f,
                    ballX + ballRadius * 0.82f, ballY + ballRadius * 1.09f, paint);

            // c. Draw the ball itself (with gradient similar to your reference image)
            RadialGradient rg = new RadialGradient(
                    ballX - ballRadius/3, ballY - ballRadius/3,
                    ballRadius * 1.2f,
                    new int[]{ Color.parseColor("#E84B2B"), Color.parseColor("#D9301C"), Color.parseColor("#AE2411") },
                    new float[]{0f, 0.75f, 1f},
                    Shader.TileMode.CLAMP);
            paint.setShader(rg);
            canvas.drawCircle(ballX, ballY, ballRadius, paint);
            paint.setShader(null);

            // d. Draw a glossy highlight
            paint.setColor(Color.argb(96,255,255,255));
            canvas.drawOval(ballX-ballRadius*0.37f, ballY-ballRadius*0.77f, ballX+ballRadius*0.62f, ballY-ballRadius*0.18f, paint);

            // e. Draw drag line if dragging
            if (dragging) {
                paint.setColor(Color.argb(188, 220, 0, 0));
                paint.setStrokeWidth(10);
                canvas.drawLine(ballX, ballY, dragEndX, dragEndY, paint);
            }
        }

        // 6. POPUP MESSAGE
        if (popupMessage.length() > 0) {
            paint.setShadowLayer(12, 0, 9, Color.argb(140, 60, 65, 105));
            paint.setTextSize(90);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setColor(lastHitCorrect != null && lastHitCorrect ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"));
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(popupMessage, getWidth() / 2, getHeight() * 0.47f, paint);
            paint.clearShadowLayer();
        }
    }

    private void drawDottedTrajectory(Canvas canvas, float x0, float y0, float x1, float y1) {
        // Dotted/dashed trajectory, similar to the one in your image.
        float dx = (x0 - x1)/10f, dy = (y0 - y1)/10f;
        float vx = dx * 17, vy = dy * 17;

        float px = x0, py = y0;
        paint.setColor(Color.parseColor("#DA3632"));
        for (int i=0; i<12; i++) {
            px += vx * 0.06f;
            py += vy * 0.06f + (0.11f * i * i); // simulate parabolic arc
            float dotRad = ballRadius * (0.34f - i*0.018f);
            paint.setAlpha(105 + 9 * Math.max(0, 6 - i));
            canvas.drawCircle(px, py, dotRad, paint);
        }
        paint.setAlpha(255);
    }

    // ... utility and thread code remains unchanged below ...

    private boolean circleIntersectsRect(float cx, float cy, float cr, float left, float top, float right, float bottom) {
        float closestX = clamp(cx, left, right);
        float closestY = clamp(cy, top, bottom);
        float distX = cx - closestX;
        float distY = cy - closestY;
        return (distX * distX + distY * distY) <= (cr * cr);
    }

    private float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    private void loadNextQuestion() {
        currentQuestion = questionManager.nextQuestion();
        hitOptionIndex = -1;
        lastHitCorrect = null;
        showBall = true;
        popupMessage = "";
        effectScale = 1f;
        updateOptionPositions();
        resetBall();
    }

    private void resetCurrentQuestion() {
        hitOptionIndex = -1;
        lastHitCorrect = null;
        showBall = true;
        popupMessage = "";
        effectScale = 1f;
        resetBall();
    }

    private void resetBall() {
        ballX = getWidth() / 2f;
        ballY = getHeight() / 2f + 250;
        velocityX = 0;
        velocityY = 0;
        dragging = false;
    }

    private static class GameThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private final GameView gameView;
        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        gameView.update();
                        if (canvas != null)
                            gameView.draw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try {
                    sleep(17);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
