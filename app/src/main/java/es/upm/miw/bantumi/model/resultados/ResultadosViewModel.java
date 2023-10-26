package es.upm.miw.bantumi.model.resultados;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class ResultadosViewModel extends AndroidViewModel {

    private ResultadosRepository resultadosRepository;

    public ResultadosViewModel(Application application) {
        super(application);
        this.resultadosRepository = new ResultadosRepository(application);
    }

    public void insert(Resultados resultados) {
        resultadosRepository.insert(resultados);
    }
}
