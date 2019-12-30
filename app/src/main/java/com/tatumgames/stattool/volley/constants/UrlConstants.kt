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

package com.tatumgames.stattool.volley.constants

object UrlConstants {

    // base constants used for building request URLs
    private val TG_API = "https://hv-api-new.appspot.com/"

    // constructed requests
    // guardians
    private val HV_GET_GUARDIANS = "guardians/base/getGuardians"
    val HV_GET_GUARDIANS_URL_CREATE = TG_API + HV_GET_GUARDIANS
    private val HV_UPDATE_BASE_STATS_BULK = "guardians/base/updateBaseStats/bulk"
    val HV_UPDATE_BASE_STATS_BULK_URL_CREATE = TG_API + HV_UPDATE_BASE_STATS_BULK
    private val HV_UPDATE_GUARDIAN = "guardians/base/updateGuardian"
    val HV_UPDATE_GUARDIAN_URL_CREATE = TG_API + HV_UPDATE_GUARDIAN
    // missions
    private val HV_UPDATE_MISSION = "missions/base/updateMission"
    val HV_UPDATE_MISSION_URL_CREATE = TG_API + HV_UPDATE_MISSION
}
