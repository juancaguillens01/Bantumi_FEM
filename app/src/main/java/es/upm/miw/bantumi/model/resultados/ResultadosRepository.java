package es.upm.miw.bantumi.model.resultados;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ResultadosRepository {

    private final ResultadosDAO resultadosDAO;
    private final LiveData<List<Resultados>> mejoresResultados;

    ResultadosRepository(Application application) {
        ResultadosDatabase db = ResultadosDatabase.getDatabase(application);
        this.resultadosDAO = db.resultadosDAO();
        this.mejoresResultados = resultadosDAO.getMejoresResultados();
    }

    void insert(Resultados resultados) {
        ResultadosDatabase.databaseWriteExecutor.execute(() -> {
            resultadosDAO.insert(resultados);
        });
    }
    void deleteAll() {
        ResultadosDatabase.databaseWriteExecutor.execute(this.resultadosDAO::deleteAll);
    }

    public LiveData<List<Resultados>> getMejoresResultados() {
        return this.mejoresResultados;
    }
}
