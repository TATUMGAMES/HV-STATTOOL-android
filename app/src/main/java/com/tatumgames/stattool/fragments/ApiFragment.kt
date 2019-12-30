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

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.volley.VolleyError
import com.tatumgames.stattool.R
import com.tatumgames.stattool.activity.BaseActivity
import com.tatumgames.stattool.adapter.ApiAdapter
import com.tatumgames.stattool.enums.Enum
import com.tatumgames.stattool.listener.OnClickAdapterListener
import com.tatumgames.stattool.logger.Logger
import com.tatumgames.stattool.model.GuardianStatsModel
import com.tatumgames.stattool.model.MappingModel
import com.tatumgames.stattool.requests.app.GetGuardiansRQ
import com.tatumgames.stattool.requests.app.GetGuardiansRS
import com.tatumgames.stattool.requests.app.UpdateGuardianBaseStatsBulkRQ
import com.tatumgames.stattool.requests.app.UpdateGuardianBaseStatsBulkRS
import com.tatumgames.stattool.requests.app.UpdateGuardianRQ
import com.tatumgames.stattool.requests.app.UpdateGuardianRS
import com.tatumgames.stattool.requests.app.UpdateMissionRQ
import com.tatumgames.stattool.requests.app.UpdateMissionRS
import com.tatumgames.stattool.requests.model.GuardianDetails
import com.tatumgames.stattool.requests.model.UpdateBaseStats
import com.tatumgames.stattool.utils.DialogUtils
import com.tatumgames.stattool.utils.ErrorUtils
import com.tatumgames.stattool.utils.StatUtils
import com.tatumgames.stattool.utils.Utils
import com.tatumgames.stattool.volley.constants.UrlConstants
import com.tatumgames.stattool.volley.listeners.ErrorListener
import com.tatumgames.stattool.volley.listeners.GsonObjectListener

import java.util.ArrayList

class ApiFragment : BaseFragment() {

    // mission max difficulty iters
    private val MAX_MISSION_DIFFICULTY_TIERS = 3

    private var mContext: Context? = null
    private var mActivity: Activity? = null
    private var mRootView: View? = null

    private var mErrorUtils: ErrorUtils? = null
    private var mStats: GuardianStatsModel? = null
    private var mStatUtils: StatUtils? = null

    private var alUpdateGuardianBaseStatsBulkRQ: ArrayList<UpdateGuardianBaseStatsBulkRQ>? = null
    private var alUpdateGuardianRQ: ArrayList<UpdateGuardianRQ>? = null
    private var alUpdateMissionRQ: ArrayList<UpdateMissionRQ>? = null
    private var mUpdateGuardianCounter: Int = 0
    private var mUpdateMissionCounter: Int = 0
    private var mApiStackPos: Int = 0

