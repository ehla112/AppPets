package co.edu.uniminuto.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityHome extends AppCompatActivity {
    private Button btnInfoHumano;
    private Button btnInfoMascota;
    private Button btnCitasMedicas;
    private Button btnVacunas;
    private Button btnMedicamentos;
    private Button btnCerrarSesion;
    private int idDueno;
    private int idMascota;

    private int idUsuario;
    private Button btnInfoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        start();
        btnInfoMascota.setOnClickListener(this::startInfoMascota);
        btnInfoHumano.setOnClickListener(this::startInfoUser);
        btnCitasMedicas.setOnClickListener(this::startInfoCitasMedicas);
        btnVacunas.setOnClickListener(this::startVacunas);
        btnCerrarSesion.setOnClickListener(this::CerrarSesion);
        btnMedicamentos.setOnClickListener(this::startMedicamentos);

    }

    private void CerrarSesion(View view) {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

    }

    private void startMedicamentos(View view) {
        Intent intent = new Intent(this, BuscarMedicamentoActivity.class);
        startActivity(intent);
    }


    private void startInfoMascota(View view) {
        Intent intent = new Intent(this, dataPetsActivity.class);
        intent.putExtra("idDueno", idDueno);


        startActivity(intent);
        Toast.makeText(this, "idDueno enviado: " + idDueno, Toast.LENGTH_SHORT).show();


    }
    private void startInfoUser(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("idDueno", idDueno);
        startActivity(intent);
        Toast.makeText(this, "idDueno enviado: " + idDueno, Toast.LENGTH_SHORT).show();
    }
    private void startInfoCitasMedicas(View view) {
        Intent intent = new Intent(this, CitasActivity.class);
        intent.putExtra("idDueno", idDueno);
        startActivity(intent);

    }
    private void startVacunas (View view) {
        Intent intent = new Intent(this, ListPetsActivity.class);
        intent.putExtra("idDueno", idDueno);
        startActivity(intent);
        Toast.makeText(this, "idDueno enviado a vacunas: " + idDueno, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void start() {
        btnInfoHumano = findViewById(R.id.btnInfoHumano);
        btnInfoMascota = findViewById(R.id.btnInfoMascota);
        btnCitasMedicas = findViewById(R.id.btnCitasMedicas);
        btnVacunas = findViewById(R.id.btnVacunas);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnMedicamentos = findViewById(R.id.btnMedicamentos);
        idDueno = getIntent().getIntExtra("idDueno", -1);
        if (idDueno == -1) {
            Toast.makeText(this, "No se pudo obtener el ID del dueño", Toast.LENGTH_SHORT).show();
        }
    }
}