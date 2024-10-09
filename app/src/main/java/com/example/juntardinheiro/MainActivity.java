package com.example.juntardinheiro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private RequestQueue requestQueue;
    private SessionManager sessionManager; // Adicionando o SessionManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        sessionManager = new SessionManager(this); // Inicializando o SessionManager

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se os campos de login e senha estão preenchidos
                String login = editTextLogin.getText().toString();
                String senha = editTextPassword.getText().toString();
                if (login.isEmpty() || senha.isEmpty()) {
                    showToast("Por favor, preencha todos os campos");
                } else {
                    // Faz uma requisição HTTP para o serviço web PHP
                    requestLogin();
                }
            }
        });

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Inicializa a fila de requisições Volley
        requestQueue = Volley.newRequestQueue(this);

        // Verificar por atualizações
        checkForUpdates();
    }

    // Método para verificar por atualizações
    private void checkForUpdates() {
        UpdateChecker.checkForUpdate(this, new UpdateChecker.UpdateListener() {
            @Override
            public void onUpdateAvailable(String apkUrl, String changelog) {
                // Atualização disponível: baixar e instalar
                showToast("Nova atualização disponível!");
                new UpdateChecker().downloadAndInstallApk(apkUrl, MainActivity.this);
            }

            @Override
            public void onNoUpdate() {
                // Nenhuma atualização disponível
                Log.d("MainActivity", "Nenhuma atualização disponível.");
            }
        });
    }

    // Método para abrir a tela de adicionar pessoa
    public void onClickButton3(View view) {
        Intent intent = new Intent(this, AdicionarPessoaActivity.class);
        startActivity(intent);
    }

    // Método para abrir a tela de buscar pessoas
    public void onClickButton4(View view) {
        Intent intent = new Intent(this, PessoaDatabaseHelper.class);
        startActivity(intent);
    }

    private void requestLogin() {
        String url = Url.BASE_URL + "check_login.php";

        // Faz uma requisição POST para o serviço web PHP
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MainActivity", "Resposta do servidor: " + response); // Log da resposta
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            Log.d("MainActivity", "Login success: " + success); // Log do sucesso do login

                            if (success) {
                                // Se o login for bem-sucedido, armazena as informações de sessão
                                String userCpf = jsonObject.getString("user_cpf");
                                sessionManager.createLoginSession(userCpf);
                                Log.d("MainActivity", "Abrindo TelaPrincipal");
                                Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                                startActivity(intent);
                            } else {
                                Log.d("MainActivity", "Login falhou");
                                showToast("Login falhou");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MainActivity", "Erro ao analisar resposta do servidor: " + e.getMessage());
                            showToast("Erro ao analisar resposta do servidor");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Erro na requisição: " + error.toString());
                        showToast("Erro na requisição: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parâmetros POST (login e senha)
                Map<String, String> params = new HashMap<>();
                params.put("login", editTextLogin.getText().toString());
                params.put("senha", editTextPassword.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    // Método para exibir Toasts de forma segura na thread principal
    private void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para capturar o clique no texto "Esqueceu a senha?"
    public void onForgotPasswordClick(View view) {
        String phoneNumber = "+5582994113716";  // Substitua pelo seu número de telefone com o código do país
        String message = "Olá, preciso de ajuda para recuperar minha senha.";

        // Verifica se o WhatsApp está instalado
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message)));
            startActivity(intent);
        } catch (Exception e) {
            // Se o WhatsApp não estiver instalado, você pode mostrar uma mensagem ou redirecionar para a Play Store
            showToast("O WhatsApp não está instalado no dispositivo.");
        }
    }
}
