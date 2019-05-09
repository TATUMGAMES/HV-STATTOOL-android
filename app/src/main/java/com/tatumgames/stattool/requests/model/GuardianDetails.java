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

public class GuardianDetails {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("alias")
    public String alias;

    @SerializedName("description")
    public String description;

    @SerializedName("shortDescription")
    public String shortDescription;

    @SerializedName("code")
    public String code;

    @SerializedName("characterTypeId")
    public String characterTypeId;

    @SerializedName("characterTypeName")
    public String characterTypeName;

    @SerializedName("cardId")
    public String cardId;

    @SerializedName("cardName")
    public String cardName;

    @SerializedName("cardTypeId")
    public String cardTypeId;

    @SerializedName("cardTypeName")
    public String cardTypeName;

    @SerializedName("affinityTypeId")
    public String affinityTypeId;

    @SerializedName("affinityTypeName")
    public String affinityTypeName;

    @SerializedName("assetBundle")
    public String assetBundle;

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

    @SerializedName("attacks")
    public ArrayList<Attacks> attacks = new ArrayList<>();

    @SerializedName("images")
    public ArrayList<Images> images = new ArrayList<>();

    @SerializedName("skins")
    public ArrayList<Skins> skins = new ArrayList<>();

    @SerializedName("stats")
    public ArrayList<Stats> stats = new ArrayList<>();

}
