package com.z_iti_271304_u2_mallozzi_martinez_erika_daniela;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private Paint paintParabola;
    private Paint paintAxes;
    private Paint paintText;
    private Paint paintGrid; // Pintura para la cuadrícula
    private Paint paintPoint; // Pintura para los puntos interactivos
    private Path parabolaPath;
    private float zoomScale = 1f; // Factor de zoom
    private float offsetX = 0f; // Desplazamiento en X
    private float offsetY = 0f; // Desplazamiento en Y

    private float a = 1; // Coeficiente de la parábola (y = ax^2 + bx + c)
    private float b = 0;
    private float c = 0;

    private GestureDetector gestureDetector;
  //  private ScaleGestureDetector scaleGestureDetector;
    private OnParabolaDoubleClickListener doubleClickListener;

    private List<Point> points; // Lista para almacenar los puntos interactivos

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        gestureDetector = new GestureDetector(context, new GestureListener());
     //   scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        points = new ArrayList<>(); // Inicializar la lista de puntos
    }

    private void init() {
        // Pintura para la parábola
        paintParabola = new Paint();
        paintParabola.setColor(Color.BLUE);
        paintParabola.setStyle(Paint.Style.STROKE);
        paintParabola.setStrokeWidth(5f);

        // Pintura para los ejes
        paintAxes = new Paint();
        paintAxes.setColor(Color.BLACK);
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setStrokeWidth(3f);

        // Pintura para la cuadrícula
        paintGrid = new Paint();
        paintGrid.setColor(Color.LTGRAY);
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setStrokeWidth(1f);

        // Pintura para los puntos
        paintPoint = new Paint();
        paintPoint.setColor(Color.RED);
        paintPoint.setStyle(Paint.Style.FILL);
        paintPoint.setStrokeWidth(5f);

        // Pintura para los textos (valores de los ejes)
        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(17f);
        paintText.setStrokeWidth(2f);

        // Inicializar el Path para la parábola
        parabolaPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // Dibujar cuadrícula
        drawGrid(canvas, width, height);

        // Dibujar ejes
        drawAxes(canvas, width, height);

        // Dibujar parábola
        drawParabola(canvas, width, height);

        // Dibujar puntos interactivos
        drawPoints(canvas, width, height);

        // Dibujar valores en los ejes
        drawAxisLabels(canvas, width, height);
    }

    private void drawGrid(Canvas canvas, int width, int height) {
        // Establecer el espaciado de la cuadrícula sin zoom
        float spacing = 50; // Valor fijo para el espaciado
        float gridOffsetY = 83; // Ajusta este valor para mover la cuadrícula hacia arriba o abajo

        // Ampliar el rango de la cuadrícula
        for (float x = -width * 20; x < width * 20; x += spacing) { // Extender a -2*width y 2*width
            canvas.drawLine(x + offsetX, 0, x + offsetX, height, paintGrid);

        }
        for (float y = -height * 2; y < height * 2; y += spacing) { // Extender a -2*height y 2*height
            canvas.drawLine(0, y + offsetY + gridOffsetY, width, y + offsetY + gridOffsetY, paintGrid); // Aplicar el desplazamiento a la cuadrícula
        }
    }

    private void drawAxes(Canvas canvas, int width, int height) {
        // Ejes con offset y zoom
        // Dibujar eje X
        canvas.drawLine(0, height / 2 + offsetY, width, height / 2 + offsetY, paintAxes);

        // Dibujar eje Y, ajustando su posición hacia la izquierda
        float offsetForYAxis = -8; // Desplazamiento de 1 milímetro a la izquierda (ajusta según sea necesario)
        canvas.drawLine(width / 2 + offsetX + offsetForYAxis, 0, width / 2 + offsetX + offsetForYAxis, height, paintAxes);
    }

    private void drawParabola(Canvas canvas, int width, int height) {
        // Limpiar el Path de la parábola
        parabolaPath.reset();

        // Escala para la parábola (ajustable según sea necesario)
        float scaleX = 50f * zoomScale; // Escala horizontal ajustada por zoom
        float scaleY = 50f * zoomScale; // Escala vertical ajustada por zoom

        // Crear puntos para la parábola
        for (int x = -width / 2; x <= width / 2; x++) {
            float scaledX = (x / scaleX); // Ajustar X por el zoom
            float y = a * scaledX * scaledX + b * scaledX + c; // Usar los coeficientes a, b y c

            // Ajustar las coordenadas para dibujar en pantalla
            float screenX = width / 2 + x + offsetX - (b * zoomScale * 5); // Ajustar posición de la parábola en X, con corrección de b
            float screenY = height / 2 - y * scaleY + offsetY; // Ajustar posición de la parábola en Y

            if (x == -width / 2) {
                parabolaPath.moveTo(screenX, screenY);
            } else {
                parabolaPath.lineTo(screenX, screenY);
            }
        }

        // Dibujar la parábola
        canvas.drawPath(parabolaPath, paintParabola);
    }



    private void drawAxisLabels(Canvas canvas, int width, int height) {
        // Dibujar etiquetas para el eje X
        for (int i = -50; i <= 50; i++) { // Rango de -50 a 50 con incremento de 1
            float xPosition = width / 2 + i * 50 + offsetX; // Multiplica por 20 para ajustar el espacio
            canvas.drawText(String.valueOf(i), xPosition, height / 2 + offsetY + 30, paintText);
        }

        // Dibujar etiquetas para el eje Y
        for (int i = 1; i <= 50; i++) { // Desde 1 hasta 50
            float positiveYPosition = height / 2 + offsetY - (i * 50); // Ajusta el incremento a 20 para espaciar
            float negativeYPosition = height / 2 + offsetY + (i * 50); // Ajusta el incremento a 20 para espaciar

            canvas.drawText(String.valueOf(i), width / 2 + offsetX + 10, positiveYPosition, paintText); // Valores positivos
            canvas.drawText(String.valueOf(-i), width / 2 + offsetX + 10, negativeYPosition, paintText); // Valores negativos
        }
    }

    private void drawPoints(Canvas canvas, int width, int height) {
        // Dibujar los puntos interactivos
        for (Point point : points) {
            float screenX = width / 2 + point.x * 50 + offsetX; // Ajustar la posición del punto
            float screenY = height / 2 - point.y * 50 + offsetY; // Ajustar la posición del punto
            canvas.drawCircle(screenX, screenY, 10, paintPoint); // Dibujar punto
        }
    }

    // Método para establecer los coeficientes de la parábola
    public void setParabolaModel(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
        // Aquí puedes llamar a un método que redibuje la vista
        invalidate(); // Redibuja la vista si es necesario
    }


    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getC() {
        return c;
    }


    // Método para manejar el doble clic
    public void setOnParabolaDoubleClickListener(OnParabolaDoubleClickListener listener) {
        this.doubleClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      //  scaleGestureDetector.onTouchEvent(event); // Maneja el evento de escalado
        gestureDetector.onTouchEvent(event); // Maneja los gestos de toque

        return true;
    }

    // Listener para el gesto de doble clic
    public interface OnParabolaDoubleClickListener {
        void onParabolaDoubleClick();
    }

    // Clase interna para manejar gestos
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (doubleClickListener != null) {
                doubleClickListener.onParabolaDoubleClick();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Ajustar el desplazamiento (scroll/pan)
            offsetX -= distanceX;
            offsetY -= distanceY;
            invalidate();
            return true;
        }
    }

  /*  // Clase interna para manejar el escalado
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zoomScale *= detector.getScaleFactor(); // Ajustar el zoom
            invalidate();
            return true;
        }
    }*/

    // Clase interna para representar un punto interactivo
    public static class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }





}
