package com.example.juntardinheiro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "LoginSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_CPF = "userCpf";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String userCpf) {
        editor.putString(KEY_USER_CPF, userCpf);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getUserCpf() {
        return pref.getString(KEY_USER_CPF, "");
    }

    public void checkLogin() {
        if (!isLoggedIn()) {
            // Se o usuário não estiver logado, redirecione para a tela de login
            Intent intent = new Intent(context, MainActivity.class);
            // Fecha todas as atividades anteriores para evitar que o usuário volte para a tela anterior após o logout
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void logoutUser() {
        // Limpa todos os dados da sessão
        editor.clear();
        editor.apply();
        // Redireciona o usuário para a tela de login
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Método para verificar se o usuário está logado
    public boolean isUserLoggedIn() {
        return isLoggedIn() && !getUserCpf().isEmpty();
    }
}

