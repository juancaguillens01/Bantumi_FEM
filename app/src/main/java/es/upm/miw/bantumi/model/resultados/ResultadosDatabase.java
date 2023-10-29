package es.upm.miw.bantumi.model.resultados;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Resultados.class}, version = 1, exportSchema = false)
public abstract class ResultadosDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "resultados_database";

    public abstract ResultadosDAO resultadosDAO();

    private static volatile ResultadosDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ResultadosDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ResultadosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ResultadosDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;

    }
}

