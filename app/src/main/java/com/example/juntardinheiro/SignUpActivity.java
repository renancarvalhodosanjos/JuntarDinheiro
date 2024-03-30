package com.example.juntardinheiro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextLogin, editTextPassword, editTextCPF, editTextEmail;
    private Button buttonCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCPF = findViewById(R.id.editTextCPF);
        editTextEmail = findViewById(R.id.editTextEmail);

        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter os valores dos campos
                String fullName = editTextFullName.getText().toString().trim();
                String login = editTextLogin.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String cpf = editTextCPF.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                // Verificar se todos os campos foram preenchidos
                if (fullName.isEmpty() || login.isEmpty() || password.isEmpty() || cpf.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Enviar os dados para o backend
                    new CreateAccountTask().execute(fullName, login, password, cpf, email);
                }
            }
        });
    }

    private class CreateAccountTask extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... params) {
            String fullName = params[0];
            String login = params[1];
            String password = params[2];
            String cpf = params[3];
            String email = params[4];




            try {
                URL url = new URL("http://179.235.190.188:8888/juntardinheiro/create_account.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("fullName", "UTF-8") + "=" + URLEncoder.encode(fullName, "UTF-8") + "&" +
                        URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("cpf", "UTF-8") + "=" + URLEncoder.encode(cpf, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leitura da resposta, se necessário
                } else {
                    // Tratar erro na conexão
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(String result) {



            // Ação após a conclusão do processo
            // Por exemplo, exibir uma mensagem de sucesso
            Toast.makeText(SignUpActivity.this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();


        }
    }
}
