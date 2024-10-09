package com.example.juntardinheiro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder> {

    private Context mContext;
    private List<Pessoa> mPessoas;

    public PessoaAdapter(Context context, List<Pessoa> pessoas) {
        mContext = context;
        mPessoas = pessoas != null ? pessoas : new ArrayList<>();  // Inicializa a lista de pessoas
    }

    @NonNull
    @Override
    public PessoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_pessoa, parent, false);
        return new PessoaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PessoaViewHolder holder, int position) {
        Pessoa pessoa = mPessoas.get(position);
        holder.bind(pessoa);
    }

    @Override
    public int getItemCount() {
        return mPessoas.size();
    }

    // Método para definir as pessoas no adaptador
    public void setPessoas(List<Pessoa> pessoas) {
        mPessoas = pessoas;
        notifyDataSetChanged();  // Notifica o RecyclerView sobre a mudança nos dados
    }

    public class PessoaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNome;
        private TextView textViewCpf;
        private TextView textViewTelefone;
        private TextView textViewEndereco;
        private TextView textViewBairro;
        private TextView textViewCidade;
        private TextView textViewCep;
        private TextView textViewRg;

        public PessoaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewCpf = itemView.findViewById(R.id.textViewCpf);
            textViewTelefone = itemView.findViewById(R.id.textViewTelefone);
            textViewEndereco = itemView.findViewById(R.id.textViewEndereco);
            textViewBairro = itemView.findViewById(R.id.textViewBairro);
            textViewCidade = itemView.findViewById(R.id.textViewCidade);
            textViewCep = itemView.findViewById(R.id.textViewCep);
            textViewRg = itemView.findViewById(R.id.textViewRg);
        }

        public void bind(Pessoa pessoa) {
            textViewNome.setText("Nome: " + pessoa.getNome());
            textViewCpf.setText("CPF: " + pessoa.getCpf());
            textViewTelefone.setText("Telefone: " + pessoa.getTelefone());
            textViewEndereco.setText("Endereço: " + pessoa.getEndereco());
            textViewBairro.setText("Bairro: " + pessoa.getBairro());
            textViewCidade.setText("Cidade: " + pessoa.getCidade());
            textViewCep.setText("CEP: " + pessoa.getCep());
            textViewRg.setText("RG: " + pessoa.getRg());
        }
    }
}