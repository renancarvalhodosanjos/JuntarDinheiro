package com.example.juntardinheiro;

import android.content.Intent;
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
                    Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
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
    }

    private void requestLogin() {
        String url = Url.BASE_URL + "check_login.php";

        // Faz uma requisição POST para o serviço web PHP
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Exibe a resposta completa do servidor no Logcat
                        Log.d("MainActivity", "Resposta do servidor: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // Se o login for bem-sucedido, armazena as informações de sessão
                                String userCpf = jsonObject.getString("user_cpf");
                                sessionManager.createLoginSession(userCpf);

                                // Abre a TelaPrincipal
                                Log.d("MainActivity", "Abrindo TelaPrincipal");
                                Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                                startActivity(intent);
                            } else {
                                // Se não, exibe uma mensagem de erro
                                Log.d("MainActivity", "Login falhou");
                                Toast.makeText(MainActivity.this, "Login falhou", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MainActivity", "Erro ao analisar resposta do servidor: " + e.getMessage());
                            Toast.makeText(MainActivity.this, "Erro ao analisar resposta do servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Exibe mensagem de erro caso ocorra um erro na requisição
                        Log.e("MainActivity", "Erro na requisição: " + error.toString());
                        Toast.makeText(MainActivity.this, "Erro na requisição: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parâmetros POST (login e senha)
                Map<String, String> params = new HashMap<>();
                // Adiciona os parâmetros de login e senha
                params.put("login", editTextLogin.getText().toString());
                params.put("senha", editTextPassword.getText().toString());
                return params;
            }
        };

        // Adiciona a requisição à fila de requisições Volley
        requestQueue.add(stringRequest);
    }
}

