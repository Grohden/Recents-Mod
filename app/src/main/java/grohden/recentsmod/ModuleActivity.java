package grohden.recentsmod;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ModuleActivity extends ActionBarActivity {
    public static boolean DEBUG=true;

    @InjectView(R.id.too_lazy) TextView tooLazy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ButterKnife.inject(this);
        tooLazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an animation instance
                Animation an = new RotateAnimation(0.0f, 360.0f,v.getWidth()/2,v.getHeight()/2);

                // Set the animation's parameters
                an.setDuration(300);               // duration in ms
                an.setRepeatCount(0);                // -1 = infinite repeated
                an.setRepeatMode(Animation.REVERSE); // reverses each repeat
                an.setFillAfter(true);               // keep rotation after animation

                // Aply animation to image view
                v.startAnimation(an);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_module, menu);
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
