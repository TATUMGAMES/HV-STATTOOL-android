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

package com.tatumgames.stattool.requests.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Levels {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("code")
    public String code;

    @SerializedName("levelNo")
    public String levelNo;

    @SerializedName("areaId")
    public String areaId;

    @SerializedName("areaName")
    public String areaName;

    @SerializedName("rewardId")
    public String rewardId;

    @SerializedName("rewardName")
    public String rewardName;

    @SerializedName("rewardQty")
    public String rewardQty;

    @SerializedName("created")
    public String created;

    @SerializedName("updated")
    public String updated;

    @SerializedName("active")
    public String active;

    @SerializedName("archived")
    public String archived;

    @SerializedName("deleted")
    public String deleted;

    @SerializedName("opponents")
    public ArrayList<Opponents> opponents = new ArrayList<>();

}
