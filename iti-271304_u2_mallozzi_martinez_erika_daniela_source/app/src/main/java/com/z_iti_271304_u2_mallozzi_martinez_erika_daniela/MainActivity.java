package com.z_iti_271304_u2_mallozzi_martinez_erika_daniela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el CustomView
        customView = findViewById(R.id.customView);

        // Establecer la parábola inicial y = x²
        customView.setParabolaModel(1.0f, 0.0f, 0.0f); // Cambiar a flotantes

        // Configurar el listener para el doble clic en la parábola
        customView.setOnParabolaDoubleClickListener(new CustomView.OnParabolaDoubleClickListener() {
            @Override
            public void onParabolaDoubleClick() {
                float currentA = customView.getA(); // Cambiar a float
                float currentB = customView.getB();
                float currentC = customView.getC();
                showEditParabolaDialog(currentA, currentB, currentC);
            }
        });
    }

    // Método para mostrar el diálogo de edición de la parábola
    private void showEditParabolaDialog(float a, float b, float c) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_parabola, null);

        EditText editA = dialogView.findViewById(R.id.edit_a);
        EditText editB = dialogView.findViewById(R.id.edit_b);
        EditText editC = dialogView.findViewById(R.id.edit_c);

        editA.setText(String.valueOf(a));
        editB.setText(String.valueOf(b));
        editC.setText(String.valueOf(c));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Parábola");
        builder.setView(dialogView);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String aInput = editA.getText().toString().trim();
            String bInput = editB.getText().toString().trim();
            String cInput = editC.getText().toString().trim();

            // Validación de entradas
            if (aInput.isEmpty() || !isNumeric(aInput)) {
                Toast.makeText(this, "El valor de 'a' no puede ser cero (ax^2)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bInput.isEmpty() || !isNumeric(bInput)) {
                bInput = "0"; // Asignar cero si no se proporciona
            }

            if (cInput.isEmpty() || !isNumeric(cInput)) {
                cInput = "0"; // Asignar cero si no se proporciona
            }

            // Convertir a flotantes
            float newA = Float.parseFloat(aInput);
            float newB = Float.parseFloat(bInput);
            float newC = Float.parseFloat(cInput);

            // Verificar que 'a' no sea cero
            if (newA == 0.0f) {
                Toast.makeText(this, "El coeficiente 'a' no puede ser cero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar el modelo de la parábola en el CustomView
            customView.setParabolaModel(newA, newB, newC);
            customView.invalidate(); // Redibujar la vista con la nueva parábola
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    // Método para verificar si una cadena es numérica (modificado para flotantes)
    private boolean isNumeric(String str) {
        try {
            Float.parseFloat(str); // Cambiado a Float
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }





}
