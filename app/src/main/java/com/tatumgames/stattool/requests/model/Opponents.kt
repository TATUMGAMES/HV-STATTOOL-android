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

package com.tatumgames.stattool.requests.model

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Opponents {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("alias")
    var alias: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("shortDescription")
    var shortDescription: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("characterTypeId")
    var characterTypeId: String? = null

    @SerializedName("characterTypeName")
    var characterTypeName: String? = null

    @SerializedName("cardId")
    var cardId: String? = null

    @SerializedName("cardName")
    var cardName: String? = null

    @SerializedName("cardTypeId")
    var cardTypeId: String? = null

    @SerializedName("cardTypeName")
    var cardTypeName: String? = null

    @SerializedName("affinityTypeId")
    var affinityTypeId: String? = null

    @SerializedName("affinityTypeName")
    var affinityTypeName: String? = null

    @SerializedName("assetBundle")
    var assetBundle: String? = null

    @SerializedName("created")
    var created: String? = null

    @SerializedName("updated")
    var updated: String? = null

    @SerializedName("active")
    var active: String? = null

    @SerializedName("archived")
    var archived: String? = null

    @SerializedName("deleted")
    var deleted: String? = null

    @SerializedName("attacks")
    var attacks = ArrayList<Attacks>()

    @SerializedName("images")
    var images = ArrayList<Images>()

    @SerializedName("skins")
    var skins = ArrayList<Skins>()

    @SerializedName("stats")
    var stats = ArrayList<Stats>()

}
