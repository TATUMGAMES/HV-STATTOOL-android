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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.tatumgames.stattool.R;
import com.tatumgames.stattool.activity.BaseActivity;
import com.tatumgames.stattool.adapter.ApiAdapter;
import com.tatumgames.stattool.enums.Enum;
import com.tatumgames.stattool.listener.OnClickAdapterListener;
import com.tatumgames.stattool.logger.Logger;
import com.tatumgames.stattool.model.GuardianStatsModel;
import com.tatumgames.stattool.model.MappingModel;
import com.tatumgames.stattool.requests.app.GetGuardiansRQ;
import com.tatumgames.stattool.requests.app.GetGuardiansRS;
import com.tatumgames.stattool.requests.app.UpdateGuardianBaseStatsBulkRQ;
import com.tatumgames.stattool.requests.app.UpdateGuardianBaseStatsBulkRS;
import com.tatumgames.stattool.requests.app.UpdateGuardianRQ;
import com.tatumgames.stattool.requests.app.UpdateGuardianRS;
import com.tatumgames.stattool.requests.app.UpdateMissionRQ;
import com.tatumgames.stattool.requests.app.UpdateMissionRS;
import com.tatumgames.stattool.requests.model.GuardianDetails;
import com.tatumgames.stattool.requests.model.UpdateBaseStats;
import com.tatumgames.stattool.utils.DialogUtils;
import com.tatumgames.stattool.utils.ErrorUtils;
import com.tatumgames.stattool.utils.StatUtils;
import com.tatumgames.stattool.utils.Utils;
import com.tatumgames.stattool.volley.constants.UrlConstants;
import com.tatumgames.stattool.volley.listeners.ErrorListener;
import com.tatumgames.stattool.volley.listeners.GsonObjectListener;

import java.util.ArrayList;

public class ApiFragment extends BaseFragment {
    public static final String TAG = ApiFragment.class.getSimpleName();

    // mission max difficulty iters
    private final int MAX_MISSION_DIFFICULTY_TIERS = 3;

    private Context mContext;
    private Activity mActivity;
    private View mRootView;

    private ErrorUtils mErrorUtils;
    private GuardianStatsModel mStats;
    private StatUtils mStatUtils;

    private ArrayList<UpdateGuardianBaseStatsBulkRQ> alUpdateGuardianBaseStatsBulkRQ;
    private ArrayList<UpdateGuardianRQ> alUpdateGuardianRQ;
    private ArrayList<UpdateMissionRQ> alUpdateMissionRQ;
    private int mUpdateGuardianCounter, mUpdateMissionCounter, mApiStackPos;

