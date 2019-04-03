package com.tatumgames.stattool.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tatumgames.stattool.R;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;

/**
 * Created by leonard on 5/2/2018.
 */

public class ErrorUtils {
    // how long to display Crouton before it dismisses
    private static final int DURATION = 7000;
    private boolean isDisplayed;
    private Crouton mCrouton;

    /**
     * @param activity An activity is a single, focused thing that the user can do
     * @param error    The error to display
     */
    public void showError(@NonNull final Activity activity, @NonNull final String error) {
        if (!isDisplayed) {
            try {
                View view = activity.getLayoutInflater().inflate(R.layout.error_layout, null, false);
                final Configuration CONFIGURATION_INFINITE = new Configuration.Builder().setDuration(DURATION).build();
                mCrouton = Crouton.make(activity, view, R.id.iv_error_close, CONFIGURATION_INFINITE);
                // set listener for crouton
                mCrouton.setLifecycleCallback(new LifecycleCallback() {
                    @Override
                    public void onDisplayed() {
                        // set flag
                        isDisplayed = true;
                    }

                    @Override
                    public void onRemoved() {
                        // reset
                        isDisplayed = false;
                    }
                });
                // instantiate views
                TextView tvErrorMessage = view.findViewById(R.id.tv_error);
                ImageView ivClose = view.findViewById(R.id.iv_error_close);
                // set text
                tvErrorMessage.setText(error);
                // set listener for close button
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // close crouton
                        mCrouton.hide();
                    }
                });

                // show error message
                mCrouton.show();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method is used to dismiss error message
     */
    public void dismiss() {
        if (!Utils.checkIfNull(mCrouton) && isDisplayed) {
            mCrouton.hide();
        }
    }

    /**
     * Method is used to check if message is showing
     *
     * @return True if message is showing, otherwise false
     */
    public boolean isShowing() {
        return isDisplayed;
    }
}
