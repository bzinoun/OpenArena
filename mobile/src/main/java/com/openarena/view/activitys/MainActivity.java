package com.openarena.view.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.openarena.R;
import com.openarena.controllers.PreferencesManager;
import com.openarena.model.interfaces.EventListener;
import com.openarena.model.objects.EventData;
import com.openarena.util.Const;
import com.openarena.util.L;
import com.openarena.view.dialogs.DialogPlayerInfo;
import com.openarena.view.dialogs.DialogTeam;
import com.openarena.view.fragments.FragmentAbout;
import com.openarena.view.fragments.FragmentFixtureInfo;
import com.openarena.view.fragments.FragmentFixtures;
import com.openarena.view.fragments.FragmentFixturesTeam;
import com.openarena.view.fragments.FragmentLeagues;
import com.openarena.view.fragments.FragmentPlayers;
import com.openarena.view.fragments.FragmentScores;

public class MainActivity extends AppCompatActivity
		implements EventListener, FacebookCallback<Sharer.Result> {

	private Toolbar mToolbar;
	private FragmentManager mFragmentManager;
	private long mLastBack;
	private CallbackManager mCallbackManager;
	private ShareDialog mSharedDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		mFragmentManager = getSupportFragmentManager();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (PreferencesManager.from(this).getBoolean(Const.SUBMITTED, false)) {
			showContent();
		}
		else startActivity(new Intent(this, IntroActivity.class));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mToolbar != null) mToolbar = null;
		if (mFragmentManager != null) mFragmentManager = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mCallbackManager != null) {
			mCallbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onBackPressed() {
		if (mLastBack + Const.TIME_TO_EXIT > System.currentTimeMillis()) {
			showFinishDialog();
		}
		else if (!mFragmentManager.popBackStackImmediate())	showFinishDialog();
		mLastBack = System.currentTimeMillis();
	}

	@Override
	public void onEvent(EventData event) {
		int code = event.gecCode();
		switch (code) {
			case Const.EVENT_CODE_SELECT_LEAGUE:
				if (mFragmentManager.findFragmentByTag(FragmentFixtures.TAG) == null) {
					Bundle data1 = new Bundle();
					data1.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentFixtures.getInstance(data1))
							.addToBackStack(FragmentFixtures.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_FIXTURE_INFO:
				if (mFragmentManager.findFragmentByTag(FragmentFixtureInfo.TAG) == null) {
					Bundle data2 = new Bundle();
					data2.putParcelable("fixture", event.getFixture());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentFixtureInfo.getInstance(data2))
							.addToBackStack(FragmentFixtureInfo.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SHOW_SCORES_TABLE:
				if (mFragmentManager.findFragmentByTag(FragmentScores.TAG) == null) {
					Bundle data3 = new Bundle();
					data3.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentScores.getInstance(data3))
							.addToBackStack(FragmentScores.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_SCORES:
				if (mFragmentManager.findFragmentByTag(DialogTeam.TAG) == null) {
					Bundle data4 = new Bundle();
					data4.putParcelable("scores", event.getScores());
					DialogTeam dialogTeam = DialogTeam.getInstance(data4);
					dialogTeam.show(mFragmentManager, DialogTeam.TAG);
				}
				break;

			case Const.EVENT_CODE_SELECT_PLAYERS:
				if (mFragmentManager.findFragmentByTag(FragmentPlayers.TAG) == null) {
					Bundle data5 = new Bundle();
					data5.putParcelable("team", event.getTeam());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentPlayers.getInstance(data5))
							.addToBackStack(FragmentPlayers.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_FIXTURES:
				if (mFragmentManager.findFragmentByTag(FragmentFixturesTeam.TAG) == null) {
					Bundle data6 = new Bundle();
					data6.putParcelable("team", event.getTeam());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentFixturesTeam.getInstance(data6))
							.addToBackStack(FragmentFixturesTeam.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_PLAYER_INFO:
				if (mFragmentManager.findFragmentByTag(DialogPlayerInfo.TAG) == null) {
					Bundle data7 = new Bundle();
					data7.putParcelable("player", event.getPlayer());
					DialogPlayerInfo dialogPlayerInfo = DialogPlayerInfo.getInstance(data7);
					dialogPlayerInfo.show(mFragmentManager, DialogPlayerInfo.TAG);
				}
				break;

			case Const.EVENT_CODE_SHOW_SETTINGS:
				if (mCallbackManager == null) mCallbackManager = CallbackManager.Factory.create();
				if (mSharedDialog == null) mSharedDialog = new ShareDialog(this);
				mSharedDialog.registerCallback(mCallbackManager, this);


				//startActivity(new Intent(this, IntroActivity.class));
				ShareLinkContent content = new ShareLinkContent.Builder()
						.setContentUrl(Uri.parse("https://developers.facebook.com"))
						.setContentTitle("This is some title (this may be your ads) (:")
						.setContentDescription("Please, don't worry it's just a creative process (:\n" +
								"this is a long description")
						.setImageUrl(Uri.parse("https://raw.githubusercontent.com/TarikW/OpenArena/master/mobile/src/main/res/drawable-xxxhdpi/im_open_arena.png"))
						.setShareHashtag(new ShareHashtag.Builder()
								.setHashtag("#TestShare")
								.build()
						)
						.build();
				//MessageDialog.show(MainActivity.this, content);

				mSharedDialog.show(content, ShareDialog.Mode.AUTOMATIC);

				break;

			case Const.EVENT_CODE_SHOW_ABOUT:
				if (mFragmentManager.findFragmentByTag(FragmentAbout.TAG) == null) {
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentAbout.getInstance(null))
							.addToBackStack(FragmentAbout.TAG)
							.commit();
				}
				break;

			default:
				L.e(MainActivity.class, "new event code->" + code);
		}
	}

	@Override
	public void onSuccess(Sharer.Result result) {
		Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCancel() {
		Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onError(FacebookException error) {
		Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
	}

	private void showFinishDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
				.setTitle(R.string.dialog_exit_title)
				.setPositiveButton(R.string.dialog_exit_positive, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						supportFinishAfterTransition();
					}
				})
				.setNegativeButton(R.string.dialog_exit_negative, null)
				.create();
		dialog.show();
	}

	private void showContent() {
		if (mFragmentManager.findFragmentByTag(FragmentLeagues.TAG) == null) {
			mFragmentManager.beginTransaction()
					.replace(
							R.id.main_container,
							FragmentLeagues.getInstance(null),
							FragmentLeagues.TAG
					).commit();
		}
	}

}
