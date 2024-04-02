package com.example.juntardinheiro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextLogin, editTextPassword, editTextConfirmPassword, editTextCPF, editTextEmail;
    private Button buttonCreateAccount;
    private ImageView imageViewPasswordVisibilityToggle, imageViewConfirmPasswordVisibilityToggle;
    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextCPF = findViewById(R.id.editTextCPF);
        editTextEmail = findViewById(R.id.editTextEmail);
        imageViewPasswordVisibilityToggle = findViewById(R.id.imageViewPasswordVisibilityToggle);
        imageViewConfirmPasswordVisibilityToggle = findViewById(R.id.imageViewConfirmPasswordVisibilityToggle);

        // Adicionando máscara ao CPF
        editTextCPF.addTextChangedListener(new CPFFormatter(editTextCPF));

        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter os valores dos campos
                String fullName = editTextFullName.getText().toString().trim();
                String login = editTextLogin.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String cpf = editTextCPF.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                // Log dos dados recebidos
                Log.d("SignUpActivity", "Nome completo: " + fullName);
                Log.d("SignUpActivity", "Login: " + login);
                Log.d("SignUpActivity", "Senha: " + password);
                Log.d("SignUpActivity", "Confirmar Senha: " + confirmPassword);
                Log.d("SignUpActivity", "CPF: " + cpf);
                Log.d("SignUpActivity", "Email: " + email);

                // Verificar se todos os campos foram preenchidos
                if (fullName.isEmpty() || login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || cpf.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else if (!isValidCPF(cpf)) { // Verificar se o CPF é válido
                    Toast.makeText(SignUpActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) { // Verificar se as senhas coincidem
                    Toast.makeText(SignUpActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                } else {
                    // Enviar os dados para o backend
                    new CreateAccountTask().execute(fullName, login, password, cpf, email);
                }
            }
        });

        // Configurar o clique do ícone de visibilidade da senha
        imageViewPasswordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordVisible) {
                    // Tornar a senha invisível
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordVisibilityToggle.setImageResource(android.R.drawable.ic_menu_view); // Ícone de olho fechado
                } else {
                    // Tornar a senha visível
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    imageViewPasswordVisibilityToggle.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Ícone de olho aberto
                }
                passwordVisible = !passwordVisible;
            }
        });

        // Configurar o clique do ícone de visibilidade da senha de confirmação
        imageViewConfirmPasswordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmPasswordVisible) {
                    // Tornar a senha de confirmação invisível
                    editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewConfirmPasswordVisibilityToggle.setImageResource(android.R.drawable.ic_menu_view); // Ícone de olho fechado
                } else {
                    // Tornar a senha de confirmação visível
                    editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    imageViewConfirmPasswordVisibilityToggle.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Ícone de olho aberto
                }
                confirmPasswordVisible = !confirmPasswordVisible;
            }
        });
    }

    private boolean isValidCPF(String cpf) {
        // Implementação básica de validação de CPF
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"); // Verifica se o CPF possui o formato XXX.XXX.XXX-XX
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
                URL url = new URL(Url.BASE_URL + "create_account.php");
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

                // Log da resposta do servidor
                Log.d("CreateAccountTask", "Resposta do servidor: " + responseCode);

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

            // Voltar para a MainActivity
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finalizar a SignUpActivity para que o usuário não possa voltar para ela pressionando o botão "voltar"
        }
    }
}
