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

package com.tatumgames.stattool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tatumgames.stattool.R;
import com.tatumgames.stattool.utils.Utils;
import com.tatumgames.stattool.volley.RequestManager;

public class BaseActivity extends AppCompatActivity {

    private static final String BUG_19917_KEY = "WORKAROUND_FOR_BUG_19917_KEY";
    private static final String BUG_19917_VALUE = "WORKAROUND_FOR_BUG_19917_VALUE";

    public RequestManager mRequestManager;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestManager = new RequestManager(getApplicationContext());
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUG_19917_KEY, BUG_19917_VALUE);
        super.onSaveInstanceState(outState);
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    public void addFragment(@NonNull Fragment fragment) {
        // check if the fragment has been added already
        Fragment temp = mFragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
        if (!Utils.checkIfNull(temp) && temp.isAdded()) {
            return;
        }

        // add fragment and transition with animation
        mFragmentManager.beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom, R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom).add(R.id.frag_container, fragment,
                fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment        The new Fragment that is going to replace the container
     * @param containerViewId Optional identifier of the container this fragment is to be placed in.
     *                        If 0, it will not be placed in a container
     */
    public void addFragment(@NonNull Fragment fragment, int containerViewId) {
        // check if the fragment has been added already
        Fragment temp = mFragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
        if (!Utils.checkIfNull(temp) && temp.isAdded()) {
            return;
        }

        // add fragment and transition with animation
        mFragmentManager.beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom, R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom).add(containerViewId, fragment,
                fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    /**
     * Method is used to pop the top state off the back stack.
     * Returns true if there was one to pop, else false.
     */
    private void popBackStack() {
        mFragmentManager.popBackStack();
    }

    /**
     * Method is used to pop the top state off the back stack.
     * Returns true if there was one to pop, else false.
     */
    public void popBackStack(String name) {
        mFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Method is used to remove a fragment
     *
     * @param fragment The fragment to be removed
     */
    void removeFragment(Fragment fragment) {
        try {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment).commitAllowingStateLoss();
            popBackStack();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to remove all fragments
     */
    public void removeAllFragments() {
        try {
            for (Fragment fragment : mFragmentManager.getFragments()) {
                if (!Utils.checkIfNull(fragment)) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(fragment).commit();
                    popBackStack(fragment.getTag());
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to retrieve the current fragment the user is on
     *
     * @return Returns the TopFragment if there is one, otherwise returns null
     */
    @Nullable
    public Fragment getTopFragment() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            int i = mFragmentManager.getBackStackEntryCount();
            while (i >= 0) {
                i--;
                Fragment topFragment = mFragmentManager.getFragments().get(i);
                if (!Utils.checkIfNull(topFragment)) {
                    return topFragment;
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
     * @param isClearBackStack True to clear Activity backstack, otherwise false
     * @param isFinished       True to finish Activity otherwise false
     */
    public void goToActivity(@NonNull Context context, @NonNull Class<?> activity, Intent intent,
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

        if (isFinished && !isFinishing()) {
            finish();
        }
        startActivity(i);
    }
}