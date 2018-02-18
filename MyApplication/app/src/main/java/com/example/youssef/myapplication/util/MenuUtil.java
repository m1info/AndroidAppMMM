package com.example.youssef.myapplication.util;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.youssef.myapplication.MainActivity;
import com.example.youssef.myapplication.R;
import com.example.youssef.myapplication.event.list.ListEvent;

/**
 * Created by youssef on 31/12/17.
 */

public class MenuUtil implements SearchView.OnQueryTextListener {
    private SearchView filterView;
    private MainActivity activity;
    private Toolbar toolbar;
    private ListEvent listEvent;

    public MenuUtil(MainActivity activity, ListEvent listEvent) {
        this.listEvent =listEvent;
        this.activity = activity;
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
    }

    public void setFilterView(Menu menu){
        MenuItem mFilter = menu.findItem(R.id.action_search);
        filterView = (SearchView) mFilter.getActionView();
        setupFilterView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listEvent.filter = query;
        activity.replaceFragment(activity.getRecyclerFragment(),R.id.flContainer);
        listEvent.getLoaderManager().restartLoader(0,null,listEvent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    private void setupFilterView() {
        filterView.setIconifiedByDefault(false);
        filterView.setOnQueryTextListener(this);
        filterView.setSubmitButtonEnabled(true);
        filterView.setQueryHint("Recherche dans titre ...");
    }


}