package es.upm.miw.bantumi.dialogos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import es.upm.miw.bantumi.JuegoBantumi;
import es.upm.miw.bantumi.MainActivity;
import es.upm.miw.bantumi.R;

public class RestartAlertDialog extends DialogFragment {

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) requireActivity();
        final String LOG_TAG = "MiW RestartAlertDialog";

        assert main != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoReiniciarPartidaTitulo)
                .setMessage(R.string.txtDialogoReiniciarPartidaPregunta)
                .setPositiveButton(
                        getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(LOG_TAG, "Reiniciando partida");
                                main.getJuegoBantumi().inicializar(JuegoBantumi.Turno.turnoJ1);
                                main.getChronometer().stop();
                                main.getChronometer().setBase(SystemClock.elapsedRealtime());
                                Snackbar.make(
                                        requireActivity().findViewById(android.R.id.content),
                                        getString(R.string.txtPartidaReiniciada),
                                        950
                                ).show();
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}
