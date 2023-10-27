package es.upm.miw.bantumi.model.resultados;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ResultadosViewModel extends AndroidViewModel {

    private final ResultadosRepository resultadosRepository;
    private final LiveData<List<Resultados>> mejoresResultados;

    public ResultadosViewModel(Application application) {
        super(application);
        this.resultadosRepository = new ResultadosRepository(application);
        this.mejoresResultados = this.resultadosRepository.getMejoresResultados();
    }

    public void insert(Resultados resultados) {
        this.resultadosRepository.insert(resultados);
    }

    public void deleteAll() {
        this.resultadosRepository.deleteAll();
    }

    public LiveData<List<Resultados>> getMejoresResultados() {
        return this.mejoresResultados;
    }
}
