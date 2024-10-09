package com.example.juntardinheiro;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MostrarPessoasActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPessoas;
    private PessoaAdapter pessoaAdapter;
    private PessoaDatabaseHelper pessoaDatabaseHelper;
    private SearchView searchView;
    private List<Pessoa> listaPessoas = new ArrayList<>();  // Lista original de pessoas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_pessoas); // Certifique-se de que este layout está criado

        recyclerViewPessoas = findViewById(R.id.recyclerViewPessoas);
        searchView = findViewById(R.id.searchView);  // Campo de pesquisa

        pessoaAdapter = new PessoaAdapter(this, listaPessoas);
        recyclerViewPessoas.setAdapter(pessoaAdapter);
        recyclerViewPessoas.setLayoutManager(new LinearLayoutManager(this));

        pessoaDatabaseHelper = new PessoaDatabaseHelper();

        // Carregar pessoas do banco de dados
        loadPessoas();

        // Configura o listener para filtrar a lista conforme o usuário digita no SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;  // Não faz nada ao enviar o texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra os resultados
                filtrarPessoas(newText);
                return true;
            }
        });
    }

    private void loadPessoas() {
        // Execute a tarefa de carregamento em uma thread separada
        new LoadPessoasTask().execute();
    }

    private void filtrarPessoas(String texto) {
        List<Pessoa> listaFiltrada = new ArrayList<>();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaPessoas);  // Mostra todas as pessoas se o campo de pesquisa estiver vazio
        } else {
            String textoFiltrado = texto.toLowerCase();
            for (Pessoa pessoa : listaPessoas) {
                if (pessoa.getNome().toLowerCase().startsWith(textoFiltrado)) {
                    listaFiltrada.add(pessoa);
                }
            }
        }
        pessoaAdapter.setPessoas(listaFiltrada);  // Atualiza o adaptador com a lista filtrada
    }

    private class LoadPessoasTask extends AsyncTask<Void, Void, List<Pessoa>> {
        @Override
        protected List<Pessoa> doInBackground(Void... voids) {
            // Aqui você deve chamar o método que recupera as pessoas do banco de dados
            return pessoaDatabaseHelper.getPessoas();
        }

        @Override
        protected void onPostExecute(List<Pessoa> pessoas) {
            listaPessoas = pessoas;  // Armazena a lista completa de pessoas
            pessoaAdapter.setPessoas(pessoas);  // Atualiza o adaptador com as pessoas recuperadas
        }
    }
}