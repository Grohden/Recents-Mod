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

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ColorPickerActivity extends ActionBarActivity implements View.OnClickListener {

    @InjectView(R.id.colors_holder) RecyclerView colorGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        ButterKnife.inject(this);

        final int[] colors=getApplicationContext().getResources().getIntArray(R.array.views_colors);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),4);
        TileRecyclerAdapter tileRecyclerAdapter=new TileRecyclerAdapter(colors);

        colorGrid.setLayoutManager(gridLayoutManager);
        colorGrid.setAdapter(tileRecyclerAdapter);

        tileRecyclerAdapter.setTileClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int color = Color.TRANSPARENT;
        Drawable background = v.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        Intent data = new Intent();
        data.putExtra("COLOR",color);
        setResult(RESULT_OK, data);
        finish();
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
