package es.upm.miw.bantumi.views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.upm.miw.bantumi.model.resultados.Resultados;

public class ResultadosListAdapter extends ListAdapter<Resultados, ResultadosViewHolder> {

    public ResultadosListAdapter(@NonNull DiffUtil.ItemCallback<Resultados> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ResultadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ResultadosViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ResultadosViewHolder holder, int position) {
        Resultados current = getItem(position);
        holder.bind(current);
    }

    public static class ResultadosDiff extends DiffUtil.ItemCallback<Resultados> {

        @Override
        public boolean areItemsTheSame(@NonNull Resultados oldItem, @NonNull Resultados newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Resultados oldItem, @NonNull Resultados newItem) {
            return oldItem.getFechaJuego().equals(newItem.getFechaJuego());
        }
    }
}
