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
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView

import com.crashlytics.android.Crashlytics
import com.tatumgames.stattool.BuildConfig
import com.tatumgames.stattool.R
import com.tatumgames.stattool.fragments.ApiFragment
import com.tatumgames.stattool.helper.InputFilterMinMax
import com.tatumgames.stattool.listener.ShakeEventListener
import com.tatumgames.stattool.logger.Logger
import com.tatumgames.stattool.model.GuardianStatsModel
import com.tatumgames.stattool.model.MappingModel
import com.tatumgames.stattool.utils.DialogUtils
import com.tatumgames.stattool.utils.StatUtils
import com.tatumgames.stattool.utils.Utils
import com.tatumgames.stattool.utils.network.NetworkReceiver

import java.util.Random
import java.util.Timer
import java.util.TimerTask

import io.fabric.sdk.android.Fabric

class MainActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener, NetworkReceiver.NetworkStatusObserver {

    private var mNetworkReceiver: NetworkReceiver? = null
    private var mContext: Context? = null
    private var mStats: GuardianStatsModel? = null
    private var mStatUtils: StatUtils? = null
    private var mSensorManager: SensorManager? = null
    private var mSensorListener: ShakeEventListener? = null
    private var v: Vibrator? = null
    private var spnAffinity: Spinner? = null
    private var spnType: Spinner? = null
    private var edtLv: EditText? = null
    private var edtAsc: EditText? = null
    private var tvHp: TextView? = null
    private var tvStr: TextView? = null
    private var tvSpd: TextView? = null
    private var tvWis: TextView? = null
    private var tvPhyDef: TextView? = null
    private var tvMagDef: TextView? = null
    private var tvCrit: TextView? = null
    private var tvTooltip: TextView? = null
    private var tvMaxLv: TextView? = null
    private var tvMaxAsc: TextView? = null
    private var tvResetToBase: TextView? = null
    private var btnRegenerate: Button? = null
    private var llUpdateWrapper: LinearLayout? = null

    private var isDefaultSet: Boolean = false
    private var isPaused: Boolean = false
    private var isEditable: Boolean = false
    private var isBaseStatsSet: Boolean = false
    private var isAnimationStarted: Boolean = false
    private var mSelectedAffinity: String? = null
    private var mSelectedCardType: String? = null
    private var arryAffinity: Array<String>? = null
    private var arryType: Array<String>? = null
    private var mTimer: Timer? = null
    private var animation: Animation? = null

    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        // instantiate Fabric API
        Fabric.with(this, Crashlytics())
        // set the activity content from a layout resource
        setContentView(R.layout.activity_main)

