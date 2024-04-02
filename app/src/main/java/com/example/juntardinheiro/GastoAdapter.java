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

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private Context mContext;
    private List<Gasto> mGastos;

    public GastoAdapter(Context context) {
        mContext = context;
        mGastos = new ArrayList<>(); // Inicializa a lista de gastos vazia
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = mGastos.get(position);
        holder.bind(gasto);
    }

    @Override
    public int getItemCount() {
        return mGastos.size();
    }

    // Método para definir os gastos no adaptador
    public void setGastos(List<Gasto> gastos) {
        mGastos = gastos;
        notifyDataSetChanged(); // Notifica o RecyclerView sobre a mudança nos dados
    }

    public class GastoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTipoGasto;
        private TextView textViewValor;
        private TextView textViewData;
        private TextView textViewDescricao;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTipoGasto = itemView.findViewById(R.id.textViewTipoGasto);
            textViewValor = itemView.findViewById(R.id.textViewValor);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
        }

        public void bind(Gasto gasto) {
            textViewTipoGasto.setText("Tipo de Gasto: " + gasto.getTipoGasto());
            textViewValor.setText("Valor: " + gasto.getValor());
            textViewData.setText("Data: " + gasto.getData());
            textViewDescricao.setText("Descrição: " + gasto.getDescricao());
        }
    }
}

