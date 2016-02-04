package com.echessa.any_note;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements SurfaceHolder.Callback {


//	background video
	private MediaPlayer mp = null;
	SurfaceView mSurfaceView = null;

//	login
	protected EditText usernameEditText;
	protected EditText passwordEditText;
	protected Button loginButton;
	
	protected TextView signUpTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login_background_video);

		//background video init
		mp = new MediaPlayer();
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);
		SurfaceHolder holder = mSurfaceView.getHolder();

		holder.addCallback(this);

//		initViewPager();

		signUpTextView = (TextView)findViewById(R.id.signUpText);
		usernameEditText = (EditText)findViewById(R.id.usernameField);
		passwordEditText = (EditText)findViewById(R.id.passwordField);
		loginButton = (Button)findViewById(R.id.loginButton);
		
		signUpTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				
				username = username.trim();
				password = password.trim();

				if (username.isEmpty() || password.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					builder.setMessage(R.string.login_error_message)
						.setTitle(R.string.login_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					setProgressBarIndeterminateVisibility(true);
					
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						@Override
						public void done(ParseUser user, ParseException e) {
							setProgressBarIndeterminateVisibility(false);
							
							if (e == null) {
								// Success!
								Intent intent = new Intent(LoginActivity.this, NaviDrawer.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
							}
							else {
								// Fail
								AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
								builder.setMessage(e.getMessage())
									.setTitle(R.string.login_error_title)
									.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
					});
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cloud);
			mp.setDataSource(LoginActivity.this, videoUri);
			mp.prepare();
			//Get the dimensions of the video
			// int videoWidth = mp.getVideoWidth();
			//int videoHeight = mp.getVideoHeight();
			//Get the width of the screen
			int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
			int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
			//Get the SurfaceView layout parameters
			android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
			//Set the width of the SurfaceView to the width of the screen
			lp.width = screenWidth;
			//Set the height of the SurfaceView to match the aspect ratio of the video
			//be sure to cast these as floats otherwise the calculation will likely be 0
			lp.height = screenHeight + 110;// (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
			//Commit the layout parameters
			mSurfaceView.setLayoutParams(lp);
			mp.setDisplay(holder);
			//Start video
			mp.start();
		} catch (Exception er) {
			er.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.release();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

	}
}
