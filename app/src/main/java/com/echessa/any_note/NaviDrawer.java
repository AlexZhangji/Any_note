package com.echessa.any_note;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.echessa.any_note.fragment.FragmentIndex;
import com.echessa.any_note.fragment.Settings;
import com.echessa.any_note.fragment.fragementBtn;
import com.parse.ParseUser;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;

/**
 * Created by JI on 2015/12/10.
 */
public class NaviDrawer extends MaterialNavigationDrawer implements MaterialAccountListener {

    private String userName = null;
    private String email = null;

    @Override
    public void init(Bundle savedInstanceState) {

        // get current user:
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();

        }else {
            ParseUser curUser = ParseUser.getCurrentUser();
            userName = curUser.getUsername();
            email = curUser.getEmail();

        }


        MaterialAccount account = new MaterialAccount(this.getResources(), userName, email,
                R.drawable.profile_img, R.drawable.material_back_dark_blue);
        this.addAccount(account);

        this.setAccountListener(this); // or new MaterialAccountListener() {}

        // create sections
        this.addSection(newSection("Notes", R.drawable.ic_action_list_2,
                new FragmentIndex()).setSectionColor(Color.parseColor("#2196F3")));
        this.addSection(newSection("Profile", R.drawable.ic_action_yinyang,
                new fragementBtn()).setSectionColor(Color.parseColor("#2196F3")));
//        this.addSection(newSection("Section", R.drawable.ic_hotel_grey600_24dp, new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

//        // create bottom section
        this.addBottomSection(newSection("Logout", R.drawable.ic_action_exit,
                new Intent(this, Settings.class)));

    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAccountOpening(MaterialAccount account) {

    }

    @Override
    public void onChangeAccount(MaterialAccount newAccount) {

    }
}
