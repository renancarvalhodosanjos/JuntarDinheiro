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

public class PessoaDatabaseHelper {
    private static final String PHP_URL = Url.BASE_URL + "manipulapessoas.php"; // Substitua pela URL correta do arquivo PHP

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

    public void addPessoa(Pessoa pessoa) {
        try {
            JSONObject jsonPessoa = new JSONObject();
            jsonPessoa.put("nome", pessoa.getNome());
            jsonPessoa.put("cpf", pessoa.getCpf());
            jsonPessoa.put("telefone", pessoa.getTelefone());
            jsonPessoa.put("endereco", pessoa.getEndereco());
            jsonPessoa.put("bairro", pessoa.getBairro());
            jsonPessoa.put("cidade", pessoa.getCidade());
            jsonPessoa.put("cep", pessoa.getCep());
            jsonPessoa.put("rg", pessoa.getRg());
            jsonPessoa.put("data_nascimento", pessoa.getDataNascimento());  // Adicionando dataNascimento

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "addPessoa");
            postDataParams.put("pessoa", jsonPessoa);

            String response = sendPostRequest(postDataParams.toString());
            if (response != null) {
                Log.d("Response", response);
            } else {
                Log.e("Error", "Erro ao adicionar pessoa");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Pessoa> getPessoas() {
        List<Pessoa> pessoaList = new ArrayList<>();
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "getAllPessoas"); // Ação correta conforme o PHP

            String response = sendPostRequest(postDataParams.toString());
            if (response != null) {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Pessoa pessoa = new Pessoa(
                            jsonObject.getString("cpf"),
                            jsonObject.getString("nome"),
                            jsonObject.optString("data_nascimento"),
                            jsonObject.optString("endereco"),
                            jsonObject.optString("cep"),
                            jsonObject.optString("bairro"),
                            jsonObject.optString("cidade"),
                            jsonObject.optString("rg"),
                            jsonObject.optString("telefone")
                    );
                    pessoaList.add(pessoa);
                }
            } else {
                Log.e("Error", "Erro ao buscar pessoas");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pessoaList;
    }

    public void deletePessoa(String cpf) {
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "deletePessoa");
            postDataParams.put("cpf", cpf);

            String response = sendPostRequest(postDataParams.toString());
            if (response != null) {
                Log.d("Response", response);
            } else {
                Log.e("Error", "Erro ao deletar pessoa");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updatePessoa(Pessoa pessoa) {
        try {
            JSONObject jsonPessoa = new JSONObject();
            jsonPessoa.put("cpf", pessoa.getCpf());
            jsonPessoa.put("nome", pessoa.getNome());
            jsonPessoa.put("telefone", pessoa.getTelefone());
            jsonPessoa.put("endereco", pessoa.getEndereco());
            jsonPessoa.put("bairro", pessoa.getBairro());
            jsonPessoa.put("cidade", pessoa.getCidade());
            jsonPessoa.put("cep", pessoa.getCep());
            jsonPessoa.put("rg", pessoa.getRg());
            jsonPessoa.put("data_nascimento", pessoa.getDataNascimento());  // Adicionando dataNascimento

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("action", "updatePessoa");
            postDataParams.put("pessoa", jsonPessoa);

            String response = sendPostRequest(postDataParams.toString());
            if (response != null) {
                Log.d("Response", response);
            } else {
                Log.e("Error", "Erro ao atualizar pessoa");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
