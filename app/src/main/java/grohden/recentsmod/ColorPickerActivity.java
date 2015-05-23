package grohden.recentsmod;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ColorPickerActivity extends ActionBarActivity {

    @InjectView(R.id.colors_holder) GridLayout colorGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        ButterKnife.inject(this);
        int[] colors=getApplicationContext().getResources().getIntArray(R.array.views_colors);
        for (int i = 0; i<colors.length; i++) {
            View squad=new View(this);
            int size= Utils.getInDIP(50,getResources());
            RelativeLayout.LayoutParams flp=new RelativeLayout.LayoutParams(size,size);
            int margin=Utils.getInDIP(100, getResources());
            flp.setMargins(margin, margin, margin, margin);
            squad.setLayoutParams(flp);
            colorGrid.addView(squad,i);
            colorGrid.getChildAt(i).setBackgroundColor(colors[i]);
            squad.requestLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
