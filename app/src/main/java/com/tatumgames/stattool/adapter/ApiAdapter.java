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

package com.tatumgames.stattool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tatumgames.stattool.R;
import com.tatumgames.stattool.listener.OnClickAdapterListener;
import com.tatumgames.stattool.utils.Utils;

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.ViewHolder> {

    // custom callback
    private static OnClickAdapterListener mOnClickAdapterListener;

    private Context mContext;
    private String[] arryApiCalls;

    /**
     * Method is used to set callback for when item is clicked
     *
     * @param listener Callback for when item is clicked
     */
    public static void onClickAdapterListener(OnClickAdapterListener listener) {
        mOnClickAdapterListener = listener;
    }

    /**
     * Constructor
     *
     * @param context      Interface to global information about an application environment
     * @param arryApiCalls List of APIs
     */
    public ApiAdapter(@NonNull Context context, @NonNull String[] arryApiCalls) {
        mContext = context;
        this.arryApiCalls = arryApiCalls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_apis, parent, false);
        return new ApiAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = holder.getAdapterPosition();

        // set background
        holder.flParentWrapper.setBackgroundColor(getBackgroundColor(position));
        // set text
        holder.tvApis.setText(arryApiCalls[position]);

        // click listener
        holder.flParentWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.checkIfNull(mOnClickAdapterListener)) {
                    // set listener
                    mOnClickAdapterListener.onClick(index);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arryApiCalls.length;
    }

    /**
     * Method is used to return back a background color
     *
     * @param position The position of the adapter item
     * @return Background  color
     */
    private int getBackgroundColor(int position) {
        if (position == 0) {
            return mContext.getResources().getColor(R.color.material_red_200_color_code);
        } else if (position == 1) {
            return mContext.getResources().getColor(R.color.material_pink_200_color_code);
        } else if (position == 2) {
            return mContext.getResources().getColor(R.color.material_purple_200_color_code);
        } else if (position == 3) {
            return mContext.getResources().getColor(R.color.material_deep_purple_200_color_code);
        } else if (position == 4) {
            return mContext.getResources().getColor(R.color.material_indigo_200_color_code);
        } else if (position == 5) {
            return mContext.getResources().getColor(R.color.material_blue_200_color_code);
        } else if (position == 6) {
            return mContext.getResources().getColor(R.color.material_light_blue_200_color_code);
        } else if (position == 7) {
            return mContext.getResources().getColor(R.color.material_cyan_200_color_code);
        } else if (position == 8) {
            return mContext.getResources().getColor(R.color.material_green_200_color_code);
        } else if (position == 9) {
            return mContext.getResources().getColor(R.color.material_teal_200_color_code);
        }
        return mContext.getResources().getColor(R.color.material_lime_200_color_code);
    }

    /**
     * View holder class
     * <p>A ViewHolder describes an item view and metadata about its place within the RecyclerView</p>
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout flParentWrapper;
        private TextView tvApis;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            flParentWrapper = itemView.findViewById(R.id.fl_parent_wrapper);
            tvApis = itemView.findViewById(R.id.tv_apis);
        }
    }
}
