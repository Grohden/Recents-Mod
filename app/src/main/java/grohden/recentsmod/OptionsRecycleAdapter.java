/*
 * Copyright 2015 Gabriel de Oliveira Rohden
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grohden.recentsmod;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class OptionsRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] mDataset;
    private int[] mColors;
    private static final int TYPE_HEADER=0;
    private static final int TYPE_COLOR_ITEM=1;
    private static final int TYPE_POSITION_ITEM=2;


    public static class BackgroundItemHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public FrameLayout mTile;

        public BackgroundItemHolder(View linearLayout) {
            super(linearLayout);
            mTextView = (TextView) linearLayout.findViewById(R.id.my_text_view);
            mTile = (FrameLayout) linearLayout.findViewById(R.id.test_tile);
        }
    }
    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public FrameLayout mAppThumbnail;
        public View mAppIcon;
        public TextView mAppName;

        public HeaderHolder(View view) {
            super(view);
            mAppName = (TextView) view.findViewById(R.id.app_name);
            mAppThumbnail = (FrameLayout) view.findViewById(R.id.app_thumbnail);
            mAppIcon = view.findViewById(R.id.app_icon);
        }
    }
    public static class PositionItemHolder extends RecyclerView.ViewHolder {
        public Spinner mGravitySpinner;
        public Spinner mPositionSpinner;
        public TextView mDescription;

        public PositionItemHolder(View view) {
            super(view);
            mDescription = (TextView) view.findViewById(R.id.position_description);
            mGravitySpinner = (Spinner) view.findViewById(R.id.gravity_spinner);
            mPositionSpinner= (Spinner) view.findViewById(R.id.position_spinner);
        }
    }

    public OptionsRecycleAdapter(String[] myDataset,int[] myColors) {
        mColors  = myColors;
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType==TYPE_COLOR_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bg_option_adapter, parent, false);
            BackgroundItemHolder vh = new BackgroundItemHolder(v);
            return vh;
        }
        else if(viewType==TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recents, parent, false);
            HeaderHolder headerHolder=new HeaderHolder(v);
            return headerHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_spinner_view, parent, false);
            PositionItemHolder vh=new PositionItemHolder(v);
            return  vh;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BackgroundItemHolder){
            setLayoutData((BackgroundItemHolder) holder, position);}
        else if(holder instanceof PositionItemHolder){
            setLayoutData((PositionItemHolder)holder,position);
     }

    }

    private void setLayoutData(PositionItemHolder v,int position){
        v.mDescription.setText(mDataset[position-1]); //set description text

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.mDescription.getContext(),R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        v.mPositionSpinner.setAdapter(adapter);
        v.mGravitySpinner.setAdapter(adapter);
     }
    private void setLayoutData(BackgroundItemHolder v,int position){
        v.mTextView.setText(mDataset[position-1]);
        v.mTile.setBackgroundColor(mColors[position-1]);
        }
    private void setLayoutData(HeaderHolder v, int position){
        }

    @Override
    public int getItemCount() {
        return mDataset.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        int type=3;
        switch (position){
            case TYPE_HEADER:
                type=TYPE_HEADER;
                break;
            case TYPE_COLOR_ITEM:
                type=TYPE_COLOR_ITEM;
                break;
            case TYPE_POSITION_ITEM:
                type=TYPE_POSITION_ITEM;
                break;
        }
        return type;
    }
}
