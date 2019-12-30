/**
 * Copyright 2013-present Tatum Games, LLC.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

import com.tatumgames.stattool.R
import com.tatumgames.stattool.utils.Utils
import com.tatumgames.stattool.volley.RequestManager

open class BaseActivity : AppCompatActivity() {

    var mRequestManager: RequestManager
    private var mFragmentManager: FragmentManager? = null

    /**
     * Method is used to retrieve the current fragment the user is on
     *
     * @return Returns the TopFragment if there is one, otherwise returns null
     */
    val topFragment: Fragment?
        get() {
            if (mFragmentManager!!.backStackEntryCount > 0) {
                var i = mFragmentManager!!.backStackEntryCount
                while (i >= 0) {
                    i--
                    val topFragment = mFragmentManager!!.fragments[i]
                    if (!Utils.checkIfNull(topFragment)) {
                        return topFragment
                    }
                }
            }
            return null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestManager = RequestManager(applicationContext)
        mFragmentManager = supportFragmentManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(BUG_19917_KEY, BUG_19917_VALUE)
        super.onSaveInstanceState(outState)
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    fun addFragment(fragment: Fragment) {
        // check if the fragment has been added already
        val temp = mFragmentManager!!.findFragmentByTag(fragment.javaClass.getSimpleName())
        if (!Utils.checkIfNull(temp) && temp.isAdded) {
            return
        }

        // add fragment and transition with animation
        mFragmentManager!!.beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom, R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom).add(R.id.frag_container, fragment,
                fragment.javaClass.getSimpleName()).addToBackStack(null).commit()
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment        The new Fragment that is going to replace the container
     * @param containerViewId Optional identifier of the container this fragment is to be placed in.
     * If 0, it will not be placed in a container
     */
    fun addFragment(fragment: Fragment, containerViewId: Int) {
        // check if the fragment has been added already
        val temp = mFragmentManager!!.findFragmentByTag(fragment.javaClass.getSimpleName())
        if (!Utils.checkIfNull(temp) && temp.isAdded) {
            return
        }

        // add fragment and transition with animation
        mFragmentManager!!.beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom, R.anim.ui_slide_in_from_bottom,
                R.anim.ui_slide_out_to_bottom).add(containerViewId, fragment,
                fragment.javaClass.getSimpleName()).addToBackStack(null).commit()
    }

    /**
     * Method is used to pop the top state off the back stack.
     * Returns true if there was one to pop, else false.
     */
    private fun popBackStack() {
        mFragmentManager!!.popBackStack()
    }

    /**
     * Method is used to pop the top state off the back stack.
     * Returns true if there was one to pop, else false.
     */
    fun popBackStack(name: String) {
        mFragmentManager!!.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /**
     * Method is used to remove a fragment
     *
     * @param fragment The fragment to be removed
     */
    internal fun removeFragment(fragment: Fragment) {
        try {
            val ft = mFragmentManager!!.beginTransaction()
            ft.remove(fragment).commitAllowingStateLoss()
            popBackStack()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    /**
     * Method is used to remove all fragments
     */
    fun removeAllFragments() {
        try {
            for (fragment in mFragmentManager!!.fragments) {
                if (!Utils.checkIfNull(fragment)) {
                    val ft = mFragmentManager!!.beginTransaction()
                    ft.remove(fragment).commit()
                    popBackStack(fragment.tag)
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

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
    fun goToActivity(context: Context, activity: Class<*>, intent: Intent,
                     isClearBackStack: Boolean, isFinished: Boolean) {
        val i: Intent
        if (Utils.checkIfNull(intent)) {
            i = Intent(context, activity)
        } else {
            i = intent
        }
        if (isClearBackStack) {
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        } else {
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        if (isFinished && !isFinishing) {
            finish()
        }
        startActivity(i)
    }

    companion object {

        private val BUG_19917_KEY = "WORKAROUND_FOR_BUG_19917_KEY"
        private val BUG_19917_VALUE = "WORKAROUND_FOR_BUG_19917_VALUE"
    }
}