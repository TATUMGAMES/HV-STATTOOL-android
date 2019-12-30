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

package com.tatumgames.stattool.fragments

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction

import com.tatumgames.stattool.R
import com.tatumgames.stattool.utils.Utils

open class BaseFragment : Fragment() {

    private var mOnRemoveFragment: OnRemoveFragment? = null

    /**
     * Method is used to retrieve the current fragment the user is on
     *
     * @return Returns the TopFragment if there is one, otherwise returns null
     */
    private val topFragment: Fragment?
        get() {
            if (!Utils.checkIfNull<FragmentManager>(activity.supportFragmentManager)) {
                if (activity.supportFragmentManager.backStackEntryCount > 0) {
                    var i = activity.supportFragmentManager.backStackEntryCount
                    while (i >= 0) {
                        i--
                        val topFragment = activity.supportFragmentManager.fragments[i]
                        if (!Utils.checkIfNull(topFragment)) {
                            return topFragment
                        }
                    }
                }
            }
            return null
        }

    /**
     * Set onRemoveListener used for inheritance
     *
     * @param fragment The Fragment to be removed
     */
    fun setOnRemoveListener(fragment: OnRemoveFragment) {
        mOnRemoveFragment = fragment
    }

    /**
     * Method is used to pop the top state off the back stack. Returns true if there
     * was one to pop, else false. This function is asynchronous -- it enqueues the
     * request to pop, but the action will not be performed until the application
     * returns to its event loop.
     */
    fun popBackStack() {
        if (!Utils.checkIfNull<FragmentActivity>(activity)) {
            try {
                activity.supportFragmentManager.popBackStack()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    internal fun addFragment(fragment: Fragment) {
        if (!Utils.checkIfNull<FragmentActivity>(activity) && !Utils.checkIfNull<FragmentManager>(activity.supportFragmentManager)) {
            // check if the fragment has been added already
            val temp = activity.supportFragmentManager.findFragmentByTag(fragment.javaClass.getSimpleName())
            if (!Utils.checkIfNull(temp) && temp.isAdded) {
                return
            }

            // add fragment and transition with animation
            activity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom,
                    R.anim.ui_slide_out_to_bottom, R.anim.ui_slide_in_from_bottom,
                    R.anim.ui_slide_out_to_bottom).add(R.id.frag_container, fragment,
                    fragment.javaClass.getSimpleName()).addToBackStack(fragment.javaClass.getSimpleName()).commit()
        }
    }

    /**
     * Method is used to add fragment to the current stack without animation
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    internal fun addFragmentNoAnim(fragment: Fragment) {
        if (!Utils.checkIfNull<FragmentActivity>(activity) && !Utils.checkIfNull<FragmentManager>(activity.supportFragmentManager)) {
            // check if the fragment has been added already
            val temp = activity.supportFragmentManager.findFragmentByTag(fragment.javaClass.getSimpleName())
            if (!Utils.checkIfNull(temp) && temp.isAdded) {
                return
            }

            // add fragment and transition with animation
            activity.supportFragmentManager.beginTransaction().add(R.id.frag_container, fragment,
                    fragment.javaClass.getSimpleName()).addToBackStack(fragment.javaClass.getSimpleName()).commit()
        }
    }

    /**
     * Method is used to add fragment with replace to stack without animation.
     * When Fragment is replaced all current fragments on the backstack are removed.
     *
     * @param fragment The Fragment to be added
     */
    internal fun addFragmentReplaceNoAnim(fragment: Fragment) {
        if (!Utils.checkIfNull<FragmentActivity>(activity) && !Utils.checkIfNull<FragmentManager>(activity.supportFragmentManager)) {
            // check if the fragment has been added already
            val temp = activity.supportFragmentManager.findFragmentByTag(fragment.javaClass.getSimpleName())
            if (!Utils.checkIfNull(temp) && temp.isAdded) {
                return
            }

            // replace fragment and transition with animation
            try {
                if (!Utils.checkIfNull<Fragment>(topFragment) &&
                        !Utils.isStringEmpty(topFragment!!.tag) && topFragment!!.isAdded) {
                    // pop back stack
                    popBackStack()
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.frag_container, fragment,
                        fragment.javaClass.getSimpleName()).addToBackStack(fragment.javaClass.getSimpleName()).commit()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                // used as last resort
                activity.supportFragmentManager.beginTransaction().replace(R.id.frag_container, fragment,
                        fragment.javaClass.getSimpleName()).addToBackStack(fragment.javaClass.getSimpleName()).commitAllowingStateLoss()
            }

        }
    }

    /**
     * Method for removing the Fragment view
     */
    internal fun remove() {
        try {
            if (!Utils.checkIfNull<FragmentActivity>(activity)) {
                val ft = activity.supportFragmentManager.beginTransaction()
                ft.setCustomAnimations(R.anim.ui_slide_in_from_bottom, R.anim.ui_slide_out_to_bottom)
                ft.remove(this).commitAllowingStateLoss()
                activity.supportFragmentManager.popBackStack()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    /**
     * Method for removing the Fragment view with no animation
     */
    internal fun removeNoAnim() {
        if (!Utils.checkIfNull<FragmentActivity>(activity) && !Utils.checkIfNull<FragmentManager>(activity.supportFragmentManager)) {
            try {
                val ft = activity.supportFragmentManager.beginTransaction()
                ft.remove(this).commitAllowingStateLoss()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Method is used to re-direct to a different Activity with no transition
     *
     * @param context          Interface to global information about an application environment
     * @param activity         The in-memory representation of a Java class
     * @param intent           An intent is an abstract description of an operation to be performed
     * @param isClearBackStack If set in an Intent passed to Context.startActivity(),
     * this flag will cause any existing task that would be associated
     * with the activity to be cleared before the activity is started
     * @param isFinished       True to finish Activity otherwise false
     */
    internal fun goToActivity(context: Context, activity: Class<*>, intent: Intent,
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

        if (!Utils.checkIfNull<FragmentActivity>(getActivity()) && !getActivity().isFinishing) {
            if (isFinished) {
                getActivity().finish()
            }
            startActivity(i)
        }
    }

    /**
     * Method is used to re-direct to different Activity from a fragment with a
     * transition animation slide in from bottom of screen
     *
     * @param context          Interface to global information about an application environment
     * @param activity         The in-memory representation of a Java class
     * @param isClearBackStack If set in an Intent passed to Context.startActivity(),
     * this flag will cause any existing task that would be associated
     * with the activity to be cleared before the activity is started
     * @param isFinished       True to finish Activity otherwise false
     */
    internal fun goToActivityAnimInFromBottom(context: Context, activity: Class<*>,
                                              intent: Intent, isClearBackStack: Boolean, isFinished: Boolean) {
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

        if (!Utils.checkIfNull<FragmentActivity>(getActivity()) && !getActivity().isFinishing) {
            if (isFinished) {
                getActivity().finish()
            }
            startActivity(i)
            // transition animation
            getActivity().overridePendingTransition(R.anim.ui_slide_in_from_bottom, R.anim.ui_slide_out_to_bottom)
        }
    }

    /**
     * Method is used to re-direct to different Activity from a fragment with a
     * transition animation slide in from bottom of screen
     *
     * @param context          Interface to global information about an application environment
     * @param activity         The in-memory representation of a Java class
     * @param isClearBackStack If set in an Intent passed to Context.startActivity(),
     * this flag will cause any existing task that would be associated
     * with the activity to be cleared before the activity is started
     * @param isFinished       True to finish Activity otherwise false
     */
    internal fun goToActivityAnimInFromTop(context: Context, activity: Class<*>, intent: Intent, isClearBackStack: Boolean, isFinished: Boolean) {
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

        if (!Utils.checkIfNull<FragmentActivity>(getActivity()) && !getActivity().isFinishing) {
            if (isFinished) {
                getActivity().finish()
            }
            startActivity(i)
            // transition animation
            getActivity().overridePendingTransition(R.anim.ui_slide_in_from_top, R.anim.ui_slide_out_to_top)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (!Utils.checkIfNull<OnRemoveFragment>(mOnRemoveFragment)) {
            mOnRemoveFragment!!.onRemove()
            mOnRemoveFragment = null
        }
    }

    /**
     * Method for removing a fragment
     */
    interface OnRemoveFragment {
        fun onRemove()
    }
}
