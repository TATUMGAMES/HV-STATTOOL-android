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

package com.tatumgames.stattool.requests.app

import com.google.gson.annotations.SerializedName
import com.tatumgames.stattool.volley.BaseEntityRQ

class UpdateMissionRQ : BaseEntityRQ() {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("missionDifficultyLevelId")
    var difficultyLevelId: String? = null

    @SerializedName("missionDifficultyLevelName")
    var difficultyLevelName: String? = null

    @SerializedName("active")
    var active: String? = null

    @SerializedName("archived")
    var archived: String? = null

    @SerializedName("deleted")
    var deleted: String? = null
}