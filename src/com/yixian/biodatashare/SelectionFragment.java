package com.yixian.biodatashare;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.facebook.biodatashare.R;
import com.facebook.biodatashare.display.DisplayMapActivity;
import com.facebook.biodatashare.display.TimeSelection;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.ActionBar.Tab;


public class SelectionFragment extends Fragment  {

	private static final String TAG = "SelectionFragment";
	private static final String USER = "UserName";
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private Button btnDownload, btnDisplayMap, btnDisplayChart;
	private boolean UPLOADING = false;
	private String userId;
	private String userName_Id = "";
	private static final int REAUTH_ACTIVITY_CODE = 100;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.selection, container, false);
		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);

		btnDisplayMap = (Button) view.findViewById(R.id.btnDisPlayMap);
		btnDownload = (Button) view.findViewById(R.id.btnDownload);
		btnDisplayChart = (Button)view.findViewById(R.id.btnDisplayChart);

		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// Get the user's data
			makeMeRequest(session);
		}
		btnDownload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences pref = getActivity().getSharedPreferences(
						USER, 0);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("USER_ID", userName_Id);
				editor.commit();

				Intent intent = new Intent(getActivity(),
						TimeSelectionActivity.class);
				startActivity(intent);

			}
		});

		btnDisplayMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),DisplayMapActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		
		btnDisplayChart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),TimeSelection.class);
				getActivity().startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {

			// Get the user's data.
			makeMeRequest(session);

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
								userId = user.getId();
								String userName = user.getName();
								userName_Id = userName + "_" + userId;
								profilePictureView.setProfileId(user.getId());
								// Set the Textview's text to the user's name.
								userNameView.setText(userName);
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.

						}
					}
				});
		request.executeAsync();
	}

	
}