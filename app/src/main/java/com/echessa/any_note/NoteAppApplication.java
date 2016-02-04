package com.echessa.any_note;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class NoteAppApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "eqD3yQotGAATEGwDTicdCENh1uuXzgEWxf5c154c", "nwlXfLZgj9yBRyta934dHfDty1VtEGzQq8upGAj1");

		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
	}

}
