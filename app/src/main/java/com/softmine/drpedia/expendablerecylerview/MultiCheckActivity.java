package com.softmine.drpedia.expendablerecylerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.model.CategoryMainItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiCheckActivity extends AppCompatActivity {

    private MultiCheckGenreAdapter adapter;
    List<CategoryMainItem> multiCheckGenres;
    HashMap<Integer,List<Integer>> map ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        multiCheckGenres = new ArrayList<>();
        map = new HashMap<>(multiCheckGenres.size());
        adapter = new MultiCheckGenreAdapter(multiCheckGenres);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Button check = (Button) findViewById(R.id.check_first_child);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPos();
            }
        });
    }
    public void printPos()
    {
        List<Integer> initialCheckedPositions = adapter.getChildCheckController().getCheckedPositions();
        map = adapter.getChildCheckController().getMap();
      /*  for(Integer list : initialCheckedPositions)
        {
            Log.d("checkedPos","pos  "+list);
        }*/

        Log.d("checkedPos","traversing Map");

        for(Map.Entry entry  : map.entrySet())
        {
            Log.d("checkedPos","group index  "+entry.getKey());
            List<String> list = (List<String>) entry.getValue();
            for(String val :list)
            {
                Log.d("checkedPos","child index  "+val);
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }
}
