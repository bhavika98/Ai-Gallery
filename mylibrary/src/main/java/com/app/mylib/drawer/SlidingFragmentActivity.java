package com.app.mylib.drawer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alexvasilkov.android.commons.state.InstanceStateManager;
import com.alexvasilkov.events.Events;

public class SlidingFragmentActivity extends AppCompatActivity implements SlidingActivityBase {

	private SlidingActivityHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InstanceStateManager.restoreInstanceState(this, savedInstanceState);
		mHelper = new SlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		mHelper.onPostCreate(savedInstanceState);
		Events.register(this);
		super.onPostCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#findViewById(int)
	 */
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mHelper.findViewById(id);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mHelper.onSaveInstanceState(outState);
		InstanceStateManager.saveInstanceState(this, outState);
		super.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View v, LayoutParams params) {
		super.setContentView(v, params);
		mHelper.registerAboveContentView(v, params);
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(int)
	 */
	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View)
	 */
	public void setBehindContentView(View v) {
		setBehindContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	public void setBehindContentView(View v, LayoutParams params) {
		mHelper.setBehindContentView(v, params);
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu()
	 */
	public SlidingMenu getSlidingMenu() {
		return mHelper.getSlidingMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#toggle()
	 */
	public void toggle() {
		mHelper.toggle();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showAbove()
	 */
	public void showContent() {
		mHelper.showContent();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showBehind()
	 */
	public void showMenu() {
		mHelper.showMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu()
	 */
	public void showSecondaryMenu() {
		mHelper.showSecondaryMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setSlidingActionBarEnabled(boolean)
	 */
	public void setSlidingActionBarEnabled(boolean b) {
		mHelper.setSlidingActionBarEnabled(b);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Events.unregister(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@NonNull
	@Override
	public ActionBar getSupportActionBar() {
		// Making getSupportActionBar() method to be @NonNull
		ActionBar actionBar = super.getSupportActionBar();
		if (actionBar == null) {
			throw new NullPointerException("Action bar was not initialized");
		}
		return actionBar;
	}
}