    // adapter
    private var mApiAdapter: ApiAdapter? = null
    private var rvApi: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_apis, container, false)

        // instantiate views
        initializeViews()
        initializeListeners()
        return mRootView
    }

    /**
     * Method is used to instantiate views
     */
    private fun initializeViews() {
        // instantiate context and activity instance
        mContext = activity
        mActivity = activity
        mErrorUtils = ErrorUtils()
        mStats = GuardianStatsModel()
        mStatUtils = StatUtils()
        alUpdateGuardianBaseStatsBulkRQ = ArrayList()
        alUpdateGuardianRQ = ArrayList()
        alUpdateMissionRQ = ArrayList()

        // initialize adapter
        rvApi = mRootView!!.findViewById(R.id.rv_apis)
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rvApi!!.layoutManager = layoutManager
        mApiAdapter = ApiAdapter(mContext!!, resources.getStringArray(R.array.array_apis))
        rvApi!!.adapter = mApiAdapter

    }

    /**
     * Method is used to initialize listeners and callbacks
     */
    private fun initializeListeners() {
        ApiAdapter.onClickAdapterListener { pos ->
            // set api position
            mApiStackPos = pos

            // make getGuardians request
            getGuardians()
        }
    }

    /**
     * Method is used to make getGuardians request
     */
    private fun getGuardians() {
        // show progress dialog
        DialogUtils.showProgressDialog(mContext!!)

        val errorListener = object : ErrorListener {
            fun onErrorResponse(googleError: VolleyError, resultCode: Int) {
                // do nothing
            }

            override fun onErrorResponse(volleyError: VolleyError) {
                volleyError.printStackTrace()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // display error dialog
                mErrorUtils!!.showError(mActivity!!, resources.getString(R.string.default_error_message))
            }
        }
        val listener = GsonObjectListener(GsonObjectListener.OnResponse<GetGuardiansRS> { response ->
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
                    populateAffinityCardTypeList(response.data)
                } else if (mApiStackPos == 1) {
                    populateUpdateGuardianStatsList(response.data)
                } else if (mApiStackPos == 2) {
                    populateMissionDifficultyList()
                } else if (mApiStackPos == 3) {

                } else if (mApiStackPos == 4) {

                } else if (mApiStackPos == 5) {

                }


            } else {
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
            }
        }, errorListener, GetGuardiansRS::class.java)
        (mActivity as BaseActivity).mRequestManager.createGetRequest(GetGuardiansRQ(), listener, errorListener,
                UrlConstants.HV_GET_GUARDIANS_URL_CREATE)
    }

    /**
     * Method is used to populate list of requests for updating affinity and card types for Guardian stats
     *
     * @param data List of Guardian data for UpdateGuardian requests
     */
    private fun populateAffinityCardTypeList(data: ArrayList<GuardianDetails>) {
        // clear list
        alUpdateGuardianRQ!!.clear()
        for (i in data.indices) {
            if (!Utils.isStringEmpty(data[i].id)) {
                // add formed request for updateGuardian
                val updateGuardianRQ = UpdateGuardianRQ()
                updateGuardianRQ.data = data[i]

                // update affinityTypeId
                updateGuardianRQ.data.affinityTypeId = MappingModel.getAffinityIdByGuardianId(data[i].id!!)
                // update affinityTypeName
                updateGuardianRQ.data.affinityTypeName = MappingModel.getAffinityNameByGuardianId(data[i].id!!)
                // update cardTypeId
                updateGuardianRQ.data.cardTypeId = MappingModel.getCardTypeIdByGuardianId(data[i].id)
                // update cardTypeName
                updateGuardianRQ.data.cardTypeName = MappingModel.getCardTypeNameByGuardianId(data[i].id)
                // update description
                updateGuardianRQ.data.description = MappingModel.getDescriptionByGuardianId(mContext, data[i].id!!)
                // update short description
                updateGuardianRQ.data.shortDescription = if (!Utils.isStringEmpty(data[i].shortDescription)) data[i].shortDescription else ""
                // update code
                updateGuardianRQ.data.code = if (!Utils.isStringEmpty(data[i].code)) data[i].code else ""
                // update asset bundle
                updateGuardianRQ.data.assetBundle = if (!Utils.isStringEmpty(data[i].assetBundle)) data[i].assetBundle else ""
                alUpdateGuardianRQ!!.add(updateGuardianRQ)
            }
        }

        // print formed request for updateGuardian
        printUpdateGuardianRQ()

        // make updateGuardian request
        updateGuardian()
    }

    /**
     * Method is used to populate list of requests for updating Guardian stats
     *
     * @param data List of Guardian data for updateGuardianStats(bulk) requests
     */
    private fun populateUpdateGuardianStatsList(data: ArrayList<GuardianDetails>) {
        for (i in data.indices) {
            if (!Utils.isStringEmpty(data[i].cardTypeId) && !Utils.isStringEmpty(data[i].affinityTypeId)) {
                val alUpdateBaseStats = ArrayList<UpdateBaseStats>()
                // generate stats
                // cardType and affinity are ids from getGuardianRS
                //                GuardianStatsModel guardianStats = mStatUtils.getStats(
                //                        MappingModel.getCardTypeByCardTypeId(Integer.parseInt(data.get(i).cardTypeId)),
                //                        MappingModel.getAffinityByAffinityId(Integer.parseInt(data.get(i).affinityTypeId)),
                //                        0);
                val guardianStats = mStatUtils!!.getStats(
                        if (i < 6) Enum.CardType.SQUAD_LEADER else Enum.CardType.COMMON,
                        MappingModel.getAffinityByAffinityId(Integer.parseInt(data[i].affinityTypeId)),
                        0)

                for (n in data[i].stats.indices) {
                    if (!Utils.isStringEmpty(data[i].stats[n].id) && !Utils.isStringEmpty(data[i].stats[n].statTypeId)) {
                        // update stats with generated stats
                        val updateBaseStats = UpdateBaseStats()
                        updateBaseStats.id = Integer.parseInt(data[i].stats[n].id)
                        updateBaseStats.statId = Integer.parseInt(data[i].stats[n].statTypeId)
                        if (data[i].stats[n].code!!.equals(Enum.Stats.HEALTH.toString(), ignoreCase = true)) {
                            // represents HP
                            updateBaseStats.statValue = guardianStats.hp
                        } else if (data[i].stats[n].code!!.equals(Enum.Stats.SPEED.toString(), ignoreCase = true)) {
                            // represents Speed
                            updateBaseStats.statValue = guardianStats.spd
                        } else if (data[i].stats[n].code!!.equals(Enum.Stats.STRENGTH.toString(), ignoreCase = true)) {
                            // represents Strength
                            updateBaseStats.statValue = guardianStats.str
                        } else if (data[i].stats[n].code!!.equals(Enum.Stats.WISDOM.toString(), ignoreCase = true)) {
                            // represents Wisdom
                            updateBaseStats.statValue = guardianStats.spd
                        } else if (data[i].stats[n].code!!.equals(Enum.Stats.PHYSICAL_RESISTANCE.toString(), ignoreCase = true)) {
                            // represents Physical Resistance
                            updateBaseStats.statValue = guardianStats.phyDef
                        } else if (data[i].stats[n].code!!.equals(Enum.Stats.MAGICAL_RESISTANCE.toString(), ignoreCase = true)) {
                            // represents Magical Resistance
                            updateBaseStats.statValue = guardianStats.magDef
                        } else if (data[i].stats[n].code!!.equals(Enum.Stats.CRITICAL_PERCENT.toString(), ignoreCase = true)) {
                            // represents Critical Percent
                            updateBaseStats.statValue = guardianStats.crit
                        }
                        // 0 is false, 1 is true
                        updateBaseStats.active = 1
                        updateBaseStats.archived = 0
                        updateBaseStats.deleted = 0
                        // add updated stat
                        alUpdateBaseStats.add(updateBaseStats)

                        if (n == data[i].stats.size - 1) {
                            // add formed request for updateBaseStats(bulk)
                            val updateGuardianBaseStatsBulkRQ = UpdateGuardianBaseStatsBulkRQ()
                            updateGuardianBaseStatsBulkRQ.guardianId = Integer.parseInt(data[i].id)
                            updateGuardianBaseStatsBulkRQ.stats = alUpdateBaseStats
                            alUpdateGuardianBaseStatsBulkRQ!!.add(updateGuardianBaseStatsBulkRQ)
                        }
                    }
                }
            }
        }

        // print formed request for updateGuardianBaseStats
        printUpdateGuardianBaseStatsRQ()

        // make updateGuardianStatsBulk(bulk) request
        updateGuardianStatsBulk()
    }

    /**
     * Method is used to make updateGuardian requests
     *
     * Although name, description and other attributes can be updated. This method is currently
     * being used to update affinity and cardType only. The other attributes have already been
     * updated
     */
    private fun updateGuardian() {
        if (mUpdateGuardianCounter == 0) {
            // show progress dialog
            DialogUtils.showProgressDialog(mContext!!)
        }

        val errorListener = object : ErrorListener {
            fun onErrorResponse(googleError: VolleyError, resultCode: Int) {
                // do nothing
            }

            override fun onErrorResponse(volleyError: VolleyError) {
                volleyError.printStackTrace()
                // reset
                mUpdateGuardianCounter = 0
                alUpdateGuardianRQ!!.clear()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // display error dialog
                mErrorUtils!!.showError(mActivity!!, resources.getString(R.string.default_error_message))
            }
        }
        val listener = GsonObjectListener(GsonObjectListener.OnResponse<UpdateGuardianRS> {
            // increase counter
            mUpdateGuardianCounter++

            if (mUpdateGuardianCounter < alUpdateGuardianRQ!!.size) {
                // recursively call updateGuardian
                updateGuardian()
            } else {
                // reset
                mUpdateGuardianCounter = 0
                alUpdateGuardianRQ!!.clear()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // successful TCoin purchase
                DialogUtils.showDefaultOKAlert(mActivity!!, resources.getString(R.string.successful_update_guardian_title),
                        resources.getString(R.string.successful_update_guardian_message))
            }
        }, errorListener, UpdateGuardianRS::class.java)
        (mActivity as BaseActivity).mRequestManager.createPostRequest(alUpdateGuardianRQ!![mUpdateGuardianCounter],
                listener, errorListener, UrlConstants.HV_UPDATE_GUARDIAN_URL_CREATE)
    }

    /**
     * Method is used to make updateGuardianStatsBulk(bulk) requests
     */
    private fun updateGuardianStatsBulk() {
        if (mUpdateGuardianCounter == 0) {
            // show progress dialog
            DialogUtils.showProgressDialog(mContext!!)
        }

        val errorListener = object : ErrorListener {
            fun onErrorResponse(googleError: VolleyError, resultCode: Int) {
                // do nothing
            }

            override fun onErrorResponse(volleyError: VolleyError) {
                volleyError.printStackTrace()
                // reset
                mUpdateGuardianCounter = 0
                alUpdateGuardianBaseStatsBulkRQ!!.clear()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // display error dialog
                mErrorUtils!!.showError(mActivity!!, resources.getString(R.string.default_error_message))
            }
        }
        val listener = GsonObjectListener(GsonObjectListener.OnResponse<UpdateGuardianBaseStatsBulkRS> {
            // increase counter
            mUpdateGuardianCounter++

            if (mUpdateGuardianCounter < alUpdateGuardianBaseStatsBulkRQ!!.size) {
                // recursively call updateGuardianStatsBulk(bulk)
                updateGuardianStatsBulk()
            } else {
                // reset
                mUpdateGuardianCounter = 0
                alUpdateGuardianBaseStatsBulkRQ!!.clear()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // successful TCoin purchase
                DialogUtils.showDefaultOKAlert(mActivity!!, resources.getString(R.string.successful_update_stats_title),
                        resources.getString(R.string.successful_update_stats_message))
            }
        }, errorListener, UpdateGuardianBaseStatsBulkRS::class.java)
        (mActivity as BaseActivity).mRequestManager.createPostRequest(alUpdateGuardianBaseStatsBulkRQ!![mUpdateGuardianCounter],
                listener, errorListener, UrlConstants.HV_UPDATE_BASE_STATS_BULK_URL_CREATE)
    }

    /**
     * Method is used to populate list of requests for updating mission difficulty tiers
     */
    private fun populateMissionDifficultyList() {
        for (i in 0 until MAX_MISSION_DIFFICULTY_TIERS) {
            val updateMissionRQ = UpdateMissionRQ()
            updateMissionRQ.id = (i + 1).toString()
            updateMissionRQ.difficultyLevelId = (i + 1).toString()
            updateMissionRQ.code = ""

            if (i == 0) {
                updateMissionRQ.name = mContext!!.resources.getString(R.string.alpha)
                updateMissionRQ.description = mContext!!.resources.getString(R.string.alpha)
                updateMissionRQ.difficultyLevelName = mContext!!.resources.getString(R.string.alpha)
            } else if (i == 1) {
                updateMissionRQ.name = mContext!!.resources.getString(R.string.beta)
                updateMissionRQ.description = mContext!!.resources.getString(R.string.beta)
                updateMissionRQ.difficultyLevelName = mContext!!.resources.getString(R.string.beta)
            } else if (i == 2) {
                updateMissionRQ.name = mContext!!.resources.getString(R.string.omega)
                updateMissionRQ.description = mContext!!.resources.getString(R.string.omega)
                updateMissionRQ.difficultyLevelName = mContext!!.resources.getString(R.string.omega)
            }
            // 0 is false, 1 is true
            updateMissionRQ.active = "1"
            updateMissionRQ.archived = "0"
            updateMissionRQ.deleted = "0"
            alUpdateMissionRQ!!.add(updateMissionRQ)
        }

        // make update mission request
        updateMissionDifficulty()
    }

    /**
     * Method is used to make updateMission requests
     */
    private fun updateMissionDifficulty() {
        if (mUpdateMissionCounter == 0) {
            // show progress dialog
            DialogUtils.showProgressDialog(mContext!!)
        }

        val errorListener = object : ErrorListener {
            fun onErrorResponse(googleError: VolleyError, resultCode: Int) {
                // do nothing
            }

            override fun onErrorResponse(volleyError: VolleyError) {
                volleyError.printStackTrace()
                // reset
                mUpdateMissionCounter = 0
                alUpdateMissionRQ!!.clear()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // display error dialog
                mErrorUtils!!.showError(mActivity!!, resources.getString(R.string.default_error_message))
            }
        }
        val listener = GsonObjectListener(GsonObjectListener.OnResponse<UpdateMissionRS> {
            // increase counter
            mUpdateMissionCounter++

            if (mUpdateMissionCounter < alUpdateMissionRQ!!.size) {
                // recursively call updateMissionDifficulty
                updateMissionDifficulty()
            } else {
                // reset
                mUpdateMissionCounter = 0
                alUpdateMissionRQ!!.clear()
                // dismiss loading dialog
                DialogUtils.dismissProgressDialog()
                // successful TCoin purchase
                DialogUtils.showDefaultOKAlert(mActivity!!, resources.getString(R.string.successful_update_mission_title),
                        resources.getString(R.string.successful_update_mission_message))
            }
        }, errorListener, UpdateMissionRS::class.java)
        (mActivity as BaseActivity).mRequestManager.createPostRequest(alUpdateMissionRQ!![mUpdateMissionCounter],
                listener, errorListener, UrlConstants.HV_UPDATE_MISSION_URL_CREATE)
    }

    /**
     * Method is used to print out all the update Guardian requests for affinity, card types and descriptions
     */
    private fun printUpdateGuardianRQ() {
        for (i in alUpdateGuardianRQ!!.indices) {
            Logger.i(TAG, "---------------------------------------------")
            // print GuardianId
            Logger.i(TAG, "guardianId: " + alUpdateGuardianRQ!![i].data.id!!)
            Logger.i(TAG, "name: " + alUpdateGuardianRQ!![i].data.name!!)
            Logger.i(TAG, "alias: " + alUpdateGuardianRQ!![i].data.alias!!)
            Logger.i(TAG, "affinityId: " + alUpdateGuardianRQ!![i].data.affinityTypeId!!)
            Logger.i(TAG, "affinityName: " + alUpdateGuardianRQ!![i].data.affinityTypeName!!)
            Logger.i(TAG, "cardTypeId: " + alUpdateGuardianRQ!![i].data.cardTypeId!!)
            Logger.i(TAG, "cardTypeName: " + alUpdateGuardianRQ!![i].data.cardTypeName!!)
            Logger.i(TAG, "description: " + alUpdateGuardianRQ!![i].data.description!!)
            Logger.i(TAG, "shortDescription: " + alUpdateGuardianRQ!![i].data.shortDescription!!)
        }
    }

    /**
     * Method is used to print out all the update Guardian base stat requests
     */
    private fun printUpdateGuardianBaseStatsRQ() {
        for (i in alUpdateGuardianBaseStatsBulkRQ!!.indices) {
            Logger.i(TAG, "---------------------------------------------")
            // print GuardianId
            Logger.i(TAG, "guardianId: " + alUpdateGuardianBaseStatsBulkRQ!![i].guardianId)
            for (n in alUpdateGuardianBaseStatsBulkRQ!![i].stats.indices) {
                // print Id (primary key)
                Logger.i(TAG, "id: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].id)
                if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 1) {
                    // print HP
                    Logger.i(TAG, "hp: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                } else if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 2) {
                    // print Speed
                    Logger.i(TAG, "spd: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                } else if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 3) {
                    // print Strength
                    Logger.i(TAG, "str: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                } else if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 4) {
                    // print Wisdom
                    Logger.i(TAG, "wisdom: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                } else if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 5) {
                    // print Physical Resistance
                    Logger.i(TAG, "phy def: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                } else if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 6) {
                    // print Magical Resistance
                    Logger.i(TAG, "mag def: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                } else if (alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statId == 7) {
                    // print Critical Percent
                    Logger.i(TAG, "crit%: " + alUpdateGuardianBaseStatsBulkRQ!![i].stats[n].statValue)
                }
            }
        }
    }

    companion object {
        val TAG = ApiFragment::class.java!!.getSimpleName()
    }

}
