package com.lcsvcn.imgurcat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridActivity extends Activity implements
        AdapterView.OnItemClickListener {

    private ImageView Selection;
    private static final Integer[] items = {
            R.drawable.ic_launcher_background
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Selection = (ImageView) findViewById(R.id.selection);
        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new ArrayAdapter<Integer>(this, R.layout.cell, items));

        grid.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.grid, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        Selection.setImageResource(items[arg2]);
    }

}