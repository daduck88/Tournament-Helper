package com.tournament.helper.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Provides a method to show a Snackbar.
 */
public class SnackbarUtils {

    public static void showSnackbar(View v, String snackbarText, final UtilsCallback callback) {
        if (v == null || snackbarText == null) {
            return;
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_LONG).addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                callback.onDismissed();
            }
        }).show();
    }

    public interface UtilsCallback{
        void onDismissed();
    }
}
