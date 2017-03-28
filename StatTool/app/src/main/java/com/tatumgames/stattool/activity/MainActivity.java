/**
 * Copyright 2013-present Tatum Games, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tatumgames.stattool.R;
import com.tatumgames.stattool.enums.Affinity;
import com.tatumgames.stattool.enums.CardType;
import com.tatumgames.stattool.helper.InputFilterMinMax;
import com.tatumgames.stattool.listener.ShakeEventListener;
import com.tatumgames.stattool.logger.Logger;
import com.tatumgames.stattool.model.Stats;
import com.tatumgames.stattool.utils.Utils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.tatumgames.stattool.utils.Utils.isStringEmpty;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private Stats stats;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Vibrator v;
    private Spinner spnAffinity, spnType;
    private EditText edtLv, edtAsc;
    private TextView tvHp, tvStr, tvSpd, tvWis, tvPhyDef, tvMagDef, tvCrit, tvTooltip, tvMaxLv, tvMaxAsc,
            tvResetToBase;
    private Button btnRegenerate;

    private boolean isDefaultSet, isPaused, isEditable, isBaseStatsSet, isAnimationStarted;
    private String mSelectedAffinity, mSelectedType;
    private String[] arryAffinity, arryType;
    private Random r;
    private Timer mTimer;

    private static final int VIBRATE_TIMER = 500; // milliseconds
    private static final int TIMER = 60000; // milliseconds
    private static final int CLICK_THRESHOLD = 500; // milliseconds
    private static final int MAX_LEVEL_FILTER_VALUE = 20;
    private static final double MULTIPLIER_LEVEL_STATS = 0.05; // percent
    private static final double MULTIPLIER_TIER_A = 0.6; // arbitrary value
    private static final double MULTIPLIER_TIER_B = 0.55; // arbitrary value
    private static final double MULTIPLIER_TIER_C = 0.5; // arbitrary value
    private static final double MULTIPLIER_TIER_D = 0.4; // arbitrary value
    private static final double MULTIPLIER_TIER_E = 0.3; // arbitrary value
    private static final double MULTIPLIER_TIER_F = 0.15; // arbitrary value
    private static final String SELECT_OPTION = "SELECT OPTION";
    private Animation animation;

    private int mMaxLv;
    private long mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIds();
    }

    /**
     * Method is used to instantiate objects and views
     */
    private void getIds() {
        mContext = MainActivity.this;
        stats = new Stats();
        r = new Random();
        mTimer = new Timer();
        v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        animation = new AlphaAnimation(1.0F, 0.6F);
        edtLv = (EditText) findViewById(R.id.edt_lv);
        edtAsc = (EditText) findViewById(R.id.edt_asc);
        tvHp = (TextView) findViewById(R.id.tv_hp);
        tvStr = (TextView) findViewById(R.id.tv_str);
        tvSpd = (TextView) findViewById(R.id.tv_spd);
        tvWis = (TextView) findViewById(R.id.tv_wis);
        tvPhyDef = (TextView) findViewById(R.id.tv_phy_def);
        tvMagDef = (TextView) findViewById(R.id.tv_mag_def);
        tvCrit = (TextView) findViewById(R.id.tv_crit);
        tvTooltip = (TextView) findViewById(R.id.tv_tooltip);
        tvMaxLv = (TextView) findViewById(R.id.tv_max_lv);
        tvMaxAsc = (TextView) findViewById(R.id.tv_max_asc);
        tvResetToBase = (TextView) findViewById(R.id.tv_reset_to_base);
        spnAffinity = (Spinner) findViewById(R.id.spn_affinity);
        spnType = (Spinner) findViewById(R.id.spn_type);
        arryAffinity = getResources().getStringArray(R.array.arryAffinity);
        arryType = getResources().getStringArray(R.array.arryCardType);
        btnRegenerate = (Button) findViewById(R.id.btn_regenerate);

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

        // set on click listener
        btnRegenerate.setOnClickListener(this);
        tvResetToBase.setOnClickListener(this);

        // set on item click listener
        spnAffinity.setOnItemSelectedListener(this);
        spnType.setOnItemSelectedListener(this);

        // set adapter for arrays
        ArrayAdapter<String> adapterAffinity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arryAffinity);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arryType);
        spnAffinity.setAdapter(adapterAffinity);
        spnType.setAdapter(adapterType);

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

        edtAsc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (Utils.isStringEmpty(String.valueOf(edtAsc.getText()))) {
                    edtAsc.setText("0");
                    tvMaxLv.setText("/ " + String.valueOf(getMaxLv(getCardType(mSelectedType), Integer.parseInt(String.valueOf(edtAsc.getText())))));

                    int currLv = Integer.parseInt(String.valueOf(edtLv.getText()));
                    if (currLv > mMaxLv) {
                        // adjust max lv
                        Logger.d(TAG, "adjusting maxLv: " + mMaxLv);
                        edtLv.setText(String.valueOf(mMaxLv));
                    }
                }
                return false;
            }
        });

        // set editText text change listener
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
                    tvMaxLv.setText("/ " + String.valueOf(getMaxLv(getCardType(mSelectedType), Integer.parseInt(String.valueOf(edtAsc.getText())))));

                    int currLv = Integer.parseInt(String.valueOf(edtLv.getText()));
                    if (currLv > mMaxLv) {
                        // adjust max lv
                        Logger.d(TAG, "adjusting maxLv: " + mMaxLv);
                        edtLv.setText(String.valueOf(mMaxLv));
                    }
                }
            }
        });

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

        // set editText text change listener
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
                if (!Utils.isStringEmpty(String.valueOf(edtLv.getText())) && isBaseStatsSet &&
                        Integer.parseInt(String.valueOf(edtLv.getText())) > 1) {
                    setStats();
                }
            }
        });

        // set default tooltip
        showRandomTooltip();
    }

    /**
     * Method is used to show a randomized tooltip
     */
    private void showRandomTooltip() {
        if (Utils.isStringEmpty(mSelectedAffinity) || Utils.isStringEmpty(mSelectedType) ||
                mSelectedAffinity.equalsIgnoreCase(SELECT_OPTION) ||
                mSelectedType.equalsIgnoreCase(SELECT_OPTION)) {
            if (isDefaultSet) {
                return;
            }

            // set default tooltip message
            isDefaultSet = true;
            tvTooltip.setText(getResources().getString(R.string.tooltip_default));
            startTooltipTimer();
        } else {
            // set random tooltip message
            int rand = r.nextInt(9);
            if (rand == 0) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_change_lv_and_asc));
            } else if (rand == 1) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_reset_stats));
            } else if (rand == 2) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_stats_increase));
            } else if (rand == 3) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_asc_increase));
            } else if (rand == 4) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_physical_defense));
            } else if (rand == 5) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_magical_defense));
            } else if (rand == 6) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_strength_attack));
            } else if (rand == 7) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_wisdom_attack));
            } else if (rand == 8) {
                tvTooltip.setText(getResources().getString(R.string.tooltip_critical_attack));
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

    // (C) hp base: [350-400]
    // (R) hp base: [375-450]
    // (E) hp base: [425-500]
    // (L) hp base: [500-600]
    // (M) hp base: [600-750]
    // (SL) hp base: [400-475]
    private int randHpStatHigh(CardType cardType, Affinity affinity) {
        int hp = 0;

        if (cardType.equals(CardType.COMMON)) {
            hp = r.nextInt(51) + 350;
        } else if (cardType.equals(CardType.RARE)) {
            hp = r.nextInt(76) + 375;
        } else if (cardType.equals(CardType.EPIC)) {
            hp = r.nextInt(76) + 425;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            hp = r.nextInt(101) + 500;
        } else if (cardType.equals(CardType.MYTHIC)) {
            hp = r.nextInt(151) + 600;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            hp = r.nextInt(76) + 400;
        }

        int baseHp = hp + randHpByAffinity(affinity, hp);
        stats.setHp(baseHp);
        return baseHp;
    }

    // multiplier: 0.6, 0.55, 0.5, 0.4, 0.3, 0.15
    // Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
    // Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
    // Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
    // Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
    // Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
    // Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
    private int randHpByAffinity(Affinity affinity, int hp) {
        int hpByAffinity = 0;

        if (affinity.equals(Affinity.ROBOTIC)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER_C);
        } else if (affinity.equals(Affinity.PHYSICAL)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER_A);
        } else if (affinity.equals(Affinity.BEAST)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER_D);
        } else if (affinity.equals(Affinity.ELEMENTAL)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER_B);
        } else if (affinity.equals(Affinity.PSYCHIC)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER_E);
        } else if (affinity.equals(Affinity.BRAINIAC)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER_F);
        }

        return hpByAffinity;
    }

    // (C) str base: [35-40]
    // (R) str base: [37-45]
    // (E) str base: [42-50]
    // (L) str base: [50-60]
    // (M) str base: [60-75]
    // (SL) str base: [40-48]
    private int randStrStat(CardType cardType, Affinity affinity) {
        int str = 0;

        if (cardType.equals(CardType.COMMON)) {
            str = r.nextInt(6) + 35;
        } else if (cardType.equals(CardType.RARE)) {
            str = r.nextInt(9) + 37;
        } else if (cardType.equals(CardType.EPIC)) {
            str = r.nextInt(9) + 42;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            str = r.nextInt(11) + 50;
        } else if (cardType.equals(CardType.MYTHIC)) {
            str = r.nextInt(16) + 60;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            str = r.nextInt(9) + 40;
        }

        int baseStr = str + randStrByAffinity(affinity, str);
        stats.setStr(baseStr);
        return baseStr;
    }

    // multiplier: 0.6, 0.55, 0.5, 0.4, 0.3, 0.15
    // Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
    // Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
    // Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
    // Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
    // Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
    // Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
    private int randStrByAffinity(Affinity affinity, int str) {
        int strByAffinity = 0;

        if (affinity.equals(Affinity.ROBOTIC)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER_A);
        } else if (affinity.equals(Affinity.PHYSICAL)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER_B);
        } else if (affinity.equals(Affinity.BEAST)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER_C);
        } else if (affinity.equals(Affinity.ELEMENTAL)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER_D);
        } else if (affinity.equals(Affinity.PSYCHIC)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER_F);
        } else if (affinity.equals(Affinity.BRAINIAC)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER_E);
        }

        return strByAffinity;
    }

    // (C) spd base: [35-40]
    // (R) spd base: [37-45]
    // (E) spd base: [42-50]
    // (L) spd base: [50-60]
    // (M) spd base: [60-75]
    // (SL) spd base: [40-48]
    private int randSpdStat(CardType cardType, Affinity affinity) {
        int spd = 0;

        if (cardType.equals(CardType.COMMON)) {
            spd = r.nextInt(6) + 35;
        } else if (cardType.equals(CardType.RARE)) {
            spd = r.nextInt(9) + 37;
        } else if (cardType.equals(CardType.EPIC)) {
            spd = r.nextInt(9) + 42;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            spd = r.nextInt(11) + 50;
        } else if (cardType.equals(CardType.MYTHIC)) {
            spd = r.nextInt(16) + 60;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            spd = r.nextInt(9) + 40;
        }

        int baseSpd = spd + randSpdByAffinity(affinity, spd);
        stats.setSpd(baseSpd);
        return baseSpd;
    }

    // multiplier: 0.6, 0.55, 0.5, 0.4, 0.3, 0.15
    // Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
    // Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
    // Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
    // Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
    // Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
    // Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
    private int randSpdByAffinity(Affinity affinity, int spd) {
        int spdByAffinity = 0;

        if (affinity.equals(Affinity.ROBOTIC)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER_F);
        } else if (affinity.equals(Affinity.PHYSICAL)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER_E);
        } else if (affinity.equals(Affinity.BEAST)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER_A);
        } else if (affinity.equals(Affinity.ELEMENTAL)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER_E);
        } else if (affinity.equals(Affinity.PSYCHIC)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER_B);
        } else if (affinity.equals(Affinity.BRAINIAC)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER_D);
        }

        return spdByAffinity;
    }

    // (C) wis base: [35-40]
    // (R) wis base: [37-45]
    // (E) wis base: [42-50]
    // (L) wis base: [50-60]
    // (M) wis base: [60-75]
    // (SL) wis base: [40-48]
    private int randWisStat(CardType cardType, Affinity affinity) {
        int wis = 0;

        if (cardType.equals(CardType.COMMON)) {
            wis = r.nextInt(6) + 35;
        } else if (cardType.equals(CardType.RARE)) {
            wis = r.nextInt(9) + 37;
        } else if (cardType.equals(CardType.EPIC)) {
            wis = r.nextInt(9) + 42;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            wis = r.nextInt(11) + 50;
        } else if (cardType.equals(CardType.MYTHIC)) {
            wis = r.nextInt(16) + 60;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            wis = r.nextInt(9) + 40;
        }

        int baseWis = wis + randWisByAffinity(affinity, wis);
        stats.setWis(baseWis);
        return baseWis;
    }

    // multiplier: 0.6, 0.55, 0.5, 0.4, 0.3, 0.15
    // Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
    // Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
    // Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
    // Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
    // Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
    // Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
    private int randWisByAffinity(Affinity affinity, int wis) {
        int wisByAffinity = 0;

        if (affinity.equals(Affinity.ROBOTIC)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER_E);
        } else if (affinity.equals(Affinity.PHYSICAL)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER_F);
        } else if (affinity.equals(Affinity.BEAST)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER_F);
        } else if (affinity.equals(Affinity.ELEMENTAL)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER_A);
        } else if (affinity.equals(Affinity.PSYCHIC)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER_A);
        } else if (affinity.equals(Affinity.BRAINIAC)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER_A);
        }

        return wisByAffinity;
    }

    // (C) phyDef base: [35-40]
    // (R) phyDef base: [37-45]
    // (E) phyDef base: [42-50]
    // (L) phyDef base: [50-60]
    // (M) phyDef base: [60-75]
    // (SL) phyDef base: [40-48]
    private int randPhyDefStat(CardType cardType, Affinity affinity) {
        int phyDef = 0;

        if (cardType.equals(CardType.COMMON)) {
            phyDef = r.nextInt(6) + 35;
        } else if (cardType.equals(CardType.RARE)) {
            phyDef = r.nextInt(9) + 37;
        } else if (cardType.equals(CardType.EPIC)) {
            phyDef = r.nextInt(9) + 42;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            phyDef = r.nextInt(11) + 50;
        } else if (cardType.equals(CardType.MYTHIC)) {
            phyDef = r.nextInt(16) + 60;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            phyDef = r.nextInt(9) + 40;
        }

        int basePhyDef = phyDef + randPhyDefByAffinity(affinity, phyDef);
        stats.setPhyDef(basePhyDef);
        return basePhyDef;
    }

    // multiplier: 0.6, 0.55, 0.5, 0.4, 0.3, 0.15
    // Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
    // Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
    // Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
    // Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
    // Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
    // Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
    private int randPhyDefByAffinity(Affinity affinity, int phyDef) {
        int phyDefByAffinity = 0;

        if (affinity.equals(Affinity.ROBOTIC)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER_D);
        } else if (affinity.equals(Affinity.PHYSICAL)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER_C);
        } else if (affinity.equals(Affinity.BEAST)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER_E);
        } else if (affinity.equals(Affinity.ELEMENTAL)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER_C);
        } else if (affinity.equals(Affinity.PSYCHIC)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER_C);
        } else if (affinity.equals(Affinity.BRAINIAC)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER_B);
        }

        return phyDefByAffinity;
    }

    // (C) magDef base: [35-40]
    // (R) magDef base: [37-45]
    // (E) magDef base: [42-50]
    // (L) magDef base: [50-60]
    // (M) magDef base: [60-75]
    // (SL) magDef base: [40-48]
    private int randMagDefStat(CardType cardType, Affinity affinity) {
        int magDef = 0;

        if (cardType.equals(CardType.COMMON)) {
            magDef = r.nextInt(6) + 35;
        } else if (cardType.equals(CardType.RARE)) {
            magDef = r.nextInt(9) + 37;
        } else if (cardType.equals(CardType.EPIC)) {
            magDef = r.nextInt(9) + 42;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            magDef = r.nextInt(11) + 50;
        } else if (cardType.equals(CardType.MYTHIC)) {
            magDef = r.nextInt(16) + 60;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            magDef = r.nextInt(9) + 40;
        }

        int baseMagDef = magDef + randMagDefByAffinity(affinity, magDef);
        stats.setMagDef(baseMagDef);
        return baseMagDef;
    }

    // multiplier: 0.6, 0.55, 0.5, 0.4, 0.3, 0.15
    // Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
    // Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
    // Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
    // Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
    // Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
    // Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
    private int randMagDefByAffinity(Affinity affinity, int magDef) {
        int magDefByAffinity = 0;

        if (affinity.equals(Affinity.ROBOTIC)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER_B);
        } else if (affinity.equals(Affinity.PHYSICAL)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER_D);
        } else if (affinity.equals(Affinity.BEAST)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER_B);
        } else if (affinity.equals(Affinity.ELEMENTAL)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER_F);
        } else if (affinity.equals(Affinity.PSYCHIC)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER_D);
        } else if (affinity.equals(Affinity.BRAINIAC)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER_C);
        }

        return magDefByAffinity;
    }

    // (C) critPerc base: [2]
    // (R) critPerc base: [4]
    // (E) critPerc base: [6]
    // (L) critPerc base: [8]
    // (M) critPerc base: [10]
    // (SL) critPerc base: [5]
    // crit percentage is not affinity linked
    private int randCritPercStat(CardType cardType) {
        if (cardType.equals(CardType.COMMON)) {
            stats.setCrit(2);
            return 2;
        } else if (cardType.equals(CardType.RARE)) {
            stats.setCrit(4);
            return 4;
        } else if (cardType.equals(CardType.EPIC)) {
            stats.setCrit(6);
            return 6;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            stats.setCrit(8);
            return 8;
        } else if (cardType.equals(CardType.MYTHIC)) {
            stats.setCrit(10);
            return 10;
        }
        // SQUAD_LEADER return value
        stats.setCrit(5);
        return 5;
    }

    /**
     * Retrieve base max level
     *
     * @param cardType
     * @return
     */
    private int getMaxLv(CardType cardType, int currAsc) {
        mMaxLv = 0;
        int ascIncValue = currAsc * 2;

        if (cardType.equals(CardType.COMMON)) {
            mMaxLv = 20 + ascIncValue;
        } else if (cardType.equals(CardType.RARE)) {
            mMaxLv = 24 + ascIncValue;
        } else if (cardType.equals(CardType.EPIC)) {
            mMaxLv = 28 + ascIncValue;
        } else if (cardType.equals(CardType.LEGENDARY)) {
            mMaxLv = 32 + ascIncValue;
        } else if (cardType.equals(CardType.MYTHIC)) {
            mMaxLv = 36 + ascIncValue;
        } else if (cardType.equals(CardType.SQUAD_LEADER)) {
            mMaxLv = 34;
        }

        // update edtText filter
        edtLv.setFilters(new InputFilter[]{new InputFilterMinMax(0, mMaxLv)});
        return mMaxLv;
    }

    /**
     * Retrieve maximum ascension level
     *
     * @note Squad Leader return 0 ascension by default.
     * @param cardType
     * @return
     */
    private int getMaxAsc(CardType cardType) {
        // SQUAD_LEADER max ascension
        int maxAsc = 0;

        if (cardType.equals(CardType.COMMON) ||
                cardType.equals(CardType.RARE) ||
                cardType.equals(CardType.EPIC)) {
            maxAsc = 3;
        } else if (cardType.equals(CardType.LEGENDARY) ||
                cardType.equals(CardType.MYTHIC)) {
            maxAsc = 4;
        }

        // update edtText filter
        edtAsc.setFilters(new InputFilter[]{new InputFilterMinMax(0, maxAsc)});
        return maxAsc;
    }

    /**
     * Helper method to convert String affinity to Affinity object
     *
     * @param affinity
     * @return
     */
    private Affinity getAffinity(String affinity) {
        if (affinity.equalsIgnoreCase(String.valueOf(Affinity.ROBOTIC))) {
            stats.setAffinity(Affinity.ROBOTIC);
            return Affinity.ROBOTIC;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Affinity.PHYSICAL))) {
            stats.setAffinity(Affinity.PHYSICAL);
            return Affinity.PHYSICAL;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Affinity.BEAST))) {
            stats.setAffinity(Affinity.BEAST);
            return Affinity.BEAST;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Affinity.ELEMENTAL))) {
            stats.setAffinity(Affinity.ELEMENTAL);
            return Affinity.ELEMENTAL;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Affinity.PSYCHIC))) {
            stats.setAffinity(Affinity.PSYCHIC);
            return Affinity.PSYCHIC;
        }
        stats.setAffinity(Affinity.BRAINIAC);
        return Affinity.BRAINIAC;
    }

    /**
     * Helper method to convert String cardType to CardType object
     *
     * @param cardType
     * @return
     */
    private CardType getCardType(String cardType) {
        if (cardType.equalsIgnoreCase(String.valueOf(CardType.COMMON))) {
            stats.setCardType(CardType.COMMON);
            return CardType.COMMON;
        } else if (cardType.equalsIgnoreCase(String.valueOf(CardType.RARE))) {
            stats.setCardType(CardType.RARE);
            return CardType.RARE;
        } else if (cardType.equalsIgnoreCase(String.valueOf(CardType.EPIC))) {
            stats.setCardType(CardType.EPIC);
            return CardType.EPIC;
        } else if (cardType.equalsIgnoreCase(String.valueOf(CardType.LEGENDARY))) {
            stats.setCardType(CardType.LEGENDARY);
            return CardType.LEGENDARY;
        } else if (cardType.equalsIgnoreCase(String.valueOf(CardType.MYTHIC))) {
            stats.setCardType(CardType.MYTHIC);
            return CardType.MYTHIC;
        }
        stats.setCardType(CardType.SQUAD_LEADER);
        return CardType.SQUAD_LEADER;
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

        edtLv.setText("1");
        edtAsc.setText("0");
        tvMaxLv.setText(getResources().getString(R.string.num));
        tvMaxAsc.setText(getResources().getString(R.string.num));
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
        if (!Utils.isStringEmpty(mSelectedAffinity) && !Utils.isStringEmpty(mSelectedType) &&
                !mSelectedAffinity.equalsIgnoreCase(SELECT_OPTION) &&
                !mSelectedType.equalsIgnoreCase(SELECT_OPTION)) {
            edtLv.setText("1");
            edtAsc.setText("0");
            tvMaxLv.setText("/ " + String.valueOf(getMaxLv(stats.getCardType(), 0)));
            tvMaxAsc.setText("/ " + String.valueOf(getMaxAsc(stats.getCardType())));
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
                }
                setStats();
                break;
            case R.id.tv_reset_to_base:
                resetToBaseStats();
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
            case R.id.spn_type:
                // start animation to prompt user that stats will not regenerate automatically
                if (!Utils.isStringEmpty(mSelectedType)) {
                    if (!isAnimationStarted && isBaseStatsSet) {
                        isAnimationStarted = true;
                        btnRegenerate.startAnimation(animation);
                    }
                }

                // set selected type
                mSelectedType = arryType[position];

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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Method is used to set stats
     */
    private void setStats() {
        if (!Utils.isStringEmpty(mSelectedAffinity) && !Utils.isStringEmpty(mSelectedType) &&
                !mSelectedAffinity.equalsIgnoreCase(SELECT_OPTION) &&
                !mSelectedType.equalsIgnoreCase(SELECT_OPTION)) {

            if (!isEditable) {
                // set editText action enabled
                isEditable = true;
                edtLv.setFocusable(true);
                edtLv.setFocusableInTouchMode(true);
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
                tvMaxLv.setText("/ " + String.valueOf(getMaxLv(getCardType(mSelectedType), Integer.parseInt(String.valueOf(edtAsc.getText())))));
                tvMaxAsc.setText("/ " + String.valueOf(getMaxAsc(getCardType(mSelectedType))));
                tvHp.setText(String.valueOf(randHpStatHigh(getCardType(mSelectedType), getAffinity(mSelectedAffinity))));
                tvStr.setText(String.valueOf(randStrStat(getCardType(mSelectedType), getAffinity(mSelectedAffinity))));
                tvSpd.setText(String.valueOf(randSpdStat(getCardType(mSelectedType), getAffinity(mSelectedAffinity))));
                tvWis.setText(String.valueOf(randWisStat(getCardType(mSelectedType), getAffinity(mSelectedAffinity))));
                tvPhyDef.setText(String.valueOf(randPhyDefStat(getCardType(mSelectedType), getAffinity(mSelectedAffinity))));
                tvMagDef.setText(String.valueOf(randMagDefStat(getCardType(mSelectedType), getAffinity(mSelectedAffinity))));
                tvCrit.setText(String.valueOf(randCritPercStat(getCardType(mSelectedType))));

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
                        tvMaxAsc.setText("/ " + String.valueOf(getMaxAsc(getCardType(mSelectedType))));

                        // update hp
                        int hp = stats.getHp();
                        tvHp.setText(String.valueOf((int)(hp + (hp * multiplierStats))));

                        // update str
                        int str = stats.getStr();
                        tvStr.setText(String.valueOf((int)(str + (str * multiplierStats))));

                        // update spd
                        int spd = stats.getSpd();
                        tvSpd.setText(String.valueOf((int)(spd + (spd * multiplierStats))));

                        // update wis
                        int wis = stats.getWis();
                        tvWis.setText(String.valueOf((int)(wis + (wis * multiplierStats))));

                        // update phyDef
                        int phyDef = stats.getPhyDef();
                        tvPhyDef.setText(String.valueOf((int)(phyDef + (phyDef * multiplierStats))));

                        // update magDef
                        int magDef = stats.getMagDef();
                        tvMagDef.setText(String.valueOf((int)(magDef + (magDef * multiplierStats))));

                        // update crit
                        int crit = stats.getCrit();
                        tvCrit.setText(String.valueOf((int)(crit + (crit * multiplierStats))));

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
        Logger.i(TAG, "card type: " + mSelectedType);
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

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        isPaused = true;
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onDestroy() {
        stopTooltipTimer();
        super.onDestroy();
    }
}
