package com.dlanuara.ejercicioc.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.dlanuara.ejercicioc.R;

public class LoadingAlertDialog extends AlertDialog {
    private @NonNull TextView lblText;

    protected LoadingAlertDialog(final @NonNull Context context) {
        super(context,false, null);
        LayoutInflater myLayout = LayoutInflater.from(context);
        View view = myLayout.inflate(R.layout.alertdialog_loading,null);
        setView(view);
        lblText = view.findViewById(R.id.lblText);
    }

    public void setText(@StringRes int id){
        lblText.setText(id);
    }

    public static LoadingAlertDialog show(@NonNull final Context context,  @StringRes final int textId){
        LoadingAlertDialog dialog = new LoadingAlertDialog(context);
        dialog.setText(textId);
        dialog.show();
        return dialog;
    }
}
