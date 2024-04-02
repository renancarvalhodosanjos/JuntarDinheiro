package com.example.juntardinheiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaPrincipal extends AppCompatActivity {

    private SessionManager sessionManager;

    public void onClickButton1(View view) {
        Intent intent = new Intent(this, NovaActivity1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onClickButton2(View view) {
        Intent intent = new Intent(this, NovaActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa o SessionManager
        sessionManager = new SessionManager(this);

        // Obtém o CPF do usuário logado do SessionManager
        String userCpf = sessionManager.getUserCpf();

        // Formata o CPF no padrão "000.000.000-00"
        String formattedCpf = formatCpf(userCpf);

        // Encontra o TextView para exibir o CPF do usuário
        TextView textViewUserCpf = findViewById(R.id.textViewUserCpf);

        // Define o texto do TextView como o CPF do usuário formatado
        textViewUserCpf.setText("CPF do Usuário: " + formattedCpf);
    }

    // Método para formatar o CPF no padrão "000.000.000-00"
    private String formatCpf(String cpf) {
        if (cpf.length() != 11) {
            return cpf; // Se o CPF não tiver 11 dígitos, retorna o CPF sem formatação
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}

