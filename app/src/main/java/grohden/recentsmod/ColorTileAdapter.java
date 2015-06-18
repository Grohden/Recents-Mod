package grohden.recentsmod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Gabriel on 28/05/2015.
 */
public class ColorTileAdapter extends BaseAdapter {
    private Context mContext;
    private final int[] mColors;
    private final String TAG= this.getClass().getName();

    public ColorTileAdapter(Context context, int[] colors){
        mContext=context;
        mColors=colors;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;


        if(convertView == null){
            gridView=new View(mContext);

            // get layout from tile_adapter_adapter.xml
            gridView = inflater.inflate(R.layout.tile_adapter,null);

            // set views colors
            FrameLayout colorTile =(FrameLayout) gridView.findViewById(R.id.color_tile);
            colorTile.setBackgroundColor(mColors[position]);
            colorTile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GridView) parent).performItemClick(v, position, 0);
                }
            });
        }
        else {
            gridView = convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mColors.length;
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
