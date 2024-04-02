package com.example.juntardinheiro;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CurrencyTextWatcher implements TextWatcher {

    private final DecimalFormat df;
    private final EditText editText;
    private String lastValue = "";

    public CurrencyTextWatcher(EditText editText) {
        this.df = new DecimalFormat("###,###,##0.00");
        this.editText = editText;
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não é necessário implementar nada aqui
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não é necessário implementar nada aqui
    }

    @Override
    public void afterTextChanged(Editable s) {
        String newValue = s.toString();
        if (!newValue.equals(lastValue)) {
            try {
                // Remove o formato atual para fazer a conversão
                String cleanString = newValue.replaceAll("[^\\d]", "");
                double parsed = Double.parseDouble(cleanString);
                String formatted = df.format(parsed / 100);

                lastValue = formatted;
                editText.setText(formatted);
                editText.setSelection(formatted.length());
            } catch (NumberFormatException e) {
                // Handle exception, if necessary
            }
        }
    }
}


