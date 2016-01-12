/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Dialog fragment to show help
   Resource: http://developer.android.com/guide/topics/ui/dialogs.html*/

package nl.mprog.mume.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import nl.mprog.mume.R;

public class HelpDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.helptitle_dialog_title);
        builder.setMessage(R.string.helptext_dialog_message);
        builder.setNegativeButton(R.string.gobackhelp_dialog_negativebutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                // Android automatically closes the dialog
            }
        });

        return builder.create();
    }

}
