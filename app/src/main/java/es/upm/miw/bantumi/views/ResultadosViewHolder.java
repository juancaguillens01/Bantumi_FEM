package es.upm.miw.bantumi.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.model.resultados.Resultados;

public class ResultadosViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvNombreGanador;

    private final TextView tvSemillasGanador;

    private ResultadosViewHolder(View itemView) {
        super(itemView);
        tvNombreGanador = itemView.findViewById(R.id.tvNombreGanador);
        tvSemillasGanador = itemView.findViewById(R.id.tvSemillasGanador);
    }

    public void bind(Resultados resultados) {
        tvNombreGanador.setText(resultados.getNombreGanador());
        tvSemillasGanador.setText(resultados.getSemillasGanador().toString());
    }

    static ResultadosViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resultados_recyclerview_item, parent, false);
        return new ResultadosViewHolder(view);
    }
}
