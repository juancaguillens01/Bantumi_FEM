package es.upm.miw.bantumi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Locale;

import es.upm.miw.bantumi.dialogos.FinalAlertDialog;
import es.upm.miw.bantumi.dialogos.RecoverAlertDialog;
import es.upm.miw.bantumi.dialogos.RestartAlertDialog;
import es.upm.miw.bantumi.model.BantumiViewModel;
import es.upm.miw.bantumi.model.resultados.Resultados;
import es.upm.miw.bantumi.model.resultados.ResultadosViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW MainActivity";
    JuegoBantumi juegoBantumi;
    BantumiViewModel bantumiVM;
    int numInicialSemillas;
    ResultadosViewModel resultadosViewModel;
    SharedPreferences sharedPref;
    Chronometer chronometer;
    long duracionPartida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.resultadosViewModel = new ViewModelProvider(this).get(ResultadosViewModel.class);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String nombreJugador = this.getNombreJugador1();
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        tvJugador1.setText(nombreJugador);

        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);

        chronometer = findViewById(R.id.chronometer);

        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();
    }

    public JuegoBantumi getJuegoBantumi() {
        return this.juegoBantumi;
    }

    public Chronometer getChronometer() {
        return this.chronometer;
    }

    private String getNombreJugador1() {
        String nombreJugador = sharedPref.getString(getString(R.string.preferencesPlayerNameKey), getString(R.string.txtPlayer1));
        if (nombreJugador.isEmpty()) {
            nombreJugador = getString(R.string.txtPlayer1);
        }
        return nombreJugador;
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos   posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            case R.id.opcReiniciarPartida:
                this.reiniciarPartida();
                return true;

            case R.id.opcGuardarPartida:
                this.guardarPartida();
                this.mostrarSnackbarGuardarPartida();
                return true;

            case R.id.opcRecuperarPartida:
                this.posibilidadRecuperarPartida();
                return true;

            case R.id.opcMejoresResultados:
                this.obtenerMejoresResultados();
                return true;

            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    /**
     * Reinicia la partida
     */
    private void reiniciarPartida() {
        new RestartAlertDialog().show(getSupportFragmentManager(), "RESTART ALERT_DIALOG");
    }

    /**
     * Guarda la partida
     */
    private void guardarPartida() {
        this.duracionPartida = SystemClock.elapsedRealtime() - chronometer.getBase();
        String partidaSerializada = juegoBantumi.serializa();
        partidaSerializada += "\n" + this.duracionPartida;
        this.archivarPartida(partidaSerializada);
        Log.d(LOG_TAG, "Partida guardada: " + partidaSerializada);
    }

    /**
     * Muestra un snackbar indicando que la partida se ha guardado correctamente
     */
    private void mostrarSnackbarGuardarPartida() {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.txtPartidaGuardada),
                950
        ).show();
    }

    /**
     * Guarda la partida serializada en un fichero de texto guardado en la memoria interna del dispositivo
     *
     * @param partidaSerializada String que contiene la partida serializada
     */
    private void archivarPartida(String partidaSerializada) {
        try {
            FileOutputStream fos;

            fos = openFileOutput(getString(R.string.ficheroPartidasGuardadas), Context.MODE_PRIVATE);
            fos.write(partidaSerializada.getBytes());
            fos.close();
            Log.i(LOG_TAG, "Fichero cerrado correctamente");
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "FICHERO NO ENCONTRADO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR I/O: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera la partida guardada en un fichero de texto guardado en la memoria interna del dispositivo
     */
    private void posibilidadRecuperarPartida() {
        if (this.juegoBantumi.isPartidaEmpezada()) {
            new RecoverAlertDialog().show(getSupportFragmentManager(), "RECOVER ALERT_DIALOG");
        } else {
            this.recuperarPartida();
        }
    }

    /**
     * Obtiene la partida guardada en un fichero de texto guardado en la memoria interna del dispositivo
     */
    public void recuperarPartida() {
        Log.i(LOG_TAG, "Recuperando partida del fichero de texto");
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(getString(R.string.ficheroPartidasGuardadas))));
            String linea = fin.readLine();
            StringBuilder sb = new StringBuilder();
            sb.append(linea).append("\n");
            int i = 0;
            while (linea != null) {
                if (i == 2) {
                    this.chronometer.setBase(SystemClock.elapsedRealtime() - Long.parseLong(linea));
                }
                linea = fin.readLine();
                sb.append(linea).append("\n");
                i++;
            }

            this.juegoBantumi.deserializa(sb.toString());
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.txtPartidaRecuperada),
                    950
            ).show();
            Log.i(LOG_TAG, "Partida recuperada correctamente: " + linea);

            fin.close();
            Log.i(LOG_TAG, "Fichero cerrado correctamente");

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "FICHERO NO ENCONTRADO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR I/O: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void obtenerMejoresResultados() {
        Intent intent = new Intent(MainActivity.this, ResultadosActivity.class);
        startActivity(intent);
    }


    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        if (!this.juegoBantumi.isPartidaEmpezada()) {
            this.chronometer.setBase(SystemClock.elapsedRealtime());
            this.chronometer.start();
        }
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * Elige una posición aleatoria del campo del jugador2 y realiza la siembra
     * Si mantiene turno -> vuelve a jugar
     */
    void juegaComputador() {
        while (juegoBantumi.turnoActual() == JuegoBantumi.Turno.turnoJ2) {
            int pos = 7 + (int) (Math.random() * 6);    // posición aleatoria [7..12]
            Log.i(LOG_TAG, "juegaComputador(), pos=" + pos);
            if (juegoBantumi.getSemillas(pos) != 0 && (pos < 13)) {
                juegoBantumi.jugar(pos);
            } else {
                Log.i(LOG_TAG, "\t posición vacía");
            }
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        this.chronometer.stop();
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana " + this.getNombreJugador1()
                : "Gana Jugador 2";
        if (juegoBantumi.getSemillas(6) == 6 * numInicialSemillas) {
            texto = "¡¡¡ EMPATE !!!";
        }
        Snackbar.make(
                        findViewById(android.R.id.content),
                        texto,
                        Snackbar.LENGTH_LONG
                )
                .show();

        String nombreGanador;
        Integer semillasGanador;
        if (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas) {
            nombreGanador = this.getNombreJugador1();
            semillasGanador = juegoBantumi.getSemillas(6);
        } else if (juegoBantumi.getSemillas(6) < 6 * numInicialSemillas) {
            nombreGanador = getString(R.string.txtPlayer2);
            semillasGanador = juegoBantumi.getSemillas(13);
        } else {
            nombreGanador = getString(R.string.txtEmpate);
            semillasGanador = juegoBantumi.getSemillas(6); // al azar
        }

        String nombreJugador1 = this.getNombreJugador1();
        Integer semillasJugador1 = juegoBantumi.getSemillas(6);
        Integer semillasJugador2 = juegoBantumi.getSemillas(13);
        //chronometer.getBase();
        String fechaJuego = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaJuego = LocalDateTime.now().toString().replace("T", " ");
            if (fechaJuego.length() >= 19) {
                fechaJuego = fechaJuego.substring(0, 19);
            }
        }

        Resultados resultados = new Resultados(fechaJuego, nombreJugador1, nombreGanador,
                semillasJugador1, semillasJugador2, semillasGanador);
        this.resultadosViewModel.insert(resultados);


        // terminar
        new FinalAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
    }
}