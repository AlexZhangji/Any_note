package com.echessa.any_note;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

public class MainActivity extends ListActivity {

    private List<Note> posts;
    private Button drawerBtn;
    private PullToRefreshView mPullToRefreshView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        Intent intent = new Intent(getApplicationContext(), NaviDrawer.class);
        startActivity(intent);


//
//        drawerBtn = (Button) findViewById(R.id.drawer_btn);
//        drawerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), NaviDrawer.class);
//                startActivity(intent);
//            }
//        });
//
//        posts = new ArrayList<Note>();
//        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
//                R.layout.list_item_layout, posts);
//        setListAdapter(adapter);
////        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
////        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////                mPullToRefreshView.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        mPullToRefreshView.setRefreshing(false);
////                        refreshPostList();
////
////                    }
////                }, 1);
////            }
////        });
//
//        // add fab
//        ImageView icon = new ImageView(this); // Create an icon
//        icon.setImageResource(R.drawable.ic_action_add);
//
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//                .setContentView(icon)
//                .build();
//
//        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
//        // repeat many times:
//        ImageView itemIcon = new ImageView(this);
//        itemIcon.setImageResource(R.drawable.ic_action_edit);
//        SubActionButton FabEditBtn = itemBuilder.setContentView(itemIcon).build();
//
//        // repeat many times:
//        ImageView itemIcon2 = new ImageView(this);
//        itemIcon2.setImageResource(R.drawable.ic_action_camera);
//        SubActionButton FabImgBtn = itemBuilder.setContentView(itemIcon2).build();
//
//        FabEditBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        FabImgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), RecognizeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        //attach to menu
//        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(FabEditBtn)
//                .addSubActionView(FabImgBtn)
//                .attachTo(actionButton)
//                .build();
//
//        refreshPostList();
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_refresh: {
                refreshPostList();
                break;
            }

            case R.id.action_new: {
                Intent intent = new Intent(this, EditNoteActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.action_logout:
                ParseUser.logOut();
                loadLoginView();
                break;

            case R.id.action_settings: {
                // Do something when user selects Settings from Action Bar overlay
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Note note = posts.get(position);
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("noteId", note.getId());
        intent.putExtra("noteTitle", note.getTitle());
        intent.putExtra("noteContent", note.getContent());
        startActivity(intent);

    }

    private void refreshPostList() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("author", ParseUser.getCurrentUser());

        setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<ParseObject>() {

            @SuppressWarnings("unchecked")
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    posts.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(), post
                                .getString("title"), post.getString("content"));
                        posts.add(note);
                    }
                    ((ArrayAdapter<Note>) getListAdapter())
                            .notifyDataSetChanged();
                } else {

                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }

            }

        });

    }
}
