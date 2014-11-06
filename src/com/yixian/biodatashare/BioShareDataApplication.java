package com.yixian.biodatashare;

import java.util.List;


import com.facebook.model.GraphUser;

import android.app.Application;

public class BioShareDataApplication extends Application{
	private List<GraphUser> selectedUsers;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
  
	public List<GraphUser> getSelectedUsers() {
	    return selectedUsers;
	}
	public void setSelectedUsers(List<GraphUser> users) {
	    selectedUsers = users;
	}
}
