package com.example.juntardinheiro;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CPFFormatter implements TextWatcher {
    private EditText editText;
    private String current = "";
    private String mask = "###.###.###-##";

    public CPFFormatter(EditText editText) {
        this.editText = editText;
        this.editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        String cpf = s.toString();

        // Verifica se o CPF está sendo apagado e retorna se estiver vazio
        if (cpf.isEmpty()) {
            return;
        }

        // Remove qualquer formatação anterior do CPF
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF possui mais de 11 dígitos e retorna se for o caso
        if (cpf.length() > 11) {
            return;
        }

        // Adiciona os pontos e o traço de acordo com a máscara
        StringBuilder formattedCpf = new StringBuilder();
        int maskIndex = 0;
        for (int i = 0; i < cpf.length(); i++) {
            if (maskIndex >= mask.length()) {
                break;
            }
            if (mask.charAt(maskIndex) == '#') {
                formattedCpf.append(cpf.charAt(i));
            } else {
                formattedCpf.append(mask.charAt(maskIndex));
                formattedCpf.append(cpf.charAt(i));
                maskIndex++;
            }
            maskIndex++;
        }

        // Define o texto formatado no EditText
        editText.removeTextChangedListener(this);
        editText.setText(formattedCpf.toString());
        editText.setSelection(formattedCpf.length());
        editText.addTextChangedListener(this);
    }
}
