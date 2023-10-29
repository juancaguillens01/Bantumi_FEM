package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.bantumi.dialogos.DeleteAlertDialog;
import es.upm.miw.bantumi.model.resultados.ResultadosViewModel;
import es.upm.miw.bantumi.views.ResultadosListAdapter;


public class ResultadosActivity extends AppCompatActivity {

    private ResultadosViewModel resultadosViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        this.setTitle(R.string.mejoresResultadosText);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_revert);
        }

        resultadosViewModel = new ViewModelProvider(this)
                .get(ResultadosViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.resultados_recyclerview_item);
        final ResultadosListAdapter adapter = new ResultadosListAdapter(new ResultadosListAdapter.ResultadosDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultadosViewModel.getMejoresResultados().observe(this, adapter::submitList);

        CardView cardView = findViewById(R.id.cvResultados);

        cardView.setOnClickListener(v -> {
            new DeleteAlertDialog().show(getSupportFragmentManager(), "DeleteAlertDialog");
        });

    }

    public ResultadosViewModel getResultadosViewModel() {
        return resultadosViewModel;
    }
}
