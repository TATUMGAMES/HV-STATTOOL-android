package com.tatumgames.stattool.volley.constants;

public class UrlConstants {

    // base constants used for building request URLs
    private static final String TG_API = "https://hv-api-new.appspot.com/";

    // constructed requests
    private static final String HV_GET_GUARDIANS = "guardians/getGuardians";
    public static final String HV_GET_GUARDIANS_URL_CREATE = TG_API.concat(HV_GET_GUARDIANS);
    private static final String HV_UPDATE_BASE_STATS_BULK = "guardians/updateBaseStats/bulk";
    public static final String HV_UPDATE_BASE_STATS_BULK_URL_CREATE = TG_API.concat(HV_UPDATE_BASE_STATS_BULK);
}