        // intialize views and listeners
        initializeViews()
        initializeHandlers()
        initializeListeners()
    }

    /**
     * Method is used to instantiate objects and views
     */
    private fun initializeViews() {
        mContext = this@MainActivity
        mStats = GuardianStatsModel()
        mStatUtils = StatUtils()
        mTimer = Timer()
        mNetworkReceiver = NetworkReceiver()

        // initialize vibrator and sensor for device shake detector
        v = mContext!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mSensorListener = ShakeEventListener()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // constructor to use when building an AlphaAnimation from code
        animation = AlphaAnimation(1.0f, 0.4f)

        // initialize views
        llUpdateWrapper = findViewById(R.id.ll_update_wrapper)
        edtLv = findViewById(R.id.edt_lv)
        edtAsc = findViewById(R.id.edt_asc)
        tvHp = findViewById(R.id.tv_hp)
        tvStr = findViewById(R.id.tv_str)
        tvSpd = findViewById(R.id.tv_spd)
        tvWis = findViewById(R.id.tv_wis)
        tvPhyDef = findViewById(R.id.tv_phy_def)
        tvMagDef = findViewById(R.id.tv_mag_def)
        tvCrit = findViewById(R.id.tv_crit)
        tvTooltip = findViewById(R.id.tv_tooltip)
        tvMaxLv = findViewById(R.id.tv_max_lv)
        tvMaxAsc = findViewById(R.id.tv_max_asc)
        tvResetToBase = findViewById(R.id.tv_reset_to_base)
        spnAffinity = findViewById(R.id.spn_affinity)
        spnType = findViewById(R.id.spn_card_type)
        arryAffinity = resources.getStringArray(R.array.arry_affinity)
        arryType = resources.getStringArray(R.array.arry_card_type)
        btnRegenerate = findViewById(R.id.btn_regenerate)

        // set visibility for update stats based on configuration
        if (BuildConfig.UPDATE_BASE_STATS) {
            llUpdateWrapper!!.visibility = View.VISIBLE
            val llParamsA = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f)
            val llParamsB = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f)
            tvResetToBase!!.layoutParams = llParamsA
            llUpdateWrapper!!.layoutParams = llParamsB
        } else {
            llUpdateWrapper!!.visibility = View.GONE
            tvResetToBase!!.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        // set animation attributes
        animation!!.duration = 300 // Duration: 300
        animation!!.interpolator = LinearInterpolator()
        // Repeat animation infinitely
        animation!!.repeatCount = Animation.INFINITE
        // Reverse animation at the end so the button will fade back in
        animation!!.repeatMode = Animation.REVERSE

        // set editText action disabled by default
        edtLv!!.isFocusable = false
        edtLv!!.isFocusableInTouchMode = false
        edtAsc!!.isFocusable = false
        edtAsc!!.isFocusableInTouchMode = false

        // set edtText filters
        edtLv!!.filters = arrayOf<InputFilter>(InputFilterMinMax(0, MAX_LEVEL_FILTER_VALUE))

        // set on item click listener
        spnAffinity!!.onItemSelectedListener = this
        spnType!!.onItemSelectedListener = this

        // set adapter for arrays
        val adapterAffinity = ArrayAdapter(this, android.R.layout.simple_spinner_item, arryAffinity!!)
        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, arryType!!)
        spnAffinity!!.adapter = adapterAffinity
        spnType!!.adapter = adapterType

        // set default tooltip
        showRandomTooltip()
    }

    /**
     * Method is used to initialize click listeners
     */
    private fun initializeHandlers() {
        llUpdateWrapper!!.setOnClickListener(this)
        btnRegenerate!!.setOnClickListener(this)
        tvResetToBase!!.setOnClickListener(this)
    }

    /**
     * Method is used to initialize listeners
     */
    private fun initializeListeners() {
        // set shake event listener
        mSensorListener!!.setOnShakeListener {
            if (isBaseStatsSet) {
                Logger.d(TAG, "reset stats from shake")
                reset()
            }
        }

        // onEditorAction listener
        edtAsc!!.setOnEditorActionListener { v, actionId, event ->
            if (Utils.isStringEmpty(edtAsc!!.text.toString())) {
                edtAsc!!.setText("0")
                // max level
                tvMaxLv!!.text = FORWARD_SLASH + mStats!!.mavLv.toString()
                // update edtText filter
                edtLv!!.filters = arrayOf<InputFilter>(InputFilterMinMax(0, MAX_LEVEL_FILTER_VALUE))

                val currLv = Integer.parseInt(edtLv!!.text.toString())
                if (currLv > mStats!!.mavLv) {
                    // adjust max lv
                    Logger.d(TAG, "adjusting maxLv: " + mStats!!.mavLv)
                    edtLv!!.setText(mStats!!.mavLv.toString())
                }
            }
            false
        }

        // addTextChanged listener
        edtAsc!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable) {
                // update max level based on inputted asc
                if (!Utils.isStringEmpty(edtAsc!!.text.toString()) && isBaseStatsSet &&
                        Integer.parseInt(edtAsc!!.text.toString()) > 0) {
                    // max level
                    tvMaxLv!!.text = FORWARD_SLASH + mStats!!.mavLv.toString()
                    // update edtText filter
                    edtLv!!.filters = arrayOf<InputFilter>(InputFilterMinMax(0, mStats!!.mavLv))

                    val currLv = Integer.parseInt(edtLv!!.text.toString())
                    if (currLv > mStats!!.mavLv) {
                        // adjust max lv
                        Logger.d(TAG, "adjusting maxLv: " + mStats!!.mavLv)
                        edtLv!!.setText(mStats!!.mavLv.toString())
                    }
                }
            }
        })

        // onEditorAction listener
        edtLv!!.setOnEditorActionListener { v, actionId, event ->
            if (Utils.isStringEmpty(edtLv!!.text.toString())) {
                edtLv!!.setText("1")
                resetToBaseStats()
            }
            false
        }

        // addTextChanged listener
        edtLv!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable) {
                // update stats based on changed level
                if (!Utils.isStringEmpty(edtLv!!.text.toString()) && isBaseStatsSet) {
                    if (Integer.parseInt(edtLv!!.text.toString()) > 1) {
                        setStats()
                    } else if (Integer.parseInt(edtLv!!.text.toString()) == 0) {
                        reset()
                    }
                }
            }
        })
    }

    /**
     * Method is used to show a randomized tooltip
     */
    private fun showRandomTooltip() {
        if (Utils.isStringEmpty(mSelectedAffinity) || Utils.isStringEmpty(mSelectedCardType) ||
                mSelectedAffinity!!.equals(SELECT_OPTION, ignoreCase = true) ||
                mSelectedCardType!!.equals(SELECT_OPTION, ignoreCase = true)) {
            if (isDefaultSet) {
                return
            }

            // set default tooltip message
            isDefaultSet = true
            tvTooltip!!.text = resources.getString(R.string.tooltip_default)
            startTooltipTimer()
        } else {
            if (!isAnimationStarted) {
                // set random tooltip message
                val rand = Random()
                val randValue = rand.nextInt(9)
                if (randValue == 0) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_change_lv_and_asc)
                } else if (randValue == 1) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_reset_stats)
                } else if (randValue == 2) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_stats_increase)
                } else if (randValue == 3) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_asc_increase)
                } else if (randValue == 4) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_physical_defense)
                } else if (randValue == 5) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_magical_defense)
                } else if (randValue == 6) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_strength_attack)
                } else if (randValue == 7) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_wisdom_attack)
                } else if (randValue == 8) {
                    tvTooltip!!.text = resources.getString(R.string.tooltip_critical_attack)
                }
            }
        }
    }

    /**
     * Method is used to start a timer that will call
     *
     * @link showRandomTooltip() to refresh tooltip message
     */
    private fun startTooltipTimer() {
        if (mTimer == null) {
            mTimer = Timer()
        }

        mTimer!!.schedule(object : TimerTask() {

            override fun run() {
                if (!isPaused) {
                    runOnUiThread {
                        // show random tooltip
                        showRandomTooltip()
                    }
                }
            }
        }, TIMER.toLong(), TIMER.toLong())
    }

    /**
     * Method is used to destroy tooltip timer
     */
    private fun stopTooltipTimer() {
        if (mTimer != null) {
            Logger.i(TAG, "destroying timer object for tooltip")
            mTimer!!.cancel()
            mTimer!!.purge()
            mTimer = null
        }
    }

    /**
     * Method is used to reset stats and data
     */
    private fun reset() {
        v!!.vibrate(VIBRATE_TIMER.toLong())
        mStats!!.reset() // reset stats model class
        isBaseStatsSet = false // reset flag tracker for stat reset
        // set editText action disabled
        isEditable = false
        edtLv!!.isFocusable = false
        edtLv!!.isFocusableInTouchMode = false
        edtAsc!!.isFocusable = false
        edtAsc!!.isFocusableInTouchMode = false
        // reset tooltip messaging
        tvTooltip!!.text = resources.getString(R.string.tooltip_default)
        spnAffinity!!.setSelection(0)
        spnType!!.setSelection(0)

        // reset editText values
        edtLv!!.setText(resources.getString(R.string.num_num))
        edtAsc!!.setText(resources.getString(R.string.num))
        tvMaxLv!!.text = resources.getString(R.string.num)
        tvMaxAsc!!.text = resources.getString(R.string.num)
        // reset editText color
        edtLv!!.setTextColor(Utils.getColor(mContext, R.color.material_grey_400_color_code))
        edtAsc!!.setTextColor(Utils.getColor(mContext, R.color.material_grey_400_color_code))

        // reset textView values
        tvHp!!.text = "0"
        tvStr!!.text = "0"
        tvSpd!!.text = "0"
        tvWis!!.text = "0"
        tvPhyDef!!.text = "0"
        tvMagDef!!.text = "0"
        tvCrit!!.text = "0"

        // stop animation
        if (isAnimationStarted) {
            btnRegenerate!!.clearAnimation()
            isAnimationStarted = false
        }
    }

    /**
     * Method is used to restore stats to their original generated values
     * before edits and adjustments were made
     */
    private fun resetToBaseStats() {
        if (!Utils.isStringEmpty(mSelectedAffinity) && !Utils.isStringEmpty(mSelectedCardType) &&
                !mSelectedAffinity!!.equals(SELECT_OPTION, ignoreCase = true) &&
                !mSelectedCardType!!.equals(SELECT_OPTION, ignoreCase = true)) {
            edtLv!!.setText("1")
            edtAsc!!.setText("0")
            tvMaxLv!!.text = FORWARD_SLASH + mStats!!.mavLv.toString()
            tvMaxAsc!!.text = FORWARD_SLASH + mStats!!.maxAsc.toString()
            tvHp!!.text = mStats!!.hp.toString()
            tvStr!!.text = mStats!!.str.toString()
            tvSpd!!.text = mStats!!.spd.toString()
            tvWis!!.text = mStats!!.wis.toString()
            tvPhyDef!!.text = mStats!!.phyDef.toString()
            tvMagDef!!.text = mStats!!.magDef.toString()
            tvCrit!!.text = mStats!!.crit.toString()

            // update Affinity selection
            for (i in arryAffinity!!.indices) {
                if (mStats!!.affinity.toString() == arryAffinity!![i]) {
                    spnAffinity!!.setSelection(i)
                    break
                }
            }

            // update Type selection
            for (i in arryType!!.indices) {
                if (mStats!!.cardType.toString() == arryType!![i]) {
                    spnType!!.setSelection(i)
                    break
                }
            }

            // stop animation
            if (isAnimationStarted) {
                btnRegenerate!!.clearAnimation()
                isAnimationStarted = false
            }
        }
    }

    override fun onClick(v: View) {

        /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 500 ms) --LT
         */
        val mCurrClickTimestamp = SystemClock.uptimeMillis()
        val mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime
        mLastClickTime = mCurrClickTimestamp
        if (mElapsedTimestamp <= CLICK_THRESHOLD) {
            Logger.i(TAG, "button click intercepted/blocked")
            return
        }

        when (v.id) {
            R.id.btn_regenerate -> {
                // regenerate will generate new base stats
                isBaseStatsSet = false
                isEditable = false

                // stop animation
                if (isAnimationStarted) {
                    btnRegenerate!!.clearAnimation()
                    isAnimationStarted = false
                    // show randon tooltip
                    showRandomTooltip()
                }
                // set stats
                setStats()
            }
            R.id.tv_reset_to_base ->
                // reset to base stats
                resetToBaseStats()
            R.id.ll_update_wrapper ->
                // add fragment
                addFragment(ApiFragment())
            else -> {
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
            R.id.spn_affinity -> {
                // start animation to prompt user that stats will not regenerate automatically
                if (!Utils.isStringEmpty(mSelectedAffinity) && mSelectedAffinity != arryAffinity!![position]) {
                    if (!isAnimationStarted && isBaseStatsSet) {
                        isAnimationStarted = true
                        btnRegenerate!!.startAnimation(animation)
                        // update tooltip
                        tvTooltip!!.text = resources.getString(R.string.tooltip_stats_regenerate)
                    }
                }

                // set selected affinity
                mSelectedAffinity = arryAffinity!![position]

                // return if both affinity and type is not selected
                if (position == 0 || spnType!!.selectedItemPosition == 0) {
                    return
                }

                // set stats
                setStats()
            }
            R.id.spn_card_type -> {
                // start animation to prompt user that stats will not regenerate automatically
                if (!Utils.isStringEmpty(mSelectedCardType)) {
                    if (!isAnimationStarted && isBaseStatsSet) {
                        isAnimationStarted = true
                        btnRegenerate!!.startAnimation(animation)
                        // update tooltip
                        tvTooltip!!.text = resources.getString(R.string.tooltip_stats_regenerate)
                    }
                }

                // set selected type
                mSelectedCardType = arryType!![position]

                // disable ascension editText field if Squad Leader is selected
                if (mSelectedCardType!!.equals(arryType!![arryType!!.size - 1], ignoreCase = true)) {
                    toggleEditTextAsc(false)
                } else {
                    toggleEditTextAsc(true)
                }

                // return if both affinity and type is not selected
                if (position == 0 || spnAffinity!!.selectedItemPosition == 0) {
                    return
                }

                // set stats
                setStats()
            }
            else -> {
            }
        }
    }

    /**
     * Method is used to toggle edtAsc to be enabled or disabled
     *
     * @param isEnabled True if ascension editText is editable, otherwise false
     */
    private fun toggleEditTextAsc(isEnabled: Boolean) {
        if (!Utils.checkIfNull<GuardianStatsModel>(mStats) && !Utils.checkIfNull<CardType>(mStats!!.cardType) &&
                !mSelectedCardType!!.equals(arryType!![0], ignoreCase = true)) {
            tvMaxAsc!!.text = FORWARD_SLASH + mStats!!.maxAsc.toString()
        }

        if (isEnabled) {
            edtAsc!!.isEnabled = true
            edtAsc!!.setTextColor(Utils.getColor(mContext, R.color.material_green_500_color_code))
            edtAsc!!.setBackgroundColor(Utils.getColor(mContext, R.color.white))
        } else {
            edtAsc!!.isEnabled = false
            edtAsc!!.setTextColor(Utils.getColor(mContext, R.color.black))
            edtAsc!!.setBackgroundColor(Utils.getColor(mContext, R.color.material_grey_300_color_code))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // do nothing
    }

    /**
     * Method is used to set stats
     */
    private fun setStats() {
        if (!Utils.isStringEmpty(mSelectedAffinity) && !Utils.isStringEmpty(mSelectedCardType) &&
                !mSelectedAffinity!!.equals(SELECT_OPTION, ignoreCase = true) &&
                !mSelectedCardType!!.equals(SELECT_OPTION, ignoreCase = true)) {

            if (!isEditable) {
                // set editText action enabled
                isEditable = true
                edtLv!!.isFocusable = true
                edtLv!!.isFocusableInTouchMode = true
                edtLv!!.setTextColor(Utils.getColor(mContext, R.color.material_green_500_color_code))
                edtAsc!!.isFocusable = true
                edtAsc!!.isFocusableInTouchMode = true

                if (Utils.isStringEmpty(edtLv!!.text.toString())) {
                    // set default values
                    edtLv!!.setText("1")
                }

                if (Utils.isStringEmpty(edtAsc!!.text.toString())) {
                    // set default values
                    edtAsc!!.setText("0")
                }
            }

            if (!isBaseStatsSet) {
                Logger.d(TAG, "setting base stats")


                // generate stats and set textView
                // cardType and affinity are String values from selected spinner options
                mStats = mStatUtils!!.getStats(
                        MappingModel.getCardTypeByCardType(mSelectedCardType!!),
                        MappingModel.getAffinityByAffinity(mSelectedAffinity!!),
                        Integer.parseInt(edtAsc!!.text.toString()))

                tvMaxLv!!.text = FORWARD_SLASH + mStats!!.mavLv.toString()
                tvMaxAsc!!.text = FORWARD_SLASH + mStats!!.maxAsc.toString()
                tvHp!!.text = mStats!!.hp.toString()
                tvStr!!.text = mStats!!.str.toString()
                tvSpd!!.text = mStats!!.spd.toString()
                tvWis!!.text = mStats!!.wis.toString()
                tvPhyDef!!.text = mStats!!.phyDef.toString()
                tvMagDef!!.text = mStats!!.magDef.toString()
                tvCrit!!.text = mStats!!.crit.toString()

                // base stats are now set
                isBaseStatsSet = true

                // print stats to console
                printStats()
            } else {
                if (!isAnimationStarted) {
                    Logger.d(TAG, "updating base stats")
                    // base stats are set, only update stats
                    if (!Utils.isStringEmpty(edtLv!!.text.toString()) && Integer.parseInt(edtLv!!.text.toString()) > 1) {
                        // level directly influences the increase in stats
                        val level = Integer.parseInt(edtLv!!.text.toString())
                        val multiplierStats = (level - 1) * MULTIPLIER_LEVEL_STATS

                        // update asc
                        tvMaxAsc!!.text = FORWARD_SLASH + mStats!!.maxAsc.toString()

                        // update hp
                        val hp = mStats!!.hp
                        tvHp!!.text = (hp + hp * multiplierStats).toInt().toString()

                        // update str
                        val str = mStats!!.str
                        tvStr!!.text = (str + str * multiplierStats).toInt().toString()

                        // update spd
                        val spd = mStats!!.spd
                        tvSpd!!.text = (spd + spd * multiplierStats).toInt().toString()

                        // update wis
                        val wis = mStats!!.wis
                        tvWis!!.text = (wis + wis * multiplierStats).toInt().toString()

                        // update phyDef
                        val phyDef = mStats!!.phyDef
                        tvPhyDef!!.text = (phyDef + phyDef * multiplierStats).toInt().toString()

                        // update magDef
                        val magDef = mStats!!.magDef
                        tvMagDef!!.text = (magDef + magDef * multiplierStats).toInt().toString()

                        // update crit
                        val crit = mStats!!.crit
                        tvCrit!!.text = (crit + crit * multiplierStats).toInt().toString()

                        // print stats to console
                        printStats()
                    }
                }
            }
        }
    }

    /**
     * Method is used to print generated stats to console
     */
    private fun printStats() {
        Logger.i(TAG, "affinity: " + mSelectedAffinity!!)
        Logger.i(TAG, "card type: " + mSelectedCardType!!)
        Logger.i(TAG, "level: " + edtLv!!.text)
        Logger.i(TAG, "asc: " + edtAsc!!.text)
        Logger.i(TAG, "hp: " + tvHp!!.text)
        Logger.i(TAG, "str: " + tvStr!!.text)
        Logger.i(TAG, "spd: " + tvSpd!!.text)
        Logger.i(TAG, "wisdom: " + tvWis!!.text)
        Logger.i(TAG, "phy def: " + tvPhyDef!!.text)
        Logger.i(TAG, "mag def: " + tvMagDef!!.text)
        Logger.i(TAG, "crit%: " + tvCrit!!.text)
        Logger.i(TAG, "---------------------------------------------")
    }

    override fun onPause() {
        // unregisters a listener for all sensors
        mSensorManager!!.unregisterListener(mSensorListener)
        // set flag
        isPaused = true

        // remove network observer
        if (!Utils.checkIfNull<NetworkReceiver>(mNetworkReceiver) &&
                mNetworkReceiver!!.observerSize > 0 && mNetworkReceiver!!.contains(this)) {
            try {
                // unregister network receiver
                unregisterReceiver(mNetworkReceiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            mNetworkReceiver!!.removeObserver(this)
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        // set flag
        isPaused = false
        // events will be delivered to the provided SensorEventListener as soon as they are available
        mSensorManager!!.registerListener(mSensorListener,
                mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI)

        // only register receiver if it has not already been registered
        if (!Utils.checkIfNull<NetworkReceiver>(mNetworkReceiver) && !mNetworkReceiver!!.contains(this)) {
            // register network receiver
            mNetworkReceiver!!.addObserver(this)
            registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            // print observer list
            mNetworkReceiver!!.printObserverList()
        }
    }

    override fun onDestroy() {
        // stop tooltip timer
        stopTooltipTimer()

        // remove network observer
        if (!Utils.checkIfNull<NetworkReceiver>(mNetworkReceiver) &&
                mNetworkReceiver!!.observerSize > 0 && mNetworkReceiver!!.contains(this)) {
            try {
                // unregister network receiver
                unregisterReceiver(mNetworkReceiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            mNetworkReceiver!!.removeObserver(this)
        }
        super.onDestroy()
    }

    override fun notifyConnectionChange(isConnected: Boolean) {
        if (isConnected) {
            // app is connected to network
            DialogUtils.dismissNoNetworkDialog()
        } else {
            // app is not connected to network
            DialogUtils.showDefaultNoNetworkAlert(this, null,
                    this.resources.getString(R.string.check_network))
        }
    }

    companion object {
        val TAG = MainActivity::class.java!!.getSimpleName()

        private val VIBRATE_TIMER = 500 // milliseconds
        private val TIMER = 60000 // milliseconds
        private val CLICK_THRESHOLD = 500 // milliseconds
        private val MAX_LEVEL_FILTER_VALUE = 20
        private val MULTIPLIER_LEVEL_STATS = 0.05 // percent

        private val SELECT_OPTION = "SELECT OPTION"
        private val FORWARD_SLASH = "/ "
    }
}
