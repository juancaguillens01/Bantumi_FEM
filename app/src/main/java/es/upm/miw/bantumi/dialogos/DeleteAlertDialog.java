package es.upm.miw.bantumi.dialogos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.ResultadosActivity;

public class DeleteAlertDialog extends DialogFragment {

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        final ResultadosActivity main = (ResultadosActivity) requireActivity();
        assert main != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoBorrarResultadosTitulo)
                .setMessage(R.string.txtDialogoBorrarResultadosPregunta)
                .setPositiveButton(
                        getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                main.getResultadosViewModel().deleteAll();
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}
