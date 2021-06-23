package ch.bbcag.cineboi.helper;

import android.app.Activity;
import android.app.AlertDialog;

public class AlertDialogHelper {

    public void generateAlertDialog(Activity activity) {
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
            activity.finish();
        });
        dialogBuilder.setMessage("Die Filme konnten nicht geladen werden. Versuche es später nochmals.").setTitle("Fehler");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
