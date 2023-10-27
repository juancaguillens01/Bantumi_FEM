package es.upm.miw.bantumi.model.resultados;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultadosDAO {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Resultados resultados);

    @Query("delete from Resultados")
    void deleteAll();

    @Query("select * from Resultados order by semillasGanador desc limit 10")
    LiveData<List<Resultados>> getMejoresResultados();

}
