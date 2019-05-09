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

package com.tatumgames.stattool.volley.constants;

public class UrlConstants {

    // base constants used for building request URLs
    private static final String TG_API = "https://hv-api-new.appspot.com/";

    // constructed requests
    // guardians
    private static final String HV_GET_GUARDIANS = "guardians/base/getGuardians";
    public static final String HV_GET_GUARDIANS_URL_CREATE = TG_API.concat(HV_GET_GUARDIANS);
    private static final String HV_UPDATE_BASE_STATS_BULK = "guardians/base/updateBaseStats/bulk";
    public static final String HV_UPDATE_BASE_STATS_BULK_URL_CREATE = TG_API.concat(HV_UPDATE_BASE_STATS_BULK);
    private static final String HV_UPDATE_GUARDIAN = "guardians/base/updateGuardian";
    public static final String HV_UPDATE_GUARDIAN_URL_CREATE = TG_API.concat(HV_UPDATE_GUARDIAN);
    // missions
    private static final String HV_UPDATE_MISSION = "missions/base/updateMission";
    public static final String HV_UPDATE_MISSION_URL_CREATE = TG_API.concat(HV_UPDATE_MISSION);
}
