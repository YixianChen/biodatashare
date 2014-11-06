package com.yixian.biodatashare.share;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.biodatashare.R;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.ProfilePictureView;
import com.yixian.biodatashare.BioShareDataApplication;
import com.yixian.biodatashare.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ShareActivity extends Activity{
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private ListView listView;
	private Button btnShareButton;
	private List<BaseListElement> listElements;
	private boolean pendingAnnounce;
	private static final String TAG = "SelectionFragment";
	private static final String PENDING_ANNOUNCE_KEY = "pendingAnnounce";
	private UiLifecycleHelper uiHelper;
	private ImageView img;
	private String filepath = "sdcard/";
	private static String strName = "screenshot_temp.png";
	private Bitmap b;
	private EditText etShareDiscription;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions","user_photos","publish_stream","user_groups");
	private boolean pendingPublishReauthorization = false;
	
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		profilePictureView = (ProfilePictureView)findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		
		userNameView = (TextView)findViewById(R.id.selection_user_name);
		listView = (ListView)findViewById(R.id.selection_list);
		listElements = new ArrayList<BaseListElement>();
		// Add an item for the friend picker
		listElements.add(new PeopleListElement(0));
		
		if (savedInstanceState != null) {
			// Restore the state for each list element
			for (BaseListElement listElement : listElements) {
				listElement.restoreState(savedInstanceState);
			}
			pendingAnnounce = savedInstanceState.getBoolean(
					PENDING_ANNOUNCE_KEY, false);
		}
		listView.setAdapter(new ActionListAdapter(this,
				R.id.selection_list, listElements));
		
		etShareDiscription = (EditText)findViewById(R.id.et_share);
		btnShareButton = (Button) findViewById(R.id.btnShare);
		btnShareButton.setEnabled(false);
		img = (ImageView) findViewById(R.id.selection_screenshot);
		if (displayScreenshot()) {
			img.setImageBitmap(b);
			img.setScaleType(ScaleType.CENTER_INSIDE);
			//img.setBackgroundDrawable(null);
			btnShareButton.setEnabled(true);
		} else {
			Toast.makeText(this,
					"Cannot find the Screenshot, please try it again!!",
					Toast.LENGTH_LONG).show();
		}
		btnShareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Do nothing for now
				handleShare();
			}
		});
		// Check for an open session
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// Get the user's data
			makeMeRequest(session);
		}
		
		
		
	}
	
	
	
	private void handleShare() {
		Session session = Session.getActiveSession();
		if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }
		Request.Callback callback = new Request.Callback() {

			//String fbPhotoAddress = null;

			@Override
			public void onCompleted(Response response) {
				// TODO Auto-generated method stub
				FacebookRequestError error = response.getError();
				if (error != null) {
					// post error
					Log.d("facebook error", error.getErrorMessage());

				} else {
					String uploadResponse = (String) response.getGraphObject()
							.getProperty("id");
					if (uploadResponse == null
							|| !(uploadResponse instanceof String)
							|| TextUtils.isEmpty((String) uploadResponse)) {
						Log.d("Error", "failed photo upload/no response");
					} else {
						// error
						Log.d("Error", uploadResponse);
						//fbPhotoAddress = "https://www.facebook.com/photo.php?fbid="
								//+ uploadResponse;
						tagFriends(uploadResponse);
						Toast.makeText(ShareActivity.this,
								"The Screen Short is uploaded!",
								Toast.LENGTH_SHORT).show();
						deleteScreenShot(filepath+strName);
						Intent intent = new Intent();
						intent.setClass(ShareActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						
					}
				}
			}

			
		};
		Request photoRequest = Request.newUploadPhotoRequest(session, b,
				callback);
		Bundle params = photoRequest.getParameters();
		String text = etShareDiscription.getText().toString();
		params.putString("message",text);
		photoRequest.setParameters(params);
		photoRequest.executeAsync();
	}
	}
	
	
	private void tagFriends(String uploadResponse) {
		// TODO Auto-generated method stub
		Session session = Session.getActiveSession();
		if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	            session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }
		}
		
		List<GraphUser> users= ((BioShareDataApplication) getApplication()).getSelectedUsers();
        if(users!=null){
        JSONArray jsonTaggedUsers = new JSONArray();
        for(int i=0;i<users.size();i++){
        	try {
				jsonTaggedUsers.put(new JSONObject().put("tag_uid", users.get(i).getId()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 System.out.println(jsonTaggedUsers.toString());
        }
        //String[] selectedUsersTag = friends_Id.toArray(new String[friends_Id.size()]);  
        System.out.println("testing ...........");
        
        String inputParm = uploadResponse + "/tags?tags=" + jsonTaggedUsers.toString();
        
        
    	System.out.println("User :"+jsonTaggedUsers.toString()+" will be tagged!!");
    	Bundle params = new Bundle();
    	params.putString("tags",jsonTaggedUsers.toString());
    	
    	Request request = new Request(session, inputParm, params, HttpMethod.POST, null);
    	RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();
        }
        
	}
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				// Session updated with new permissions
				// so try publishing once more.
				tokenUpdated();
				// if (pendingPublishReauthorization &&
				// state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				// pendingPublishReauthorization = false;
				// }
			} else {
				// Get the user's data.
				makeMeRequest(session);
			}
		}
	}
	private void deleteScreenShot(String filename){
		File file = new File(filename);
		if(file.exists()){
			file.delete();

			}
		
	}
	
	private boolean displayScreenshot() {
		// TODO Auto-generated method stub

		File file = new File(filepath);
		if (file.exists()) {
			b = BitmapFactory.decodeFile(filepath + strName);
			// b = decodeFile(fis);
			return true;
		}
		return false;

	}
	
	private void tokenUpdated() {
		// Check if a publish action is in progress
		// awaiting a successful reauthorization
		if (pendingAnnounce) {
			// Publish the action
			handleShare();
		}
	}
	
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Set the id for the ProfilePictureView
								// view that in turn displays the profile
								// picture.
								profilePictureView.setProfileId(user.getId());
								// Set the Textview's text to the user's name.
								userNameView.setText(user.getName());
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
							
						}
					}
				});
		request.executeAsync();
	}
	
	
	
	
	private class PeopleListElement extends BaseListElement {
		private static final String FRIENDS_KEY = "friends";
		private List<GraphUser> selectedUsers;

		public PeopleListElement(int requestCode) {
			super(getResources().getDrawable(R.drawable.ic_launcher),
					getResources().getString(
							R.string.action_people), 
							getResources().getString(
									R.string.action_people_default),
					requestCode);
		}

		@Override
		protected View.OnClickListener getOnClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startPickerActivity(PickerActivity.FRIEND_PICKER,
							getRequestCode());
				}
			};
		}
		private void startPickerActivity(Uri data, int requestCode) {
			Intent intent = new Intent();
			intent.setData(data);
			intent.setClass(ShareActivity.this, PickerActivity.class);
			startActivityForResult(intent, requestCode);
		}

		@Override
		protected void onActivityResult(Intent data) {
			selectedUsers = ((BioShareDataApplication) getApplication()).getSelectedUsers();
			setUsersText();
			notifyDataChanged();
		}

		@Override
		protected void populateOGAction(OpenGraphAction action) {
			// TODO Auto-generated method stub
			if (selectedUsers != null) {
				action.setTags(selectedUsers);
			}
		}

		@Override
		protected void onSaveInstanceState(Bundle bundle) {
			if (selectedUsers != null) {
				bundle.putByteArray(FRIENDS_KEY, getByteArray(selectedUsers));
			}
		}

		@Override
		protected boolean restoreState(Bundle savedState) {
			byte[] bytes = savedState.getByteArray(FRIENDS_KEY);
			if (bytes != null) {
				selectedUsers = restoreByteArray(bytes);
				setUsersText();
				return true;
			}
			return false;
		}

		private void setUsersText() {
			String text = null;
			if (selectedUsers != null) {
				// If there is one friend
				if (selectedUsers.size() == 1) {
					text = String.format(
							getResources().getString(
									R.string.single_user_selected),
							selectedUsers.get(0).getName());
				} else if (selectedUsers.size() == 2) {
					// If there are two friends
					text = String.format(
							getResources().getString(
									R.string.two_users_selected), selectedUsers
									.get(0).getName(), selectedUsers.get(1)
									.getName());
				} else if (selectedUsers.size() > 2) {
					// If there are more than two friends
					text = String.format(
							getResources().getString(
									R.string.multiple_users_selected),
							selectedUsers.get(0).getName(),
							(selectedUsers.size() - 1));
				}
			}
			if (text == null) {
				// If no text, use the placeholder text
				text = getResources().getString(R.string.action_people_default);
			}
			// Set the text in list element. This will notify the
			// adapter that the data has changed to
			// refresh the list view.
			setText2(text);
		}

		private byte[] getByteArray(List<GraphUser> users) {
			// convert the list of GraphUsers to a list of String
			// where each element is the JSON representation of the
			// GraphUser so it can be stored in a Bundle
			List<String> usersAsString = new ArrayList<String>(users.size());

			for (GraphUser user : users) {
				usersAsString.add(user.getInnerJSONObject().toString());
			}
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				new ObjectOutputStream(outputStream).writeObject(usersAsString);
				return outputStream.toByteArray();
			} catch (IOException e) {
				Log.e(TAG, "Unable to serialize users.", e);
			}
			return null;
		}

		private List<GraphUser> restoreByteArray(byte[] bytes) {
			try {
				@SuppressWarnings("unchecked")
				List<String> usersAsString = (List<String>) (new ObjectInputStream(
						new ByteArrayInputStream(bytes))).readObject();
				if (usersAsString != null) {
					List<GraphUser> users = new ArrayList<GraphUser>(
							usersAsString.size());
					for (String user : usersAsString) {
						GraphUser graphUser = GraphObject.Factory.create(
								new JSONObject(user), GraphUser.class);
						users.add(graphUser);
					}
					return users;
				}
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "Unable to deserialize users.", e);
			} catch (IOException e) {
				Log.e(TAG, "Unable to deserialize users.", e);
			} catch (JSONException e) {
				Log.e(TAG, "Unable to deserialize users.", e);
			}
			return null;
		}
	}
	
	private class ActionListAdapter extends ArrayAdapter<BaseListElement> {
		private List<BaseListElement> listElements;

		public ActionListAdapter(Context context, int resourceId,
				List<BaseListElement> listElements) {
			super(context, resourceId, listElements);
			this.listElements = listElements;
			// Set up as an observer for list item changes to
			// refresh the view.
			for (int i = 0; i < listElements.size(); i++) {
				listElements.get(i).setAdapter(this);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) 
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.listitem_share, null);
			}

			BaseListElement listElement = listElements.get(position);
			if (listElement != null) {
				view.setOnClickListener(listElement.getOnClickListener());
				ImageView icon = (ImageView) view.findViewById(R.id.icon);
				TextView text1 = (TextView) view.findViewById(R.id.text1);
				TextView text2 = (TextView) view.findViewById(R.id.text2);
				if (icon != null) {
					icon.setImageDrawable(listElement.getIcon());
				}
				if (text1 != null) {
					text1.setText(listElement.getText1());
				}
				if (text2 != null) {
					text2.setText(listElement.getText2());
				}
			}
			return view;
		}

	}
}
