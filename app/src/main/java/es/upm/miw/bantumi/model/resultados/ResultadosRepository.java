package es.upm.miw.bantumi.model.resultados;

import android.app.Application;

public class ResultadosRepository {

    private ResultadosDAO resultadosDAO;

    ResultadosRepository(Application application) {
        ResultadosDatabase db = ResultadosDatabase.getDatabase(application);
        resultadosDAO = db.resultadosDAO();
    }

    void insert(Resultados resultados) {
        ResultadosDatabase.databaseWriteExecutor.execute(() -> {
            resultadosDAO.insert(resultados);
        });
    }
}
