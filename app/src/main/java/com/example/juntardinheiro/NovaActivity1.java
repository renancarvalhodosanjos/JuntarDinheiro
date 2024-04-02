package com.example.juntardinheiro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NovaActivity1 extends AppCompatActivity {

    private SessionManager sessionManager;
    private EditText editTextData;
    private EditText editTextValor;
    private EditText editTextDescricao;
    private Spinner spinnerTipoGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova1);

        // Inicializa o SessionManager
        sessionManager = new SessionManager(this);

        // Obtém o CPF do usuário logado do SessionManager
        String userCpf = sessionManager.getUserCpf();

        // Formata o CPF no padrão "000.000.000-00"
        String formattedCpf = formatCpf(userCpf);

        TextView textViewUserCpf = findViewById(R.id.textViewUserCpf);

        // Define o texto do TextView como o CPF do usuário formatado
        textViewUserCpf.setText("CPF do Usuário: " + formattedCpf);

        // Encontra os EditTexts para data, valor e descrição
        editTextData = findViewById(R.id.editTextData);

        editTextValor = findViewById(R.id.editTextValor);
        editTextValor.addTextChangedListener(new CurrencyTextWatcher(editTextValor));

        editTextDescricao = findViewById(R.id.editTextDescricao);

        // Encontra o Spinner para o tipo de gasto
        spinnerTipoGasto = findViewById(R.id.spinnerTipoGasto);

        // Obtém a data atual
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());

        // Define a data atual no campo de texto
        editTextData.setText(currentDate);

        // Define o OnClickListener para abrir o DatePickerDialog
        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém a data atual para exibir no DatePickerDialog
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Cria o DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NovaActivity1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Atualiza o texto do EditText com a data selecionada
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editTextData.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

                // Exibe o DatePickerDialog
                datePickerDialog.show();
            }
        });

        // Define o OnClickListener para o botão de inserção
        Button buttonInserir = findViewById(R.id.buttonInserir);
        buttonInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirDados();
            }
        });
    }

    // Método para formatar o CPF no padrão "000.000.000-00"
    private String formatCpf(String cpf) {
        if (cpf.length() != 11) {
            return cpf; // Se o CPF não tiver 11 dígitos, retorna o CPF sem formatação
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    // Método para inserir os dados no servidor PHP
    private void inserirDados() {
        // Recupera os valores dos EditTexts e do Spinner
        String data = editTextData.getText().toString();
        String valor = editTextValor.getText().toString();
        String descricao = editTextDescricao.getText().toString();
        String tipoGasto = spinnerTipoGasto.getSelectedItem().toString();

        // Obtém o CPF do usuário logado do SessionManager
        String cpfUsuario = sessionManager.getUserCpf();

        // Executa a tarefa assíncrona para inserir os dados no servidor
        new InserirDadosTask().execute(cpfUsuario, data, valor, descricao, tipoGasto);
    }

    private class InserirDadosTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cpfUsuario = params[0];
            String data = params[1];
            String valor = params[2];
            String descricao = params[3];
            String tipoGasto = params[4];

            try {
                // URL do script PHP de inserção de dados
                String urlStr = Url.BASE_URL + "inserirgastos.php";
                URL url = new URL(urlStr);

                // Abrir conexão HTTP
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Criar os parâmetros da requisição
                String postData = URLEncoder.encode("cpf_usuario", "UTF-8") + "=" + URLEncoder.encode(cpfUsuario, "UTF-8") + "&" +
                        URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8") + "&" +
                        URLEncoder.encode("valor", "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8") + "&" +
                        URLEncoder.encode("descricao", "UTF-8") + "=" + URLEncoder.encode(descricao, "UTF-8") + "&" +
                        URLEncoder.encode("tipo_gasto", "UTF-8") + "=" + URLEncoder.encode(tipoGasto, "UTF-8");

                // Escrever os dados na conexão
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Verificar o código de resposta
                int responseCode = conn.getResponseCode();
                Log.d("InserirDadosTask", "Código de resposta: " + responseCode);

                // Ler a resposta, se necessário
                // ...

                // Fechar a conexão
                conn.disconnect();
            } catch (Exception e) {
                Log.e("InserirDadosTask", "Erro ao inserir dados: " + e.getMessage());
                e.printStackTrace(); // Adicione esta linha para imprimir o stack trace da exceção
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Ação após a conclusão do processo
            // Por exemplo, exibir uma mensagem de sucesso
            Toast.makeText(NovaActivity1.this, "Dados inseridos com sucesso", Toast.LENGTH_SHORT).show();

            // Voltar para a MainActivity
            Intent intent = new Intent(NovaActivity1.this, TelaPrincipal.class);
            startActivity(intent);
            finish(); // Finalizar a NovaActivity1 para que o usuário não possa voltar para ela pressionando o botão "voltar"
        }
    }
}
