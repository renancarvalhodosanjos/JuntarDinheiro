package com.example.juntardinheiro;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String PHP_URL = Url.BASE_URL + "manipulagastos.php";

    public String sendPostRequest(String postDataParams) {
        HttpURLConnection conn = null;
        try {
            URL requestUrl = new URL(PHP_URL);
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(postDataParams.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public void addGasto(Gasto gasto) {
        try {
            JSONObject jsonGasto = new JSONObject();
            jsonGasto.put("cpf_usuario", gasto.getCpfUsuario());
            jsonGasto.put("tipo_gasto", gasto.getTipoGasto());
            jsonGasto.put("valor", gasto.getValor());
            jsonGasto.put("data", gasto.getData());
            jsonGasto.put("descricao", gasto.getDescricao());

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "addGasto");
            postDataParams.put("gasto", jsonGasto);

            String response = sendPostRequest(postDataParams.toString());
            Log.d("Response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Gasto> getGastosPorCpf(String cpf) {
        List<Gasto> gastosList = new ArrayList<>();
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "getGastosPorCpf");
            postDataParams.put("cpf", cpf);

            String response = sendPostRequest(postDataParams.toString());
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Gasto gasto = new Gasto(
                        jsonObject.getInt("gasto_id"),
                        jsonObject.getString("cpf_usuario"),
                        jsonObject.getString("tipo_gasto"),
                        jsonObject.getString("valor"),
                        jsonObject.getString("data"),
                        jsonObject.getString("descricao")
                );
                gastosList.add(gasto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gastosList;
    }

    public void deleteGasto(int gastoId) {
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "deleteGasto");
            postDataParams.put("id", gastoId);

            String response = sendPostRequest(postDataParams.toString());
            Log.d("Response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateGasto(Gasto gasto) {
        try {
            JSONObject jsonGasto = new JSONObject();
            jsonGasto.put("gasto_id", gasto.getGastoId());
            jsonGasto.put("cpf_usuario", gasto.getCpfUsuario());
            jsonGasto.put("tipo_gasto", gasto.getTipoGasto());
            jsonGasto.put("valor", gasto.getValor());
            jsonGasto.put("data", gasto.getData());
            jsonGasto.put("descricao", gasto.getDescricao());

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "updateGasto");
            postDataParams.put("gasto", jsonGasto);

            String response = sendPostRequest(postDataParams.toString());
            Log.d("Response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