    // adapter
    private ApiAdapter mApiAdapter;
    private RecyclerView rvApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_apis, container, false);

        // instantiate views
        initializeViews();
        initializeListeners();
        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {
        // instantiate context and activity instance
        mContext = getActivity();
        mActivity = getActivity();
        mErrorUtils = new ErrorUtils();
        mStats = new GuardianStatsModel();
        mStatUtils = new StatUtils();
        alUpdateGuardianBaseStatsBulkRQ = new ArrayList<>();
        alUpdateGuardianRQ = new ArrayList<>();
        alUpdateMissionRQ = new ArrayList<>();

        // initialize adapter
        rvApi = mRootView.findViewById(R.id.rv_apis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvApi.setLayoutManager(layoutManager);
        mApiAdapter = new ApiAdapter(mContext, getResources().getStringArray(R.array.array_apis));
        rvApi.setAdapter(mApiAdapter);

    }

    /**
     * Method is used to initialize listeners and callbacks
     */
    private void initializeListeners() {
        ApiAdapter.onClickAdapterListener(new OnClickAdapterListener() {
            @Override
            public void onClick(int pos) {
                // set api position
                mApiStackPos = pos;

                // make getGuardians request
                getGuardians();
            }
        });
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
                mErrorUtils.showError(mActivity, getResources().getString(R.string.default_error_message));
            }
        };
        GsonObjectListener<GetGuardiansRS> listener = new GsonObjectListener<>(new GsonObjectListener.OnResponse<GetGuardiansRS>() {
            @Override
            public void onResponse(GetGuardiansRS response) {
                if (!Utils.checkIfNull(response) && !Utils.checkIfNull(response.data)) {

                    /*
                     * API Stack Order:
                     * 1) Update Affinity and Card Type
                     * 2) Update Base Stats
                     * 3) Update Mission Difficulty e.g. Alpha, Beta, Omega
                     * 4) Update Mission Areas e.g. Abandoned Circus, National Forest, ect
                     * 5) Update Mission Levels
                     * 6) Update Mission Opponents
                     */
                    if (mApiStackPos == 0) {
                        populateAffinityCardTypeList(response.data);
                    } else if (mApiStackPos == 1) {
                        populateUpdateGuardianStatsList(response.data);
                    } else if (mApiStackPos == 2) {
                        populateMissionDifficultyList();
                    } else if (mApiStackPos == 3) {

                    } else if (mApiStackPos == 4) {

                    } else if (mApiStackPos == 5) {

                    }


                } else {
                    // dismiss loading dialog
                    DialogUtils.dismissProgressDialog();
                }
            }
        }, errorListener, GetGuardiansRS.class);
        ((BaseActivity) mActivity).mRequestManager.createGetRequest(new GetGuardiansRQ(), listener, errorListener,
                UrlConstants.HV_GET_GUARDIANS_URL_CREATE);
    }

    /**
     * Method is used to populate list of requests for updating affinity and card types for Guardian stats
     *
     * @param data List of Guardian data for UpdateGuardian requests
     */
    private void populateAffinityCardTypeList(ArrayList<GuardianDetails> data) {
        // clear list
        alUpdateGuardianRQ.clear();
        for (int i = 0; i < data.size(); i++) {
            if (!Utils.isStringEmpty(data.get(i).id)) {
                // add formed request for updateGuardian
                UpdateGuardianRQ updateGuardianRQ = new UpdateGuardianRQ();
                updateGuardianRQ.data = data.get(i);

                // update affinityTypeId
                updateGuardianRQ.data.affinityTypeId = MappingModel.getAffinityIdByGuardianId(data.get(i).id);
                // update affinityTypeName
                updateGuardianRQ.data.affinityTypeName = MappingModel.getAffinityNameByGuardianId(data.get(i).id);
                // update cardTypeId
                updateGuardianRQ.data.cardTypeId = MappingModel.getCardTypeIdByGuardianId(data.get(i).id);
                // update cardTypeName
                updateGuardianRQ.data.cardTypeName = MappingModel.getCardTypeNameByGuardianId(data.get(i).id);
                // update description
                updateGuardianRQ.data.description = MappingModel.getDescriptionByGuardianId(mContext, data.get(i).id);
                // update short description
                updateGuardianRQ.data.shortDescription = !Utils.isStringEmpty(data.get(i).shortDescription) ? data.get(i).shortDescription : "";
                // update code
                updateGuardianRQ.data.code = !Utils.isStringEmpty(data.get(i).code) ? data.get(i).code : "";
                // update asset bundle
                updateGuardianRQ.data.assetBundle = !Utils.isStringEmpty(data.get(i).assetBundle) ? data.get(i).assetBundle : "";
                alUpdateGuardianRQ.add(updateGuardianRQ);
            }
        }

        // print formed request for updateGuardian
        printUpdateGuardianRQ();

        // make updateGuardian request
        updateGuardian();
    }

    /**
     * Method is used to populate list of requests for updating Guardian stats
     *
     * @param data List of Guardian data for updateGuardianStats(bulk) requests
     */
    private void populateUpdateGuardianStatsList(ArrayList<GuardianDetails> data) {
        for (int i = 0; i < data.size(); i++) {
            if (!Utils.isStringEmpty(data.get(i).cardTypeId) && !Utils.isStringEmpty(data.get(i).affinityTypeId)) {
                ArrayList<UpdateBaseStats> alUpdateBaseStats = new ArrayList<>();
                // generate stats
                // cardType and affinity are ids from getGuardianRS
//                GuardianStatsModel guardianStats = mStatUtils.getStats(
//                        MappingModel.getCardTypeByCardTypeId(Integer.parseInt(data.get(i).cardTypeId)),
//                        MappingModel.getAffinityByAffinityId(Integer.parseInt(data.get(i).affinityTypeId)),
//                        0);
                GuardianStatsModel guardianStats = mStatUtils.getStats(
                        i < 6 ? Enum.CardType.SQUAD_LEADER : Enum.CardType.COMMON,
                        MappingModel.getAffinityByAffinityId(Integer.parseInt(data.get(i).affinityTypeId)),
                        0);

                for (int n = 0; n < data.get(i).stats.size(); n++) {
                    if (!Utils.isStringEmpty(data.get(i).stats.get(n).id) && !Utils.isStringEmpty(data.get(i).stats.get(n).statTypeId)) {
                        // update stats with generated stats
                        UpdateBaseStats updateBaseStats = new UpdateBaseStats();
                        updateBaseStats.id = Integer.parseInt(data.get(i).stats.get(n).id);
                        updateBaseStats.statId = Integer.parseInt(data.get(i).stats.get(n).statTypeId);
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
                        }
                    }
                }
            }
        }

        // print formed request for updateGuardianBaseStats
        printUpdateGuardianBaseStatsRQ();

        // make updateGuardianStatsBulk(bulk) request
        updateGuardianStatsBulk();
    }

    /**
     * Method is used to make updateGuardian requests
     * <p>Although name, description and other attributes can be updated. This method is currently
     * being used to update affinity and cardType only. The other attributes have already been
     * updated</p>
     */
    private void updateGuardian() {
        if (mUpdateGuardianCounter == 0) {
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
                // reset
                mUpdateGuardianCounter = 0;
                alUpdateGuardianRQ.clear();
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog();
                // display error dialog
                mErrorUtils.showError(mActivity, getResources().getString(R.string.default_error_message));
            }
        };
        GsonObjectListener<UpdateGuardianRS> listener = new GsonObjectListener<>(new GsonObjectListener.OnResponse<UpdateGuardianRS>() {
            @Override
            public void onResponse(UpdateGuardianRS response) {
                // increase counter
                mUpdateGuardianCounter++;

                if (mUpdateGuardianCounter < alUpdateGuardianRQ.size()) {
                    // recursively call updateGuardian
                    updateGuardian();
                } else {
                    // reset
                    mUpdateGuardianCounter = 0;
                    alUpdateGuardianRQ.clear();
                    // dismiss loading dialog
                    DialogUtils.dismissProgressDialog();
                    // successful TCoin purchase
                    DialogUtils.showDefaultOKAlert(mActivity, getResources().getString(R.string.successful_update_guardian_title),
                            getResources().getString(R.string.successful_update_guardian_message));
                }
            }
        }, errorListener, UpdateGuardianRS.class);
        ((BaseActivity) mActivity).mRequestManager.createPostRequest(alUpdateGuardianRQ.get(mUpdateGuardianCounter),
                listener, errorListener, UrlConstants.HV_UPDATE_GUARDIAN_URL_CREATE);
    }

    /**
     * Method is used to make updateGuardianStatsBulk(bulk) requests
     */
    private void updateGuardianStatsBulk() {
        if (mUpdateGuardianCounter == 0) {
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
                // reset
                mUpdateGuardianCounter = 0;
                alUpdateGuardianBaseStatsBulkRQ.clear();
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog();
                // display error dialog
                mErrorUtils.showError(mActivity, getResources().getString(R.string.default_error_message));
            }
        };
        GsonObjectListener<UpdateGuardianBaseStatsBulkRS> listener = new GsonObjectListener<>(new GsonObjectListener.OnResponse<UpdateGuardianBaseStatsBulkRS>() {
            @Override
            public void onResponse(UpdateGuardianBaseStatsBulkRS response) {
                // increase counter
                mUpdateGuardianCounter++;

                if (mUpdateGuardianCounter < alUpdateGuardianBaseStatsBulkRQ.size()) {
                    // recursively call updateGuardianStatsBulk(bulk)
                    updateGuardianStatsBulk();
                } else {
                    // reset
                    mUpdateGuardianCounter = 0;
                    alUpdateGuardianBaseStatsBulkRQ.clear();
                    // dismiss loading dialog
                    DialogUtils.dismissProgressDialog();
                    // successful TCoin purchase
                    DialogUtils.showDefaultOKAlert(mActivity, getResources().getString(R.string.successful_update_stats_title),
                            getResources().getString(R.string.successful_update_stats_message));
                }
            }
        }, errorListener, UpdateGuardianBaseStatsBulkRS.class);
        ((BaseActivity) mActivity).mRequestManager.createPostRequest(alUpdateGuardianBaseStatsBulkRQ.get(mUpdateGuardianCounter),
                listener, errorListener, UrlConstants.HV_UPDATE_BASE_STATS_BULK_URL_CREATE);
    }

    /**
     * Method is used to populate list of requests for updating mission difficulty tiers
     */
    private void populateMissionDifficultyList() {
        for (int i = 0; i < MAX_MISSION_DIFFICULTY_TIERS; i++) {
            UpdateMissionRQ updateMissionRQ = new UpdateMissionRQ();
            updateMissionRQ.id = String.valueOf(i + 1);
            updateMissionRQ.difficultyLevelId = String.valueOf(i + 1);
            updateMissionRQ.code = "";

            if (i == 0) {
                updateMissionRQ.name = mContext.getResources().getString(R.string.alpha);
                updateMissionRQ.description = mContext.getResources().getString(R.string.alpha);
                updateMissionRQ.difficultyLevelName = mContext.getResources().getString(R.string.alpha);
            } else if (i == 1) {
                updateMissionRQ.name = mContext.getResources().getString(R.string.beta);
                updateMissionRQ.description = mContext.getResources().getString(R.string.beta);
                updateMissionRQ.difficultyLevelName = mContext.getResources().getString(R.string.beta);
            } else if (i == 2) {
                updateMissionRQ.name = mContext.getResources().getString(R.string.omega);
                updateMissionRQ.description = mContext.getResources().getString(R.string.omega);
                updateMissionRQ.difficultyLevelName = mContext.getResources().getString(R.string.omega);
            }
            // 0 is false, 1 is true
            updateMissionRQ.active = "1";
            updateMissionRQ.archived = "0";
            updateMissionRQ.deleted = "0";
            alUpdateMissionRQ.add(updateMissionRQ);
        }

        // make update mission request
        updateMissionDifficulty();
    }

    /**
     * Method is used to make updateMission requests
     */
    private void updateMissionDifficulty() {
        if (mUpdateMissionCounter == 0) {
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
                // reset
                mUpdateMissionCounter = 0;
                alUpdateMissionRQ.clear();
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog();
                // display error dialog
                mErrorUtils.showError(mActivity, getResources().getString(R.string.default_error_message));
            }
        };
        GsonObjectListener<UpdateMissionRS> listener = new GsonObjectListener<>(new GsonObjectListener.OnResponse<UpdateMissionRS>() {
            @Override
            public void onResponse(UpdateMissionRS response) {
                // increase counter
                mUpdateMissionCounter++;

                if (mUpdateMissionCounter < alUpdateMissionRQ.size()) {
                    // recursively call updateMissionDifficulty
                    updateMissionDifficulty();
                } else {
                    // reset
                    mUpdateMissionCounter = 0;
                    alUpdateMissionRQ.clear();
                    // dismiss loading dialog
                    DialogUtils.dismissProgressDialog();
                    // successful TCoin purchase
                    DialogUtils.showDefaultOKAlert(mActivity, getResources().getString(R.string.successful_update_mission_title),
                            getResources().getString(R.string.successful_update_mission_message));
                }
            }
        }, errorListener, UpdateMissionRS.class);
        ((BaseActivity) mActivity).mRequestManager.createPostRequest(alUpdateMissionRQ.get(mUpdateMissionCounter),
                listener, errorListener, UrlConstants.HV_UPDATE_MISSION_URL_CREATE);
    }

    /**
     * Method is used to print out all the update Guardian requests for affinity, card types and descriptions
     */
    private void printUpdateGuardianRQ() {
        for (int i = 0; i < alUpdateGuardianRQ.size(); i++) {
            Logger.i(TAG, "---------------------------------------------");
            // print GuardianId
            Logger.i(TAG, "guardianId: " + alUpdateGuardianRQ.get(i).data.id);
            Logger.i(TAG, "name: " + alUpdateGuardianRQ.get(i).data.name);
            Logger.i(TAG, "alias: " + alUpdateGuardianRQ.get(i).data.alias);
            Logger.i(TAG, "affinityId: " + alUpdateGuardianRQ.get(i).data.affinityTypeId);
            Logger.i(TAG, "affinityName: " + alUpdateGuardianRQ.get(i).data.affinityTypeName);
            Logger.i(TAG, "cardTypeId: " + alUpdateGuardianRQ.get(i).data.cardTypeId);
            Logger.i(TAG, "cardTypeName: " + alUpdateGuardianRQ.get(i).data.cardTypeName);
            Logger.i(TAG, "description: " + alUpdateGuardianRQ.get(i).data.description);
            Logger.i(TAG, "shortDescription: " + alUpdateGuardianRQ.get(i).data.shortDescription);
        }
    }

    /**
     * Method is used to print out all the update Guardian base stat requests
     */
    private void printUpdateGuardianBaseStatsRQ() {
        for (int i = 0; i < alUpdateGuardianBaseStatsBulkRQ.size(); i++) {
            Logger.i(TAG, "---------------------------------------------");
            // print GuardianId
            Logger.i(TAG, "guardianId: " + alUpdateGuardianBaseStatsBulkRQ.get(i).guardianId);
            for (int n = 0; n < alUpdateGuardianBaseStatsBulkRQ.get(i).stats.size(); n++) {
                // print Id (primary key)
                Logger.i(TAG, "id: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).id);
                if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 1) {
                    // print HP
                    Logger.i(TAG, "hp: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                } else if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 2) {
                    // print Speed
                    Logger.i(TAG, "spd: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                } else if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 3) {
                    // print Strength
                    Logger.i(TAG, "str: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                } else if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 4) {
                    // print Wisdom
                    Logger.i(TAG, "wisdom: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                } else if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 5) {
                    // print Physical Resistance
                    Logger.i(TAG, "phy def: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                } else if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 6) {
                    // print Magical Resistance
                    Logger.i(TAG, "mag def: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                } else if (alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statId == 7) {
                    // print Critical Percent
                    Logger.i(TAG, "crit%: " + alUpdateGuardianBaseStatsBulkRQ.get(i).stats.get(n).statValue);
                }
            }
        }
    }

}
