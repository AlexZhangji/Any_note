package com.echessa.any_note.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.echessa.any_note.EditNoteActivity;
import com.echessa.any_note.LoginActivity;
import com.echessa.any_note.Note;
import com.echessa.any_note.R;
import com.echessa.any_note.RecognizeActivity;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class FragmentIndex extends ListFragment{
    private List<Note> posts;
    private Button drawerBtn;
    private PullToRefreshView mPullToRefreshView;
    private View myFragmentView;
    private FloatingActionMenu actionMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getContext(),
                R.layout.list_item_layout, posts);
        setListAdapter(adapter);


        // add fab
        ImageView icon = new ImageView(getContext()); // Create an icon
        icon.setImageResource(R.drawable.ic_action_add);

        FloatingActionButton actionButton = new FloatingActionButton.Builder((Activity) getContext())
                .setContentView(icon)
                .setBackgroundDrawable(R.drawable.button_action_blue_touch)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder((Activity) getContext());
        // repeat many times:
        ImageView itemIcon = new ImageView(getContext());
        itemIcon.setImageResource(R.drawable.ic_action_edit);
        SubActionButton FabEditBtn = itemBuilder.setContentView(itemIcon).build();
//        FabEditBtn.setBackgroundColor(getResources().getColor(R.color.custom_theme_background));

        // repeat many times:
        ImageView itemIcon2 = new ImageView(getContext());
        itemIcon2.setImageResource(R.drawable.ic_action_camera);
        SubActionButton FabImgBtn = itemBuilder.setContentView(itemIcon2).build();

        FabEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditNoteActivity.class);
                startActivity(intent);
            }
        });

        FabImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecognizeActivity.class);
                startActivity(intent);
            }
        });

        //attach to menu
         actionMenu = new FloatingActionMenu.Builder((Activity) getContext())
                .addSubActionView(FabEditBtn)
                .addSubActionView(FabImgBtn)
                .attachTo(actionButton)
                .build();

        refreshPostList();
    }

    private void loadLoginView() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.list_fragment, container, false);

        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getContext(),
                R.layout.list_item_layout, posts);
        setListAdapter(adapter);
        refreshPostList();

        //felix pull to refresh
        mPullToRefreshView = (PullToRefreshView) myFragmentView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        refreshPostList();
                    }
                }, 1);
            }
        });

        return myFragmentView;
    }


    //close everytime when resume
    @Override
    public void onResume() {
        super.onResume();

        if(actionMenu != null){
            if(actionMenu.isOpen()){
                actionMenu.close(true);
            }
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Note note = posts.get(position);
        Intent intent = new Intent(getContext(), EditNoteActivity.class);
        intent.putExtra("noteId", note.getId());
        intent.putExtra("noteTitle", note.getTitle());
        intent.putExtra("noteContent", note.getContent());
        startActivity(intent);

    }

    private void refreshPostList() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("author", ParseUser.getCurrentUser());


        query.findInBackground(new FindCallback<ParseObject>() {

            @SuppressWarnings("unchecked")
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
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
