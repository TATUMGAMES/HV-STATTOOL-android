/**
 * Copyright 2013-present Tatum Games, LLC.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tatumgames.stattool.R;
import com.tatumgames.stattool.utils.Utils;

public class BaseFragment extends Fragment {

    @Nullable
    private OnRemoveFragment mOnRemoveFragment;

    /**
     * Set onRemoveListener used for inheritance
     *
     * @param fragment The Fragment to be removed
     */
    public void setOnRemoveListener(OnRemoveFragment fragment) {
        mOnRemoveFragment = fragment;
    }

    /**
     * Method is used to pop the top state off the back stack. Returns true if there
     * was one to pop, else false. This function is asynchronous -- it enqueues the
     * request to pop, but the action will not be performed until the application
     * returns to its event loop.
     */
    public void popBackStack() {
        if (!Utils.checkIfNull(getActivity())) {
            try {
                getActivity().getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    void addFragment(@NonNull Fragment fragment) {
        if (!Utils.checkIfNull(getActivity()) && !Utils.checkIfNull(getActivity().getSupportFragmentManager())) {
            // check if the fragment has been added already
            Fragment temp = getActivity().getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
            if (!Utils.checkIfNull(temp) && temp.isAdded()) {
                return;
            }

            // add fragment and transition with animation
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom,
                    R.anim.ui_slide_out_to_bottom, R.anim.ui_slide_in_from_bottom,
                    R.anim.ui_slide_out_to_bottom).add(R.id.frag_container, fragment,
                    fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

    /**
     * Method is used to add fragment to the current stack without animation
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    void addFragmentNoAnim(@NonNull Fragment fragment) {
        if (!Utils.checkIfNull(getActivity()) && !Utils.checkIfNull(getActivity().getSupportFragmentManager())) {
            // check if the fragment has been added already
            Fragment temp = getActivity().getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
            if (!Utils.checkIfNull(temp) && temp.isAdded()) {
                return;
            }

            // add fragment and transition with animation
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frag_container, fragment,
                    fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

    /**
     * Method is used to add fragment with replace to stack without animation.
     * When Fragment is replaced all current fragments on the backstack are removed.
     *
     * @param fragment The Fragment to be added
     */
    void addFragmentReplaceNoAnim(@NonNull Fragment fragment) {
        if (!Utils.checkIfNull(getActivity()) && !Utils.checkIfNull(getActivity().getSupportFragmentManager())) {
            // check if the fragment has been added already
            Fragment temp = getActivity().getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
            if (!Utils.checkIfNull(temp) && temp.isAdded()) {
                return;
            }

            // replace fragment and transition with animation
            try {
                if (!Utils.checkIfNull(getTopFragment()) &&
                        !Utils.isStringEmpty(getTopFragment().getTag()) && getTopFragment().isAdded()) {
                    // pop back stack
                    popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fragment,
                        fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                // used as last resort
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fragment,
                        fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
            }
        }
    }

    /**
     * Method for removing the Fragment view
     */
    void remove() {
        try {
            if (!Utils.checkIfNull(getActivity())) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.ui_slide_in_from_bottom, R.anim.ui_slide_out_to_bottom);
                ft.remove(this).commitAllowingStateLoss();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for removing the Fragment view with no animation
     */
    void removeNoAnim() {
        if (!Utils.checkIfNull(getActivity()) && !Utils.checkIfNull(getActivity().getSupportFragmentManager())) {
            try {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(this).commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method is used to retrieve the current fragment the user is on
     *
     * @return Returns the TopFragment if there is one, otherwise returns null
     */
    @Nullable
    private Fragment getTopFragment() {
        if (!Utils.checkIfNull(getActivity().getSupportFragmentManager())) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                int i = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                while (i >= 0) {
                    i--;
                    Fragment topFragment = getActivity().getSupportFragmentManager().getFragments().get(i);
                    if (!Utils.checkIfNull(topFragment)) {
                        return topFragment;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Method is used to re-direct to a different Activity with no transition
     *
     * @param context          Interface to global information about an application environment
     * @param activity         The in-memory representation of a Java class
     * @param intent           An intent is an abstract description of an operation to be performed
     * @param isClearBackStack If set in an Intent passed to Context.startActivity(),
     *                         this flag will cause any existing task that would be associated
     *                         with the activity to be cleared before the activity is started
     * @param isFinished       True to finish Activity otherwise false
     */
    void goToActivity(@NonNull Context context, @NonNull Class<?> activity, Intent intent,
                      boolean isClearBackStack, boolean isFinished) {
        Intent i;
        if (Utils.checkIfNull(intent)) {
            i = new Intent(context, activity);
        } else {
            i = intent;
        }
        if (isClearBackStack) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        if (!Utils.checkIfNull(getActivity()) && !getActivity().isFinishing()) {
            if (isFinished) {
                getActivity().finish();
            }
            startActivity(i);
        }
    }

    /**
     * Method is used to re-direct to different Activity from a fragment with a
     * transition animation slide in from bottom of screen
     *
     * @param context          Interface to global information about an application environment
     * @param activity         The in-memory representation of a Java class
     * @param isClearBackStack If set in an Intent passed to Context.startActivity(),
     *                         this flag will cause any existing task that would be associated
     *                         with the activity to be cleared before the activity is started
     * @param isFinished       True to finish Activity otherwise false
     */
    void goToActivityAnimInFromBottom(@NonNull Context context, @NonNull Class<?> activity,
                                      Intent intent, boolean isClearBackStack, boolean isFinished) {
        Intent i;
        if (Utils.checkIfNull(intent)) {
            i = new Intent(context, activity);
        } else {
            i = intent;
        }
        if (isClearBackStack) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        if (!Utils.checkIfNull(getActivity()) && !getActivity().isFinishing()) {
            if (isFinished) {
                getActivity().finish();
            }
            startActivity(i);
            // transition animation
            getActivity().overridePendingTransition(R.anim.ui_slide_in_from_bottom, R.anim.ui_slide_out_to_bottom);
        }
    }

    /**
     * Method is used to re-direct to different Activity from a fragment with a
     * transition animation slide in from bottom of screen
     *
     * @param context          Interface to global information about an application environment
     * @param activity         The in-memory representation of a Java class
     * @param isClearBackStack If set in an Intent passed to Context.startActivity(),
     *                         this flag will cause any existing task that would be associated
     *                         with the activity to be cleared before the activity is started
     * @param isFinished       True to finish Activity otherwise false
     */
    void goToActivityAnimInFromTop(@NonNull Context context, @NonNull Class<?> activity, Intent intent, boolean isClearBackStack, boolean isFinished) {
        Intent i;
        if (Utils.checkIfNull(intent)) {
            i = new Intent(context, activity);
        } else {
            i = intent;
        }
        if (isClearBackStack) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        if (!Utils.checkIfNull(getActivity()) && !getActivity().isFinishing()) {
            if (isFinished) {
                getActivity().finish();
            }
            startActivity(i);
            // transition animation
            getActivity().overridePendingTransition(R.anim.ui_slide_in_from_top, R.anim.ui_slide_out_to_top);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!Utils.checkIfNull(mOnRemoveFragment)) {
            mOnRemoveFragment.onRemove();
            mOnRemoveFragment = null;
        }
    }

    /**
     * Method for removing a fragment
     */
    public interface OnRemoveFragment {
        void onRemove();
    }
}
