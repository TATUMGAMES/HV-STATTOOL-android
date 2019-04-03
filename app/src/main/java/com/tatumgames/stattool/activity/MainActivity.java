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

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.tatumgames.stattool.BuildConfig;
import com.tatumgames.stattool.R;
import com.tatumgames.stattool.enums.Enum;
import com.tatumgames.stattool.helper.InputFilterMinMax;
import com.tatumgames.stattool.listener.ShakeEventListener;
import com.tatumgames.stattool.logger.Logger;
import com.tatumgames.stattool.model.GuardianStats;
import com.tatumgames.stattool.requests.app.GetGuardiansRQ;
import com.tatumgames.stattool.requests.app.GetGuardiansRS;
import com.tatumgames.stattool.requests.app.UpdateGuardianBaseStatsBulkRQ;
import com.tatumgames.stattool.requests.app.UpdateGuardianBaseStatsBulkRS;
import com.tatumgames.stattool.requests.model.Data;
import com.tatumgames.stattool.requests.model.UpdateBaseStats;
import com.tatumgames.stattool.utils.DialogUtils;
import com.tatumgames.stattool.utils.ErrorUtils;
import com.tatumgames.stattool.utils.StatUtils;
import com.tatumgames.stattool.utils.Utils;
import com.tatumgames.stattool.utils.network.NetworkReceiver;
import com.tatumgames.stattool.volley.RequestManager;
import com.tatumgames.stattool.volley.constants.UrlConstants;
import com.tatumgames.stattool.volley.listeners.ErrorListener;
import com.tatumgames.stattool.volley.listeners.GsonObjectListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        NetworkReceiver.NetworkStatusObserver {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int VIBRATE_TIMER = 500; // milliseconds
    private static final int TIMER = 60000; // milliseconds
    private static final int CLICK_THRESHOLD = 500; // milliseconds
    private static final int MAX_LEVEL_FILTER_VALUE = 20;
    private static final double MULTIPLIER_LEVEL_STATS = 0.05; // percent

    private static final String SELECT_OPTION = "SELECT OPTION";
    private static final String FORWARD_SLASH = "/ ";

    private RequestManager mRequestManager;
    private NetworkReceiver mNetworkReceiver;
    private ErrorUtils mErrorUtils;
    private Context mContext;
    private GuardianStats stats;
    private StatUtils statUtils;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Vibrator v;
    private Spinner spnAffinity, spnType;
    private EditText edtLv, edtAsc;
    private TextView tvHp, tvStr, tvSpd, tvWis, tvPhyDef, tvMagDef, tvCrit, tvTooltip, tvMaxLv, tvMaxAsc,
            tvResetToBase;
    private Button btnRegenerate;
    private LinearLayout llUpdateWrapper;

    private boolean isDefaultSet, isPaused, isEditable, isBaseStatsSet, isAnimationStarted;
    private String mSelectedAffinity, mSelectedCardType;
    private String[] arryAffinity, arryType;
    private Timer mTimer;
    private Animation animation;
    private ArrayList<UpdateGuardianBaseStatsBulkRQ> alUpdateGuardianBaseStatsBulkRQ;

    private int updateGuardianCounter;
    private long mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        // instantiate Fabric API
        Fabric.with(this, new Crashlytics());
        // set the activity content from a layout resource
        setContentView(R.layout.activity_main);

        // intialize views and listeners
        initializeViews();
        initializeHandlers();
        initializeListeners();
    }

    /**
     * Method is used to instantiate objects and views
     */
    private void initializeViews() {
        mContext = MainActivity.this;
        stats = new GuardianStats();
        statUtils = new StatUtils();
        mTimer = new Timer();
        mRequestManager = new RequestManager(this);
        mNetworkReceiver = new NetworkReceiver();
        mErrorUtils = new ErrorUtils();
        alUpdateGuardianBaseStatsBulkRQ = new ArrayList<>();

        // initialize vibrator and sensor for device shake detector
        v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // constructor to use when building an AlphaAnimation from code
        animation = new AlphaAnimation(1.0F, 0.4F);

        // initialize views
        llUpdateWrapper = findViewById(R.id.ll_update_wrapper);
        edtLv = findViewById(R.id.edt_lv);
        edtAsc = findViewById(R.id.edt_asc);
        tvHp = findViewById(R.id.tv_hp);
        tvStr = findViewById(R.id.tv_str);
        tvSpd = findViewById(R.id.tv_spd);
        tvWis = findViewById(R.id.tv_wis);
        tvPhyDef = findViewById(R.id.tv_phy_def);
        tvMagDef = findViewById(R.id.tv_mag_def);
        tvCrit = findViewById(R.id.tv_crit);
        tvTooltip = findViewById(R.id.tv_tooltip);
        tvMaxLv = findViewById(R.id.tv_max_lv);
        tvMaxAsc = findViewById(R.id.tv_max_asc);
        tvResetToBase = findViewById(R.id.tv_reset_to_base);
        spnAffinity = findViewById(R.id.spn_affinity);
        spnType = findViewById(R.id.spn_card_type);
        arryAffinity = getResources().getStringArray(R.array.arryAffinity);
        arryType = getResources().getStringArray(R.array.arryCardType);
        btnRegenerate = findViewById(R.id.btn_regenerate);

        // set visibility for update stats based on configuration
        if (BuildConfig.UPDATE_BASE_STATS) {
            llUpdateWrapper.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams llParamsA = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            LinearLayout.LayoutParams llParamsB = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
            tvResetToBase.setLayoutParams(llParamsA);
            llUpdateWrapper.setLayoutParams(llParamsB);
        } else {
            llUpdateWrapper.setVisibility(View.GONE);
            tvResetToBase.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        // set animation attributes
        animation.setDuration(300); // Duration: 300
        animation.setInterpolator(new LinearInterpolator());
        // Repeat animation infinitely
        animation.setRepeatCount(Animation.INFINITE);
        // Reverse animation at the end so the button will fade back in
        animation.setRepeatMode(Animation.REVERSE);

        // set editText action disabled by default
        edtLv.setFocusable(false);
        edtLv.setFocusableInTouchMode(false);
        edtAsc.setFocusable(false);
        edtAsc.setFocusableInTouchMode(false);

        // set edtText filters
        edtLv.setFilters(new InputFilter[]{new InputFilterMinMax(0, MAX_LEVEL_FILTER_VALUE)});

        // set on item click listener
        spnAffinity.setOnItemSelectedListener(this);
        spnType.setOnItemSelectedListener(this);

        // set adapter for arrays
        ArrayAdapter<String> adapterAffinity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arryAffinity);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arryType);
        spnAffinity.setAdapter(adapterAffinity);
        spnType.setAdapter(adapterType);

        // set default tooltip
        showRandomTooltip();
    }

    /**
     * Method is used to initialize click listeners
     */
    private void initializeHandlers() {
        llUpdateWrapper.setOnClickListener(this);
        btnRegenerate.setOnClickListener(this);
        tvResetToBase.setOnClickListener(this);
    }

    /**
     * Method is used to initialize listeners
     */
    private void initializeListeners() {
        // set shake event listener
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                if (isBaseStatsSet) {
                    Logger.d(TAG, "reset stats from shake");
                    reset();
                }
            }
        });

        // onEditorAction listener
        edtAsc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (Utils.isStringEmpty(String.valueOf(edtAsc.getText()))) {
                    edtAsc.setText("0");
                    // max level
                    tvMaxLv.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMavLv())));
                    // update edtText filter
                    edtLv.setFilters(new InputFilter[]{new InputFilterMinMax(0, MAX_LEVEL_FILTER_VALUE)});

                    int currLv = Integer.parseInt(String.valueOf(edtLv.getText()));
                    if (currLv > stats.getMavLv()) {
                        // adjust max lv
                        Logger.d(TAG, "adjusting maxLv: " + stats.getMavLv());
                        edtLv.setText(String.valueOf(stats.getMavLv()));
                    }
                }
                return false;
            }
        });

        // addTextChanged listener
        edtAsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // update max level based on inputted asc
                if (!Utils.isStringEmpty(String.valueOf(edtAsc.getText())) && isBaseStatsSet &&
                        Integer.parseInt(String.valueOf(edtAsc.getText())) > 0) {
                    // max level
                    tvMaxLv.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMavLv())));
                    // update edtText filter
                    edtLv.setFilters(new InputFilter[]{new InputFilterMinMax(0, stats.getMavLv())});

                    int currLv = Integer.parseInt(String.valueOf(edtLv.getText()));
                    if (currLv > stats.getMavLv()) {
                        // adjust max lv
                        Logger.d(TAG, "adjusting maxLv: " + stats.getMavLv());
                        edtLv.setText(String.valueOf(stats.getMavLv()));
                    }
                }
            }
        });

        // onEditorAction listener
        edtLv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (Utils.isStringEmpty(String.valueOf(edtLv.getText()))) {
                    edtLv.setText("1");
                    resetToBaseStats();
                }
                return false;
            }
        });

        // addTextChanged listener
        edtLv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // update stats based on changed level
                if (!Utils.isStringEmpty(String.valueOf(edtLv.getText())) && isBaseStatsSet) {
                    if (Integer.parseInt(String.valueOf(edtLv.getText())) > 1) {
                        setStats();
                    } else if (Integer.parseInt(String.valueOf(edtLv.getText())) == 0) {
                        reset();
                    }
                }
            }
        });
    }

    /**
     * Method is used to show a randomized tooltip
     */
    private void showRandomTooltip() {
        if (Utils.isStringEmpty(mSelectedAffinity) || Utils.isStringEmpty(mSelectedCardType) ||
                mSelectedAffinity.equalsIgnoreCase(SELECT_OPTION) ||
                mSelectedCardType.equalsIgnoreCase(SELECT_OPTION)) {
            if (isDefaultSet) {
                return;
            }

            // set default tooltip message
            isDefaultSet = true;
            tvTooltip.setText(getResources().getString(R.string.tooltip_default));
            startTooltipTimer();
        } else {
            if (!isAnimationStarted) {
                // set random tooltip message
                Random rand = new Random();
                int randValue = rand.nextInt(9);
                if (randValue == 0) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_change_lv_and_asc));
                } else if (randValue == 1) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_reset_stats));
                } else if (randValue == 2) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_stats_increase));
                } else if (randValue == 3) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_asc_increase));
                } else if (randValue == 4) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_physical_defense));
                } else if (randValue == 5) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_magical_defense));
                } else if (randValue == 6) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_strength_attack));
                } else if (randValue == 7) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_wisdom_attack));
                } else if (randValue == 8) {
                    tvTooltip.setText(getResources().getString(R.string.tooltip_critical_attack));
                }
            }
        }
    }

    /**
     * Method is used to start a timer that will call
     *
     * @link showRandomTooltip() to refresh tooltip message
     */
    private void startTooltipTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!isPaused) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // show random tooltip
                            showRandomTooltip();
                        }
                    });
                }
            }
        }, TIMER, TIMER);
    }

    /**
     * Method is used to destroy tooltip timer
     */
    private void stopTooltipTimer() {
        if (mTimer != null) {
            Logger.i(TAG, "destroying timer object for tooltip");
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    /**
     * Method is used to reset stats and data
     */
    private void reset() {
        v.vibrate(VIBRATE_TIMER);
        stats.reset(); // reset stats model class
        isBaseStatsSet = false; // reset flag tracker for stat reset
        // set editText action disabled
        isEditable = false;
        edtLv.setFocusable(false);
        edtLv.setFocusableInTouchMode(false);
        edtAsc.setFocusable(false);
        edtAsc.setFocusableInTouchMode(false);
        // reset tooltip messaging
        tvTooltip.setText(getResources().getString(R.string.tooltip_default));
        spnAffinity.setSelection(0);
        spnType.setSelection(0);

        // reset editText values
        edtLv.setText(getResources().getString(R.string.num_num));
        edtAsc.setText(getResources().getString(R.string.num));
        tvMaxLv.setText(getResources().getString(R.string.num));
        tvMaxAsc.setText(getResources().getString(R.string.num));
        // reset editText color
        edtLv.setTextColor(Utils.getColor(mContext, R.color.material_grey_400_color_code));
        edtAsc.setTextColor(Utils.getColor(mContext, R.color.material_grey_400_color_code));

        // reset textView values
        tvHp.setText("0");
        tvStr.setText("0");
        tvSpd.setText("0");
        tvWis.setText("0");
        tvPhyDef.setText("0");
        tvMagDef.setText("0");
        tvCrit.setText("0");

        // stop animation
        if (isAnimationStarted) {
            btnRegenerate.clearAnimation();
            isAnimationStarted = false;
        }
    }

    /**
     * Method is used to restore stats to their original generated values
     * before edits and adjustments were made
     */
    private void resetToBaseStats() {
        if (!Utils.isStringEmpty(mSelectedAffinity) && !Utils.isStringEmpty(mSelectedCardType) &&
                !mSelectedAffinity.equalsIgnoreCase(SELECT_OPTION) &&
                !mSelectedCardType.equalsIgnoreCase(SELECT_OPTION)) {
            edtLv.setText("1");
            edtAsc.setText("0");
            tvMaxLv.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMavLv())));
            tvMaxAsc.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMaxAsc())));
            tvHp.setText(String.valueOf(stats.getHp()));
            tvStr.setText(String.valueOf(stats.getStr()));
            tvSpd.setText(String.valueOf(stats.getSpd()));
            tvWis.setText(String.valueOf(stats.getWis()));
            tvPhyDef.setText(String.valueOf(stats.getPhyDef()));
            tvMagDef.setText(String.valueOf(stats.getMagDef()));
            tvCrit.setText(String.valueOf(stats.getCrit()));

            // update Affinity selection
            for (int i = 0; i < arryAffinity.length; i++) {
                if (String.valueOf(stats.getAffinity()).equals(arryAffinity[i])) {
                    spnAffinity.setSelection(i);
                    break;
                }
            }

            // update Type selection
            for (int i = 0; i < arryType.length; i++) {
                if (String.valueOf(stats.getCardType()).equals(arryType[i])) {
                    spnType.setSelection(i);
                    break;
                }
            }

            // stop animation
            if (isAnimationStarted) {
                btnRegenerate.clearAnimation();
                isAnimationStarted = false;
            }
        }
    }

    @Override
    public void onClick(View v) {

        /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 500 ms) --LT
         */
        long mCurrClickTimestamp = SystemClock.uptimeMillis();
        long mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime;
        mLastClickTime = mCurrClickTimestamp;
        if (mElapsedTimestamp <= CLICK_THRESHOLD) {
            Logger.i(TAG, "button click intercepted/blocked");
            return;
        }

        switch (v.getId()) {
            case R.id.btn_regenerate:
                // regenerate will generate new base stats
                isBaseStatsSet = false;
                isEditable = false;

                // stop animation
                if (isAnimationStarted) {
                    btnRegenerate.clearAnimation();
                    isAnimationStarted = false;
                    // show randon tooltip
                    showRandomTooltip();
                }
                // set stats
                setStats();
                break;
            case R.id.tv_reset_to_base:
                // reset to base stats
                resetToBaseStats();
                break;
            case R.id.ll_update_wrapper:
                // getGuardians request
                getGuardians();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn_affinity:
                // start animation to prompt user that stats will not regenerate automatically
                if (!Utils.isStringEmpty(mSelectedAffinity) && !mSelectedAffinity.equals(arryAffinity[position])) {
                    if (!isAnimationStarted && isBaseStatsSet) {
                        isAnimationStarted = true;
                        btnRegenerate.startAnimation(animation);
                        // update tooltip
                        tvTooltip.setText(getResources().getString(R.string.tooltip_stats_regenerate));
                    }
                }

                // set selected affinity
                mSelectedAffinity = arryAffinity[position];

                // return if both affinity and type is not selected
                if (position == 0 || spnType.getSelectedItemPosition() == 0) {
                    return;
                }

                // set stats
                setStats();
                break;
            case R.id.spn_card_type:
                // start animation to prompt user that stats will not regenerate automatically
                if (!Utils.isStringEmpty(mSelectedCardType)) {
                    if (!isAnimationStarted && isBaseStatsSet) {
                        isAnimationStarted = true;
                        btnRegenerate.startAnimation(animation);
                        // update tooltip
                        tvTooltip.setText(getResources().getString(R.string.tooltip_stats_regenerate));
                    }
                }

                // set selected type
                mSelectedCardType = arryType[position];

                // disable ascension editText field if Squad Leader is selected
                if (mSelectedCardType.equalsIgnoreCase(arryType[arryType.length - 1])) {
                    toggleEditTextAsc(false);
                } else {
                    toggleEditTextAsc(true);
                }

                // return if both affinity and type is not selected
                if (position == 0 || spnAffinity.getSelectedItemPosition() == 0) {
                    return;
                }

                // set stats
                setStats();
                break;
            default:
                break;
        }
    }

    /**
     * Method is used to toggle edtAsc to be enabled or disabled
     *
     * @param isEnabled True if ascension editText is editable, otherwise false
     */
    private void toggleEditTextAsc(boolean isEnabled) {
        if (!Utils.checkIfNull(stats) && !Utils.checkIfNull(stats.getCardType()) &&
                !mSelectedCardType.equalsIgnoreCase(arryType[0])) {
            tvMaxAsc.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMaxAsc())));
        }

        if (isEnabled) {
            edtAsc.setEnabled(true);
            edtAsc.setTextColor(Utils.getColor(mContext, R.color.material_green_500_color_code));
            edtAsc.setBackgroundColor(Utils.getColor(mContext, R.color.white));
        } else {
            edtAsc.setEnabled(false);
            edtAsc.setTextColor(Utils.getColor(mContext, R.color.black));
            edtAsc.setBackgroundColor(Utils.getColor(mContext, R.color.material_grey_300_color_code));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    /**
     * Method is used to set stats
     */
    private void setStats() {
        if (!Utils.isStringEmpty(mSelectedAffinity) && !Utils.isStringEmpty(mSelectedCardType) &&
                !mSelectedAffinity.equalsIgnoreCase(SELECT_OPTION) &&
                !mSelectedCardType.equalsIgnoreCase(SELECT_OPTION)) {

            if (!isEditable) {
                // set editText action enabled
                isEditable = true;
                edtLv.setFocusable(true);
                edtLv.setFocusableInTouchMode(true);
                edtLv.setTextColor(Utils.getColor(mContext, R.color.material_green_500_color_code));
                edtAsc.setFocusable(true);
                edtAsc.setFocusableInTouchMode(true);

                if (Utils.isStringEmpty(edtLv.getText().toString())) {
                    // set default values
                    edtLv.setText("1");
                }

                if (Utils.isStringEmpty(edtAsc.getText().toString())) {
                    // set default values
                    edtAsc.setText("0");
                }
            }

            if (!isBaseStatsSet) {
                Logger.d(TAG, "setting base stats");
                // generate stats and set textView
                stats = statUtils.getStats(
                        statUtils.getCardType(mSelectedCardType),
                        statUtils.getAffinity(mSelectedAffinity),
                        Integer.parseInt(String.valueOf(edtAsc.getText())));

                tvMaxLv.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMavLv())));
                tvMaxAsc.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMaxAsc())));
                tvHp.setText(String.valueOf(stats.getHp()));
                tvStr.setText(String.valueOf(stats.getStr()));
                tvSpd.setText(String.valueOf(stats.getSpd()));
                tvWis.setText(String.valueOf(stats.getWis()));
                tvPhyDef.setText(String.valueOf(stats.getPhyDef()));
                tvMagDef.setText(String.valueOf(stats.getMagDef()));
                tvCrit.setText(String.valueOf(stats.getCrit()));

                // base stats are now set
                isBaseStatsSet = true;

                // print stats to console
                printStats();
            } else {
                if (!isAnimationStarted) {
                    Logger.d(TAG, "updating base stats");
                    // base stats are set, only update stats
                    if (!Utils.isStringEmpty(String.valueOf(edtLv.getText())) &&
                            Integer.parseInt(String.valueOf(edtLv.getText())) > 1) {
                        // level directly influences the increase in stats
                        int level = Integer.parseInt(String.valueOf(edtLv.getText()));
                        double multiplierStats = (level - 1) * MULTIPLIER_LEVEL_STATS;

                        // update asc
                        tvMaxAsc.setText(FORWARD_SLASH.concat(String.valueOf(stats.getMaxAsc())));

                        // update hp
                        int hp = stats.getHp();
                        tvHp.setText(String.valueOf((int) (hp + (hp * multiplierStats))));

                        // update str
                        int str = stats.getStr();
                        tvStr.setText(String.valueOf((int) (str + (str * multiplierStats))));

                        // update spd
                        int spd = stats.getSpd();
                        tvSpd.setText(String.valueOf((int) (spd + (spd * multiplierStats))));

                        // update wis
                        int wis = stats.getWis();
                        tvWis.setText(String.valueOf((int) (wis + (wis * multiplierStats))));

                        // update phyDef
                        int phyDef = stats.getPhyDef();
                        tvPhyDef.setText(String.valueOf((int) (phyDef + (phyDef * multiplierStats))));

                        // update magDef
                        int magDef = stats.getMagDef();
                        tvMagDef.setText(String.valueOf((int) (magDef + (magDef * multiplierStats))));

                        // update crit
                        int crit = stats.getCrit();
                        tvCrit.setText(String.valueOf((int) (crit + (crit * multiplierStats))));

                        // print stats to console
                        printStats();
                    }
                }
            }
        }
    }

    /**
     * Method is used to print generated stats to console
     */
    private void printStats() {
        Logger.i(TAG, "affinity: " + mSelectedAffinity);
        Logger.i(TAG, "card type: " + mSelectedCardType);
        Logger.i(TAG, "level: " + edtLv.getText());
        Logger.i(TAG, "asc: " + edtAsc.getText());
        Logger.i(TAG, "hp: " + tvHp.getText());
        Logger.i(TAG, "str: " + tvStr.getText());
        Logger.i(TAG, "spd: " + tvSpd.getText());
        Logger.i(TAG, "wisdom: " + tvWis.getText());
        Logger.i(TAG, "phy def: " + tvPhyDef.getText());
        Logger.i(TAG, "mag def: " + tvMagDef.getText());
        Logger.i(TAG, "crit%: " + tvCrit.getText());
        Logger.i(TAG, "---------------------------------------------");
    }

    /**
     * Method is used to make getGuardians request
     */
    private void getGuardians() {
        // show progress dialog
        DialogUtils.showProgressDialog(mContext);

        ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError googleError, int resultCode) {
                // do nothing
            }

            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                volleyError.printStackTrace();
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog();
                // display error dialog
                mErrorUtils.showError(MainActivity.this, getResources().getString(R.string.default_error_message));
            }
        };
        GsonObjectListener<GetGuardiansRS> listener = new GsonObjectListener<>(new GsonObjectListener.OnResponse<GetGuardiansRS>() {
            @Override
            public void onResponse(GetGuardiansRS response) {
                if (!Utils.checkIfNull(response) && !Utils.checkIfNull(response.data)) {
                    populateUpdateGuardianStatsList(response.data);
                } else {
                    // dismiss loading dialog
                    DialogUtils.dismissProgressDialog();
                }
            }
        }, errorListener, GetGuardiansRS.class);
        mRequestManager.createGetRequest(new GetGuardiansRQ(), listener, errorListener,
                UrlConstants.HV_GET_GUARDIANS_URL_CREATE);
    }

    /**
     * Method is used to populate list of requests for updating Guardian stats
     *
     * @param data List of Guardian data
     */
    private void populateUpdateGuardianStatsList(ArrayList<Data> data) {
        ArrayList<UpdateBaseStats> alUpdateBaseStats = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            // generate stats
            GuardianStats guardianStats = statUtils.getStats(
                    statUtils.getCardType(i < 6 ? "SQUAD_LEADER" : "COMMON"),
                    statUtils.getAffinity("ROBOTIC"),
                    0);

            for (int n = 0; n < data.get(i).stats.size(); n++) {
                // update stats with generated stats
                UpdateBaseStats updateBaseStats = new UpdateBaseStats();
                updateBaseStats.id = Integer.parseInt(data.get(i).stats.get(n).id);
                updateBaseStats.statId = Integer.parseInt(data.get(i).stats.get(n).statId);
                if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.HEALTH.toString())) {
                    // represents HP
                    updateBaseStats.statValue = guardianStats.getHp();
                } else if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.SPEED.toString())) {
                    // represents Speed
                    updateBaseStats.statValue = guardianStats.getSpd();
                } else if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.STRENGTH.toString())) {
                    // represents Strength
                    updateBaseStats.statValue = guardianStats.getStr();
                } else if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.WISDOM.toString())) {
                    // represents Wisdom
                    updateBaseStats.statValue = guardianStats.getSpd();
                } else if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.PHYSICAL_RESISTANCE.toString())) {
                    // represents Physical Resistance
                    updateBaseStats.statValue = guardianStats.getPhyDef();
                } else if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.MAGICAL_RESISTANCE.toString())) {
                    // represents Magical Resistance
                    updateBaseStats.statValue = guardianStats.getMagDef();
                } else if (data.get(i).stats.get(n).code.equalsIgnoreCase(Enum.Stats.CRITICAL_PERCENT.toString())) {
                    // represents Critical Percent
                    updateBaseStats.statValue = guardianStats.getCrit();
                }
                // 0 is false, 1 is true
                updateBaseStats.active = 1;
                updateBaseStats.archived = 0;
                updateBaseStats.deleted = 0;
                // add updated stat
                alUpdateBaseStats.add(updateBaseStats);

                if (n == (data.get(i).stats.size() - 1)) {
                    // add formed request for updateBaseStats(bulk)
                    UpdateGuardianBaseStatsBulkRQ updateGuardianBaseStatsBulkRQ = new UpdateGuardianBaseStatsBulkRQ();
                    updateGuardianBaseStatsBulkRQ.guardianId = Integer.parseInt(data.get(i).id);
                    updateGuardianBaseStatsBulkRQ.stats = alUpdateBaseStats;
                    alUpdateGuardianBaseStatsBulkRQ.add(updateGuardianBaseStatsBulkRQ);
                    // clear list
                    alUpdateBaseStats.clear();
                }
            }
        }

        // make updateGuardianStats(bulk) request
        updateGuardianStats();
    }

    /**
     * Method is used to make updateGuardianStats(bulk) requests
     */
    private void updateGuardianStats() {
        if (updateGuardianCounter == 0) {
            // show progress dialog
            DialogUtils.showProgressDialog(mContext);
        }

        ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError googleError, int resultCode) {
                // do nothing
            }

            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                volleyError.printStackTrace();
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog();
                // display error dialog
                mErrorUtils.showError(MainActivity.this, getResources().getString(R.string.default_error_message));
            }
        };
        GsonObjectListener<UpdateGuardianBaseStatsBulkRS> listener = new GsonObjectListener<>(new GsonObjectListener.OnResponse<UpdateGuardianBaseStatsBulkRS>() {
            @Override
            public void onResponse(UpdateGuardianBaseStatsBulkRS response) {
                // increase counter
                updateGuardianCounter++;

                if (updateGuardianCounter < alUpdateGuardianBaseStatsBulkRQ.size()) {
                    // recursively call updateGuardianStats() with short delay
                    updateGuardianStats();
                } else {
                    // reset
                    updateGuardianCounter = 0;
                    alUpdateGuardianBaseStatsBulkRQ.clear();
                    // dismiss loading dialog
                    DialogUtils.dismissProgressDialog();
                    // successful TCoin purchase
                    DialogUtils.showDefaultOKAlert(MainActivity.this, getResources().getString(R.string.successful_update_stats_title),
                            getResources().getString(R.string.successful_update_stats_message));
                }
            }
        }, errorListener, UpdateGuardianBaseStatsBulkRS.class);
        mRequestManager.createPostRequest(alUpdateGuardianBaseStatsBulkRQ.get(updateGuardianCounter),
                listener, errorListener, UrlConstants.HV_UPDATE_BASE_STATS_BULK_URL_CREATE);

    }

    @Override
    protected void onPause() {
        // unregisters a listener for all sensors
        mSensorManager.unregisterListener(mSensorListener);
        // set flag
        isPaused = true;

        // remove network observer
        if (!Utils.checkIfNull(mNetworkReceiver) &&
                mNetworkReceiver.getObserverSize() > 0 && mNetworkReceiver.contains(this)) {
            try {
                // unregister network receiver
                unregisterReceiver(mNetworkReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mNetworkReceiver.removeObserver(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set flag
        isPaused = false;
        // events will be delivered to the provided SensorEventListener as soon as they are available
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        // only register receiver if it has not already been registered
        if (!Utils.checkIfNull(mNetworkReceiver) && !mNetworkReceiver.contains(this)) {
            // register network receiver
            mNetworkReceiver.addObserver(this);
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            // print observer list
            mNetworkReceiver.printObserverList();
        }
    }

    @Override
    protected void onDestroy() {
        // stop tooltip timer
        stopTooltipTimer();

        // remove network observer
        if (!Utils.checkIfNull(mNetworkReceiver) &&
                mNetworkReceiver.getObserverSize() > 0 && mNetworkReceiver.contains(this)) {
            try {
                // unregister network receiver
                unregisterReceiver(mNetworkReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mNetworkReceiver.removeObserver(this);
        }
        super.onDestroy();
    }

    @Override
    public void notifyConnectionChange(boolean isConnected) {
        if (isConnected) {
            // app is connected to network
            DialogUtils.dismissNoNetworkDialog();
        } else {
            // app is not connected to network
            DialogUtils.showDefaultNoNetworkAlert(this, null,
                    this.getResources().getString(R.string.check_network));
        }
    }
}
