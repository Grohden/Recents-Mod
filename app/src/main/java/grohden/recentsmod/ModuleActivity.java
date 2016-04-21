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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import butterknife.ButterKnife;
import butterknife.InjectView;


// TODO: find a substitute for 'shared view' , official needs at last api 21.


public class ModuleActivity extends ActionBarActivity implements View.OnClickListener,SpringListener{
    public static boolean DEBUG=true;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spring spring;
    private boolean zoomed=true;
    private static double TENSION = 800;
    private static double DAMPER = 20; //friction

    @InjectView(R.id.rebound_test_button) Button mReboundTest;
    @InjectView(R.id.options_recycle) RecyclerView optionsRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_module);
        ButterKnife.inject(this);
        String[] options=getApplicationContext().getResources().getStringArray(R.array.options_names);
        int[] colors=getApplicationContext().getResources().getIntArray(R.array.views_colors);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        optionsRecycle.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        optionsRecycle.setLayoutManager(mLayoutManager);

        mAdapter = new OptionsRecycleAdapter(options,colors);
        optionsRecycle.setAdapter(mAdapter);
        mReboundTest.setOnClickListener(this);

        // Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

        // Add a spring to the system.
        spring = springSystem.createSpring();

        // Add a listener to observe the motion of the spring.
        spring.addListener(this);


        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        spring.setSpringConfig(config);


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


    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = value;
        mReboundTest.setScaleX(scale);
        mReboundTest.setScaleY(scale);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    //this is just a test.
    @Override
    public void onClick(View v) {
        if(zoomed) {
            spring.setEndValue(2f);
            optionsRecycle.animate().alpha(0f);
            optionsRecycle.setVisibility(View.GONE);
            mReboundTest.animate().y(+100);
        } else {
            spring.setEndValue(1f);
            optionsRecycle.animate().alpha(1f);
            optionsRecycle.setVisibility(View.VISIBLE);
            mReboundTest.animate().y(0);
        }
        zoomed=!zoomed;

    }
}
