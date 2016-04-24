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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class OptionsRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] mDataset;
    private int[] mColors;
    private OnItemClickListener optionListener = null;
    private OnSpinnerListener spinnerListener = null;
    public static final int TYPE_HEADER=0;
    public static final int TYPE_COLOR_ITEM=1;
    public static final int TYPE_POSITION_ITEM=2;

    public OptionsRecycleAdapter(String[] myDataset,int[] myColors) {
        mColors  = myColors;
        mDataset = myDataset;
    }

    public static class BackgroundItemHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public View mTile;
        public View parent;

        public BackgroundItemHolder(View linearLayout) {
            super(linearLayout);
            this.mTextView = (TextView) linearLayout.findViewById(R.id.my_text_view);
            this.mTile = linearLayout.findViewById(R.id.color_tile);
            this.parent = linearLayout;
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
        public Spinner xSpinner;
        public Spinner ySpinner;
        public TextView mDescription;

        public PositionItemHolder(View view) {
            super(view);
            mDescription = (TextView) view.findViewById(R.id.position_description);
            xSpinner = (Spinner) view.findViewById(R.id.x_axys_spinner);
            ySpinner = (Spinner) view.findViewById(R.id.y_axys_spinner);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,int type);
    }

    public interface OnSpinnerListener {
        void onItemSelected(AdapterView<?> parent,View view,int pos, long id);

        void onNothingSelected(AdapterView<?> parent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh=null;
        View v=null;
        switch (viewType){
            case TYPE_COLOR_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_background_option, parent, false);
                vh = new BackgroundItemHolder(v);
                break;
            case TYPE_HEADER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recents_demo, parent, false);
                vh=new HeaderHolder(v);
                break;
            case TYPE_POSITION_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_two_spiner, parent, false);
                vh=new PositionItemHolder(v);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BackgroundItemHolder){
            setLayoutData((BackgroundItemHolder) holder, position);
        }
        else if(holder instanceof PositionItemHolder){
            setLayoutData((PositionItemHolder) holder,position);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position; //TODO:Discover why i did this.
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.optionListener =listener;
    }

    public void setSpinnerListener(OnSpinnerListener listener){
        this.spinnerListener = listener;
    }

    private void setLayoutData(PositionItemHolder v,int position){
        v.mDescription.setText(mDataset[position-1]); //set description text


        ArrayAdapter<CharSequence> xAdapter = ArrayAdapter.createFromResource(v.mDescription.getContext(),R.array.x_axys_options, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> yAdapter = ArrayAdapter.createFromResource(v.mDescription.getContext(),R.array.y_axys_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        xAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        v.ySpinner.setAdapter(yAdapter);
        v.xSpinner.setAdapter(xAdapter);

        AdapterView.OnItemSelectedListener clickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerListener.onItemSelected(parent,view,position,id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerListener.onNothingSelected(parent);
            }
        };

        v.xSpinner.setOnItemSelectedListener(clickListener);
        v.ySpinner.setOnItemSelectedListener(clickListener);
     }

    private void setLayoutData(BackgroundItemHolder v, final int position){
        v.mTextView.setText(mDataset[position-1]);
        v.mTile.setBackgroundColor(mColors[position-1]);
        v.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionListener.onItemClick(v,position,TYPE_COLOR_ITEM);
            }
        });
    }

    private void setLayoutData(HeaderHolder v, int position){

    }

}
