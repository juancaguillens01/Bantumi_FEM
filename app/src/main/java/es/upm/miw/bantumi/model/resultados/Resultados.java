package es.upm.miw.bantumi.model.resultados;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Resultados")
public class Resultados {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "fechaJuego")
    private String fechaJuego;

    @ColumnInfo(name = "nombreJugador1")
    private String nombreJugador1;

    @ColumnInfo(name = "nombreGanador")
    private String nombreGanador;

    @ColumnInfo(name = "semillasJugador1")
    private Integer semillasJugador1;

    @ColumnInfo(name = "semillasJugador2")
    private Integer semillasJugador2;


    public Resultados(String fechaJuego, String nombreJugador1,
                      String nombreGanador, Integer semillasJugador1,
                      Integer semillasJugador2) {
        this.fechaJuego = fechaJuego;
        this.nombreJugador1 = nombreJugador1;
        this.nombreGanador = nombreGanador;
        this.semillasJugador1 = semillasJugador1;
        this.semillasJugador2 = semillasJugador2;
    }

    @NonNull
    public Integer getId() {
        return this.id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getFechaJuego() {
        return this.fechaJuego;
    }

    public void setFechaJuego(String fechaJuego) {
        this.fechaJuego = fechaJuego;
    }

    public String getNombreJugador1() {
        return this.nombreJugador1;
    }

    public void setNombreJugador1(String nombreJugador1) {
        this.nombreJugador1 = nombreJugador1;
    }

    public String getNombreGanador() {
        return this.nombreGanador;
    }

    public void setNombreGanador(String nombreGanador) {
        this.nombreGanador = nombreGanador;
    }

    public Integer getSemillasJugador1() {
        return this.semillasJugador1;
    }

    public void setSemillasJugador1(Integer semillasJugador1) {
        this.semillasJugador1 = semillasJugador1;
    }

    public Integer getSemillasJugador2() {
        return this.semillasJugador2;
    }

    public void setSemillasJugador2(Integer semillasJugador2) {
        this.semillasJugador2 = semillasJugador2;
    }


}
