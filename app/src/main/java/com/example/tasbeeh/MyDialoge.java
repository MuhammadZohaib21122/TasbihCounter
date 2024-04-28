package com.example.tasbeeh;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class MyDialoge {




    public void showDialogWithThreeButtons(Context context, String title, String message, SelectListener listener, Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message) // Set the message
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                  }
                })

                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.onDeleteClicked(item);

                    }
                })

                .setNeutralButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.onItemClicked(item);

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
