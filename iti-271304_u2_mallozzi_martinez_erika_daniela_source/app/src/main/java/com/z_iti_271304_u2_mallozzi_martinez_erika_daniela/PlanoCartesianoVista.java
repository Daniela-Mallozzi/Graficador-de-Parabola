package com.z_iti_271304_u2_mallozzi_martinez_erika_daniela;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PlanoCartesianoVista extends View {

    private float originX;
    private float originY;
    private Paint axisPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private Paint parabolaPaint;

    // Valores fijos para los ejes
    private float[] valuesX = {0, 2, 4, 6, 8};
    private float[] valuesY = {0, 2, 4, 6, 8, 10, 12, 14, 16};

    public PlanoCartesianoVista(Context context) {
        super(context);

        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(5);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30); // Tamaño constante del texto

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY); // Color de la cuadrícula
        gridPaint.setStrokeWidth(2);

        parabolaPaint = new Paint();
        parabolaPaint.setColor(Color.RED);
        parabolaPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Mantener el origen en el centro de la pantalla
        originX = getWidth() / 2;
        originY = getHeight() / 2;

        // Dibujar cuadrícula y ejes
        drawGrid(canvas);
        drawAxis(canvas);
        drawParabola(canvas);
    }

    private void drawGrid(Canvas canvas) {
        // Dibujar líneas de la cuadrícula
        for (float x : valuesX) {
            float screenX = originX + x * 50; // Escala para ajustar la distancia entre los valores de X
            canvas.drawLine(screenX, -getHeight(), screenX, getHeight(), gridPaint);
        }

        for (float y : valuesY) {
            float screenY = originY - y * 50; // Escala para ajustar la distancia entre los valores de Y
            canvas.drawLine(-getWidth(), screenY, getWidth(), screenY, gridPaint);
        }
    }

    private void drawAxis(Canvas canvas) {
        // Dibujar eje X
        canvas.drawLine(-getWidth(), originY, getWidth(), originY, axisPaint); // Eje X

        // Dibujar eje Y
        canvas.drawLine(originX, -getHeight(), originX, getHeight(), axisPaint); // Eje Y

        // Dibujar valores en el eje X
        for (float x : valuesX) {
            float screenX = originX + x * 50;
            canvas.drawText(String.valueOf(x), screenX, originY + 20, textPaint);
        }

        // Dibujar valores en el eje Y
        for (float y : valuesY) {
            float screenY = originY - y * 50;
            canvas.drawText(String.valueOf(y), originX + 10, screenY, textPaint);
        }
    }

    public void drawParabola(Canvas canvas) {
        for (int x = -getWidth() / 2; x <= getWidth() / 2; x++) {
            float y = (float) Math.pow((x / 50), 2); // Ajustar la escala de la parábola
            canvas.drawPoint(originX + x, originY - y * 50, parabolaPaint);
        }
    }

    public void updateParabola(String newEquation) {
        // Lógica para actualizar la ecuación de la parábola
        invalidate();  // Redibujar
    }

    public boolean isParabolaTouched(float x, float y) {
        // Ajustar la escala de acuerdo al tamaño del plano
        float scale = 50;

        // Calcular las coordenadas de la parábola
        for (int px = -getWidth() / 2; px <= getWidth() / 2; px++) {
            // Calcular el valor de y para la parábola y ajustar por la escala y el origen
            float parabolaY = (float) Math.pow((px / scale), 2);
            float screenY = originY - parabolaY * scale; // Posición en la pantalla

            // Verificar si el punto está cerca de la parábola (con un margen de error)
            if (Math.abs(screenY - y) < 5 && Math.abs((originX + px) - x) < 5) {
                return true; // El punto toca la parábola
            }
        }
        return false; // El punto no toca la parábola
    }

}
