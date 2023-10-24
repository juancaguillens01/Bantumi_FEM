package es.upm.miw.bantumi.dialogos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import es.upm.miw.bantumi.MainActivity;
import es.upm.miw.bantumi.R;

public class RecoverAlertDialog extends DialogFragment {

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) requireActivity();
        final String LOG_TAG = "MiW RecoverAlertDialog";

        assert main != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoRecuperarPartidaTitulo)
                .setMessage(R.string.txtDialogoRecuperarPartidaPregunta)
                .setPositiveButton(
                        getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(LOG_TAG, "Reiniciando partida");
                                main.recuperarPartida();
                                Snackbar.make(
                                        requireActivity().findViewById(android.R.id.content),
                                        getString(R.string.txtPartidaRecuperada),
                                        950
                                ).show();
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}

