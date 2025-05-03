package co.edu.uniminuto.appproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import co.edu.uniminuto.appproject.apiexterna.OpenFDAClient;
import co.edu.uniminuto.appproject.apiexterna.OpenFDAResponse;
import co.edu.uniminuto.appproject.apiexterna.OpenFDAService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuscarMedicamentoActivity extends AppCompatActivity {
    private EditText etMedicamento;
    private TextView tvResultado;
    private Button btnBuscarMedi;
    private TextView tvMedicamentos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_medicamento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciar();
        btnBuscarMedi.setOnClickListener(this::buscarMedicamento);
        
    }

    private void buscarMedicamento(View view) {
        // Capturamos el nombre como final para poder usarlo dentro del Callback
        final String nombreParam = etMedicamento.getText().toString().trim();
        if (nombreParam.isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        OpenFDAService service = OpenFDAClient.getClient().create(OpenFDAService.class);
        Call<OpenFDAResponse> call = service.buscarProducto("drug.brand_name:" + nombreParam, 1);

        call.enqueue(new Callback<OpenFDAResponse>() {
            @Override
            public void onResponse(Call<OpenFDAResponse> call, Response<OpenFDAResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OpenFDAResponse.Result> resultados = response.body().getResults();
                    if (resultados != null && !resultados.isEmpty()) {
                        OpenFDAResponse.Result evento = resultados.get(0);

                        if (evento.getDrug() != null && !evento.getDrug().isEmpty()) {
                            OpenFDAResponse.Result.Drug drug = evento.getDrug().get(0);
                            // Asegúrate de que Animal no sea null
                            String especie = evento.getAnimal() != null
                                    ? evento.getAnimal().getSpecies()
                                    : "Desconocida";

                            String salida = "Marca: "   + drug.getBrandName()       + "\n"
                                    + "Activo: "  + drug.getActiveIngredient() + "\n"
                                    + "Vía: "     + drug.getRoute()            + "\n"
                                    + "Especie: " + especie;
                            if (evento.getReactions() != null && !evento.getReactions().isEmpty()) {
                                StringBuilder sb = new StringBuilder("\nEfectos adversos :\n");
                                for (OpenFDAResponse.Result.Reaction reaction : evento.getReactions()) {
                                    sb.append("- ").append(reaction.getDescription());
                                    if (reaction.getOutcome() != null) {
                                        sb.append(" (Resultado: ").append(reaction.getOutcome()).append(")");
                                    }
                                    sb.append("\n");

                                }
                                salida += sb.toString();
                            }
                            tvResultado.setText(salida);
                        } else {
                            tvResultado.setText("Este evento no lista ningún medicamento.");
                        }
                    } else {
                        tvResultado.setText("No se encontraron eventos para: " + nombreParam);
                    }
                } else {

                    tvResultado.setText("Error en la respuesta: " + response.code());
                    try {
                        Log.e("OpenFDA", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OpenFDAResponse> call, Throwable t) {
                tvResultado.setText("Error al conectar: " + t.getMessage());
                Log.e("OpenFDA", "onFailure", t);
            }
        });
    }

    private void iniciar() {
        context = this;
        etMedicamento = findViewById(R.id.etMedicamento);
        tvResultado = findViewById(R.id.tvResultado);
        btnBuscarMedi = findViewById(R.id.btnBuscarMedi);
        tvMedicamentos = findViewById(R.id.tvMedicamentos);

    }
}