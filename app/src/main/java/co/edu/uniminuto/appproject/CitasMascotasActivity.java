package co.edu.uniminuto.appproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import co.edu.uniminuto.appproject.entities.Citas;
import co.edu.uniminuto.appproject.entities.Mascotas;
import co.edu.uniminuto.appproject.repository.CitaRepository;
import co.edu.uniminuto.appproject.repository.MascotasRepository;

public class CitasMascotasActivity extends AppCompatActivity {
    private int idDueno;
    private ListView lvCitasMas;
    private int idMascota;
    private int idCitas;
    private EditText etFecha;
    private EditText etLugar;
    private EditText etHora;
    private EditText etDescripcion;
    private TextView tvCitas;

    private Button btnAgendar;
    private Button btnCitasPasadas;
    private Button btnProximas;
    private int year;
    private int month;
    private int day;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citas_mascotas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciar();
        btnAgendar.setOnClickListener(this::addCita);
        lvCitasMas.setOnItemClickListener(this::setOnItemClick);
        btnProximas.setOnClickListener(this::proximaCita);
        btnCitasPasadas.setOnClickListener(this::citasPasadas);






    }
    private void addCita(View view){
        idMascota = getIntent().getIntExtra("idMascota", -1);
        Toast.makeText(this, " Clic en agendar: idMascota: " + idMascota, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AddCitaActivity.class);
        intent.putExtra("idMascota", idMascota);
        startActivity(intent);




    }
    private void popUp(ArrayList<Citas> citas){
        StringBuilder sb = new StringBuilder();  // se crea un String builder que permite construir un texto, este se morstarar en un popup
        for (Citas cita : citas) {
            sb.append("Mascota: ").append(cita.getNombreMascota()).append("\n")
                    .append("Fecha: ").append(cita.getFecha()).append("\n")
                    .append("Lugar: ").append(cita.getLugar()).append("\n")
                    .append("Hora: ").append(cita.getHora()).append("\n")
                    .append("Descripción: ").append(cita.getDescripcion()).append("\n");

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Citas Pasadas");
        builder.setMessage(sb.toString());
        builder.setPositiveButton("Aceptar", null);
        builder.show();

    }

    private void citasPasadas(View view){
        CitaRepository citaRep = new CitaRepository(findViewById(R.id.main), context);
        ArrayList<Citas> citasPasadas = citaRep.getCitasPasadas(idMascota);
        if(!citasPasadas.isEmpty()){
            popUp(citasPasadas);

        }else {
            Toast.makeText(this, "No hay citas pasadas", Toast.LENGTH_LONG).show();
        }



    }
    private void proximaCita(View view){

        idMascota = getIntent().getIntExtra("idMascota", -1);
        CitaRepository citaRepository = new CitaRepository(view,context);
        Citas siguiente = citaRepository.getProximaCita(idMascota);
        if(siguiente != null){
            String txt = String.format("Proxima cita %s a las %s\nLugar: %s",
                    siguiente.getFecha(), siguiente.getHora(), siguiente.getLugar());
            new AlertDialog.Builder(this)
                    .setTitle("Próxima cita")
                    .setMessage(txt)
                    .setPositiveButton("Aceptar", null)
                    .show();



        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Próxima cita")
                    .setMessage("No hay citas programadas")
                    .setPositiveButton("Aceptar", null)
                    .show();

        }
    }
    private void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Citas selected = (Citas) parent.getItemAtPosition(position);
        idCitas = selected.getIdCita();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opcciones");
        builder.setItems(new String[]{"Editar", "Eliminar"}, (dialog, which) -> {
            if (which == 0) {
                DialogActualizar(view);

            }else if(which == 1){
                EliminarCita(view);

            }
        });
        builder.show();

    }

    private void DialogActualizar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_update_cita, null);
        etFecha = dialogView.findViewById(R.id.etFecha);
        etFecha.setOnClickListener(vista -> {
            Calendar calendar =Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day =calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,(view1, year1, month1, dayOfMonth) -> {
                String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                etFecha.setText(date);
            },year,month,day);
            datePickerDialog.show();
        });

        etLugar = dialogView.findViewById(R.id.etLugar);
        etHora = dialogView.findViewById(R.id.etHora);
        etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        builder.setView(dialogView);

        CitaRepository citaRep = new CitaRepository(view, context);
        Citas selected = citaRep.getCitaByID(idCitas);
        etFecha.setText(selected.getFecha());
        etLugar.setText(selected.getLugar());
        etHora.setText(selected.getHora());
        etDescripcion.setText(selected.getDescripcion());
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            Citas citas = new Citas();
            citas.setIdCita(idCitas);
            citas.setFecha(etFecha.getText().toString());
            citas.setLugar(etLugar.getText().toString());
            citas.setHora(etHora.getText().toString());
            citas.setDescripcion(etDescripcion.getText().toString());
            citaRep.updateCita(citas);
            listData(view);
            Toast.makeText(this, "Cita actualizada", Toast.LENGTH_LONG).show();

        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();



    }
    private void EliminarCita (View view) {
        CitaRepository citaRep = new CitaRepository(view, context);
        boolean correcto = citaRep.deleteCita(idCitas);
        if(correcto) {
            listData(view);
            Toast.makeText(this, "Cita eliminada", Toast.LENGTH_LONG).show();
        }


    }
    private void listData(View view){

        idMascota= getIntent().getIntExtra("idMascota", -1);

        if(idMascota != -1){
            CitaRepository citaRep = new CitaRepository(view,context);
            ArrayList<Citas> list = citaRep.getAllCitas(idMascota);


            if(list.size()>0){
              ArrayAdapter<Citas> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                lvCitasMas.setAdapter(adapter);

            }else{
                Toast.makeText(this, "No se encontraron citas para esta mascota", Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(this, "id no valido", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        listData(findViewById(R.id.main));




    }

    private void iniciar() {
        this.context = this;
        this.lvCitasMas = findViewById(R.id.lvCitasMas);
        this.btnAgendar = findViewById(R.id.btnAgendar);
        this.btnProximas = findViewById(R.id.btnProximas);
        this.btnCitasPasadas = findViewById(R.id.btnCitasPasadas);
        this.tvCitas = findViewById(R.id.tvCitas);
        listData(findViewById(R.id.main));
        String nombreMascota = getIntent().getStringExtra("nombreMascota");
        if(nombreMascota != null && tvCitas !=null){
            tvCitas.setText("No olvides  las citas de "+ nombreMascota);
        }


    }
}