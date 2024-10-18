package com.example.juntardinheiro;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AdicionarPessoaActivity extends AppCompatActivity {

    private EditText editTextCpf, editTextNome, editTextDataNascimento, editTextEndereco,
            editTextCep, editTextBairro, editTextCidade, editTextRg, editTextTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pessoa);

        // Inicializa os EditTexts
        editTextCpf = findViewById(R.id.editTextCpf);
        editTextNome = findViewById(R.id.editTextNome);
        editTextDataNascimento = findViewById(R.id.editTextDataNascimento);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextCep = findViewById(R.id.editTextCep);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextRg = findViewById(R.id.editTextRg);
        editTextTelefone = findViewById(R.id.editTextTelefone);

        // Aplicar a formatação de CPF no campo CPF
        new CPFFormatter(editTextCpf);

        // Configura o botão para inserir dados
        Button buttonAdicionar = findViewById(R.id.buttonAdicionar);
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirPessoa();
            }
        });
    }

    // Método para inserir os dados no servidor PHP
    private void inserirPessoa() {
        // Recupera os valores dos EditTexts
        String cpf = editTextCpf.getText().toString().trim();
        String nome = editTextNome.getText().toString().trim();
        String dataNascimento = editTextDataNascimento.getText().toString().trim();
        String endereco = editTextEndereco.getText().toString().trim();
        String cep = editTextCep.getText().toString().trim();
        String bairro = editTextBairro.getText().toString().trim();
        String cidade = editTextCidade.getText().toString().trim();
        String rg = editTextRg.getText().toString().trim();
        String telefone = editTextTelefone.getText().toString().trim();

        // Valida campos obrigatórios
        if (cpf.isEmpty() || nome.isEmpty()) {
            Toast.makeText(this, "CPF e Nome são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Executa a tarefa assíncrona para inserir os dados no servidor
        new InserirPessoaTask().execute(cpf, nome, dataNascimento, endereco, cep, bairro, cidade, rg, telefone);
    }

    private class InserirPessoaTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cpf = params[0];
            String nome = params[1];
            String dataNascimento = params[2];
            String endereco = params[3];
            String cep = params[4];
            String bairro = params[5];
            String cidade = params[6];
            String rg = params[7];
            String telefone = params[8];

            try {
                // URL do script PHP para inserção
                String urlStr = Url.BASE_URL + "inserirpessoa.php"; // Atualize com o nome do seu script PHP
                URL url = new URL(urlStr);

                // Abrir conexão HTTP
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Criar os parâmetros da requisição
                String postData = URLEncoder.encode("cpf", "UTF-8") + "=" + URLEncoder.encode(cpf, "UTF-8") + "&" +
                        URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("data_nascimento", "UTF-8") + "=" + URLEncoder.encode(dataNascimento, "UTF-8") + "&" +
                        URLEncoder.encode("endereco", "UTF-8") + "=" + URLEncoder.encode(endereco, "UTF-8") + "&" +
                        URLEncoder.encode("cep", "UTF-8") + "=" + URLEncoder.encode(cep, "UTF-8") + "&" +
                        URLEncoder.encode("bairro", "UTF-8") + "=" + URLEncoder.encode(bairro, "UTF-8") + "&" +
                        URLEncoder.encode("cidade", "UTF-8") + "=" + URLEncoder.encode(cidade, "UTF-8") + "&" +
                        URLEncoder.encode("rg", "UTF-8") + "=" + URLEncoder.encode(rg, "UTF-8") + "&" +
                        URLEncoder.encode("telefone", "UTF-8") + "=" + URLEncoder.encode(telefone, "UTF-8");

                // Escrever os dados na conexão
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Verificar o código de resposta
                int responseCode = conn.getResponseCode();
                Log.d("InserirPessoaTask", "Código de resposta: " + responseCode);

                // Fechar a conexão
                conn.disconnect();
            } catch (Exception e) {
                Log.e("InserirPessoaTask", "Erro ao inserir dados: " + e.getMessage());
                e.printStackTrace(); // Adicione esta linha para imprimir o stack trace da exceção
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Ação após a conclusão do processo
            Toast.makeText(AdicionarPessoaActivity.this, "Pessoa inserida com sucesso", Toast.LENGTH_SHORT).show();
            finish(); // Finalizar a AdicionarPessoaActivity
        }
    }
}

