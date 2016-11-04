package com.electromatt.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = BookActivity.class.getName();
    private BookAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private String mSearchText;
    private String mSearchURL;
    private Button mButton;
    private EditText mEdit;
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        mButton = (Button)findViewById(R.id.button);
        mEdit   = (EditText)findViewById(R.id.search);

        mButton.setOnClickListener(
            new View.OnClickListener(){
                public void onClick(View view){
                    mSearchText = mEdit.getText().toString();
                    mSearchURL = getString(R.string.google_url)+mSearchText+getString(R.string.max_results);
                    Log.v("EditText", getString(R.string.google_url)+mSearchText+getString(R.string.max_results));
                    restartLoader();
                }
            }
        );

        ListView bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View loadingSpinner = findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);

        }

    }
    public void restartLoader(){
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v("Loader", "Trying to load: "+mSearchURL);
        return new BookLoader(this, mSearchURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.v("onLoadFinished", "Finished loading");
        mEmptyStateTextView.setText(R.string.search_for_books);
        View loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);
        mAdapter.clear();
        if(books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        } else if(books==null && mSearchText !=null){
            mEmptyStateTextView.setText(R.string.no_books);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
