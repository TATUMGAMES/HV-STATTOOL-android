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

package com.tatumgames.stattool.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

import com.tatumgames.stattool.R
import com.tatumgames.stattool.listener.OnClickAdapterListener
import com.tatumgames.stattool.utils.Utils

class ApiAdapter
/**
 * Constructor
 *
 * @param context      Interface to global information about an application environment
 * @param arryApiCalls List of APIs
 */
(private val mContext: Context, private val arryApiCalls: Array<String>) : RecyclerView.Adapter<ApiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.item_apis, parent, false)
        return ApiAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = holder.adapterPosition

        // set background
        holder.flParentWrapper.setBackgroundColor(getBackgroundColor(position))
        // set text
        holder.tvApis.text = arryApiCalls[position]

        // click listener
        holder.flParentWrapper.setOnClickListener {
            if (!Utils.checkIfNull<OnClickAdapterListener>(mOnClickAdapterListener)) {
                // set listener
                mOnClickAdapterListener!!.onClick(index)
            }
        }
    }

    override fun getItemCount(): Int {
        return arryApiCalls.size
    }

    /**
     * Method is used to return back a background color
     *
     * @param position The position of the adapter item
     * @return Background  color
     */
    private fun getBackgroundColor(position: Int): Int {
        if (position == 0) {
            return mContext.resources.getColor(R.color.material_red_200_color_code)
        } else if (position == 1) {
            return mContext.resources.getColor(R.color.material_pink_200_color_code)
        } else if (position == 2) {
            return mContext.resources.getColor(R.color.material_purple_200_color_code)
        } else if (position == 3) {
            return mContext.resources.getColor(R.color.material_deep_purple_200_color_code)
        } else if (position == 4) {
            return mContext.resources.getColor(R.color.material_indigo_200_color_code)
        } else if (position == 5) {
            return mContext.resources.getColor(R.color.material_blue_200_color_code)
        } else if (position == 6) {
            return mContext.resources.getColor(R.color.material_light_blue_200_color_code)
        } else if (position == 7) {
            return mContext.resources.getColor(R.color.material_cyan_200_color_code)
        } else if (position == 8) {
            return mContext.resources.getColor(R.color.material_green_200_color_code)
        } else if (position == 9) {
            return mContext.resources.getColor(R.color.material_teal_200_color_code)
        }
        return mContext.resources.getColor(R.color.material_lime_200_color_code)
    }

    /**
     * View holder class
     *
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val flParentWrapper: FrameLayout
        private val tvApis: TextView

        init {

            flParentWrapper = itemView.findViewById(R.id.fl_parent_wrapper)
            tvApis = itemView.findViewById(R.id.tv_apis)
        }
    }

    companion object {

        // custom callback
        private var mOnClickAdapterListener: OnClickAdapterListener? = null

        /**
         * Method is used to set callback for when item is clicked
         *
         * @param listener Callback for when item is clicked
         */
        fun onClickAdapterListener(listener: OnClickAdapterListener) {
            mOnClickAdapterListener = listener
        }
    }
}
