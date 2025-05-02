package co.edu.uniminuto.appproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import co.edu.uniminuto.appproject.entities.Mascotas;
import co.edu.uniminuto.appproject.repository.MascotasRepository;



public class CitasActivity extends AppCompatActivity {
    private int idDueno;
    private int idMascota;
    private String nombreMascota;
    private ListView lvCitasMascotas;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciar();
        this.lvCitasMascotas.setOnItemClickListener(this::setOnItemClick);

    }
    private void listData(View view){
        idDueno= getIntent().getIntExtra("idDueno", -1);

        if(idDueno != -1){
            MascotasRepository mascotasRepository = new MascotasRepository(view,context);

            ArrayList<Mascotas> list = mascotasRepository.getAllMascotas(idDueno);

            if(list.size()>0){
                ArrayAdapter<Mascotas> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                lvCitasMascotas.setAdapter(arrayAdapter);

            }else{
                Toast.makeText(this, "No se encontraron mascotas", Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(this, "id no valido", Toast.LENGTH_LONG).show();
        }


    }
    private void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mascotas selected = (Mascotas) parent.getItemAtPosition(position);
        idMascota = selected.getIdMascota();
        nombreMascota = selected.getNombreMascota();
        Intent intent = new Intent(this, CitasMascotasActivity.class);
        intent.putExtra("idMascota", idMascota);
        intent.putExtra("nombreMascota", nombreMascota);
        startActivity(intent);

    }


    private void iniciar() {
        this.context = this;
        this.lvCitasMascotas = findViewById(R.id.lvCitasMascotas);
        listData(findViewById(R.id.main));

    }
}