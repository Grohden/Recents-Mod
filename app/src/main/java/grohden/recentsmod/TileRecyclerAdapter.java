package grohden.recentsmod;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Avell on 21/04/2016.
 */
public class TileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int colors[];
    private View.OnClickListener listener=null;

    public TileRecyclerAdapter(int colors[]){
        this.colors=colors;
    }

    public static class ColorHolder extends RecyclerView.ViewHolder{
        public View colorTile;
        public View parent;

        public ColorHolder(View itemView) {
            super(itemView);
            this.colorTile = itemView.findViewById(R.id.color_tile);
            this.parent = itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tile_adapter, parent, false);
        return new ColorHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ColorHolder) holder).colorTile.setBackgroundColor(colors[position]);
        ((ColorHolder) holder).colorTile.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public void setTileClickListener(View.OnClickListener listener){
            this.listener=listener;
    }

}
