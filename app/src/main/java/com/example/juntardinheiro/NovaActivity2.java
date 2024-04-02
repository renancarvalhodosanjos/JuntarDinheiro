package com.example.juntardinheiro;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NovaActivity2 extends AppCompatActivity {

    private SessionManager sessionManager;
    private RecyclerView recyclerViewGastos;
    private GastoAdapter gastoAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova2);

        // Inicializa o SessionManager
        sessionManager = new SessionManager(this);

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper();

        // Inicializa a RecyclerView e o adaptador

        recyclerViewGastos = findViewById(R.id.recyclerViewGastos);
        recyclerViewGastos.setLayoutManager(new LinearLayoutManager(this));
        gastoAdapter = new GastoAdapter(this); // Passa o contexto para o adaptador
        recyclerViewGastos.setAdapter(gastoAdapter);

        // Obtém o CPF do usuário logado do SessionManager
        String userCpf = sessionManager.getUserCpf();
        Log.d("UserCpf", userCpf); // Adicione esta linha para verificar o CPF obtido

        // Define o CPF do usuário no TextView
        TextView textViewUserCpf = findViewById(R.id.textViewUserCpf);
        textViewUserCpf.setText("CPF: " + userCpf);

        // Envie uma solicitação POST para o servidor em uma thread separada
        new SendPostRequestTask().execute(userCpf);
    }

    private class SendPostRequestTask extends AsyncTask<String, Void, List<Gasto>> {

        @Override
        protected List<Gasto> doInBackground(String... params) {
            String userCpf = params[0];
            return databaseHelper.getGastosPorCpf(userCpf);
        }

        @Override
        protected void onPostExecute(List<Gasto> gastos) {
            // Atualiza o adaptador com a lista de gastos
            gastoAdapter.setGastos(gastos);
            // Notifica o adaptador sobre a mudança nos dados
            gastoAdapter.notifyDataSetChanged();
        }
    }
}

