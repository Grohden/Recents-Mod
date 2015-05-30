package grohden.recentsmod;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Gabriel on 30/05/2015.
 */
public class OptionsAdapter extends BaseAdapter {
    private String[] mOptions;
    private Context mContext;

    public OptionsAdapter(Context context, String[] options){
        mContext=context;
        mOptions=options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;

        if(convertView == null){

            if (position==0){
                listView = inflater.inflate(R.layout.bg_option_adapter, null);
                ((TextView) listView.findViewById(R.id.description_text)).setText(mOptions[position]);
                ((FrameLayout) listView.findViewById(R.id.tile)).setBackgroundColor(Color.parseColor("#1ba1e2"));
            }
            else {
                // get layout from tile_adapter.xml
                listView = inflater.inflate(R.layout.option_adapter, null);

                // set views colors
                ((TextView) listView.findViewById(R.id.description_text)).setText(mOptions[position]);
            }
        }
        else {
            listView = convertView;
        }

        return listView;
    }

    @Override
    public int getCount() {
        return mOptions.length ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
