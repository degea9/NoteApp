package com.example.firstapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.customview.ExpandableHeightGridView;
import com.example.dialog.DatePickerFragment;
import com.example.dialog.PickColorDialog;
import com.example.dialog.PickPhotoDialog;
import com.example.dialog.TimePickerFragment;
import com.example.model.Note;
import com.example.model.NoteManager;
import com.example.model.Photo;
import com.example.utils.CommonUtil;
import com.example.utils.NotificationHelper;
import com.exmaple.adapter.GridNoteAdapter;
import com.exmaple.adapter.GridPhotoAdapter;

/**
 * this activity represent note's detail information screen,also include
 * modify,save note
 * 
 * @author tuanda
 * 
 */
public class NoteDetailActivity extends FragmentActivity implements
		OnClickListener, OnItemSelectedListener,
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
		OnItemClickListener, TextWatcher {
	private ExpandableHeightGridView mGridphoto;
	private GridPhotoAdapter mGridPhotoAdapter;
	public static final int SELECT_PHOTO = 100;
	public static final int CAMERA_REQUEST = 200;
	private static final String FRAGMENT_PICK_PHOTO_TAG = "FRAGMENT_PICK_PHOTO";
	private static final String FRAGMENT_PICK_COLOR_TAG = "FRAGMENT_PICK_COLOR";
	private static final String DATE_TIME_OTHER = "Other";
	private static final String TODAY = "Today";
	private static final String TOMORROW = "Tomorrow";
	private static ArrayList<Photo> list = new ArrayList<>();
	private LinearLayout ll_bottom;
	private TextView tv_created_date;
	private EditText edt_title;
	private EditText edt_detail;
	private ImageView imv_discard;
	private ImageView imv_share;
	private ImageView imv_next_note;
	private ImageView imv_prev_note;
	private TextView tv_alarm;
	private Spinner spn_date;
	private Spinner spn_time;
	private RelativeLayout rl_note;
	private ImageView imv_cancel_alarm;
	private NoteManager noteManager;
	private long note_id = -1;
	private Note note;
	private Calendar cal = Calendar.getInstance();
	private File capturedImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_detail);
		if (savedInstanceState != null)
			note_id = savedInstanceState.getLong(MainActivity.EXTRA_NOTE_ID);
		if (getIntent() != null) 
			note_id = getIntent().getLongExtra(MainActivity.EXTRA_NOTE_ID, -1);
		initView();
		cal.setTimeInMillis(0);
		noteManager = new NoteManager(getApplicationContext());
		if (note_id != -1) {
			Log.e("updatelogic", "notedetail " + note_id);
			ll_bottom.setVisibility(View.VISIBLE);
			note = noteManager.getNote(note_id);
			initNote(note);
		} else {
			note = new Note();
			list = new ArrayList<>();
		}
		
		mGridPhotoAdapter = new GridPhotoAdapter(this, list);
		mGridphoto.setAdapter(mGridPhotoAdapter);
		mGridPhotoAdapter.notifyDataSetChanged();
		mGridphoto.setOnItemClickListener(this);
	}

	/**
	 * initialize views for the activity
	 * 
	 */
	public void initView() {
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mGridphoto = (ExpandableHeightGridView) findViewById(R.id.note_detail_grid);
		mGridphoto.setExpanded(true);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_action_bottom);
		rl_note = (RelativeLayout) findViewById(R.id.rl_note);
		tv_created_date = (TextView) findViewById(R.id.tv_created_date);
		edt_title = (EditText) findViewById(R.id.edt_title);
		edt_detail = (EditText) findViewById(R.id.edt_details);
		imv_discard = (ImageView) findViewById(R.id.imv_discard);
		imv_share = (ImageView) findViewById(R.id.imv_share);
		imv_next_note = (ImageView) findViewById(R.id.imv_next);
		imv_prev_note = (ImageView) findViewById(R.id.imv_previous);
		imv_cancel_alarm = (ImageView) findViewById(R.id.imv_delete_alarm);
		spn_date = (Spinner) findViewById(R.id.spn_date);
		spn_time = (Spinner) findViewById(R.id.spn_time);
		tv_alarm = (TextView) findViewById(R.id.tv_alarm);
		edt_title.addTextChangedListener(this);
		imv_discard.setOnClickListener(this);
		imv_share.setOnClickListener(this);
		imv_next_note.setOnClickListener(this);
		imv_prev_note.setOnClickListener(this);
		imv_cancel_alarm.setOnClickListener(this);
		tv_alarm.setOnClickListener(this);
		displayAlarmDate(null);
		displayAlarmTime(null);
	}

	public void initNote(Note note) {
		note_id = note.getId();
		if (!note.getTitle().isEmpty()) {
			setTitle(note.getTitle());
			edt_title.setText(note.getTitle());
		} else {
			setTitle(GridNoteAdapter.UN_TITLE);
			edt_title.setText(GridNoteAdapter.UN_TITLE);
		}

		edt_detail.setText(note.getNoteDetail());
		rl_note.setBackgroundColor(note.getColor());
		if (note.getAlarmTime() != 0) {
			cal.setTimeInMillis(note.getAlarmTime());
			displayAlarmDate(cal);
			displayAlarmTime(cal);
		}
		tv_created_date.setText(CommonUtil.getDateFromMiliseconds(
				"dd/MM/yyyy hh:mm", note.getCreatedAt()));
		if (note.getListPhoto() != null)
			list = note.getListPhoto();
		mGridPhotoAdapter = new GridPhotoAdapter(getApplicationContext(), list);
		mGridphoto.setAdapter(mGridPhotoAdapter);
		mGridPhotoAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.note_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			backToMainMenu();

		} else if (id == R.id.action_new_note) {
			Intent intent = new Intent(NoteDetailActivity.this,
					NoteDetailActivity.class);
			startActivity(intent);
			finish();
			return true;

		} else if (id == R.id.action_pick_photo) {
			showPickPhotoDialog();
			return true;

		} else if (id == R.id.action_pick_color) {
			showPickColorDialog();
			return true;
		} else if (id == R.id.action_save) {
			saveNote();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		saveNote();
	}

	/**
	 * show a dialog that use to pick a image to attach with note
	 * 
	 */
	private void showPickPhotoDialog() {
		FragmentManager fm = getFragmentManager();
		PickPhotoDialog pickPhotoDialog = new PickPhotoDialog();
		pickPhotoDialog.show(fm, FRAGMENT_PICK_PHOTO_TAG);
	}

	/**
	 * show a dialog that use to pick color for note
	 * 
	 */
	private void showPickColorDialog() {
		FragmentManager fm = getFragmentManager();
		PickColorDialog pickColorDialog = new PickColorDialog();
		pickColorDialog.show(fm, FRAGMENT_PICK_COLOR_TAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				Photo photo = new Photo(getPath(selectedImage));
				if (note_id != -1)
					noteManager.createPhotoAssociateWithNote(note_id, photo);
				list.add(photo);
				mGridPhotoAdapter.notifyDataSetChanged();
			}
			break;
		case CAMERA_REQUEST:
			if (resultCode == RESULT_OK) {
				if (capturedImage != null) {
					Photo photo = new Photo(capturedImage.getAbsolutePath());
					if (note_id != -1)
						noteManager
								.createPhotoAssociateWithNote(note_id, photo);
					list.add(photo);
					mGridPhotoAdapter.notifyDataSetChanged();
				}
			}
			break;
		}
	}

	/**
	 * get path of the image that was chosen in gallery application
	 * 
	 * @param uri
	 *            : the returned URI from {@link}
	 *            {@link #onActivityResult(int, int, Intent)}
	 * @return : the path of the image
	 */
	public String getPath(Uri uri) {

		if (uri == null) {
			return null;
		}
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(projection[0]);
			String path = cursor.getString(columnIndex);
			cursor.close();
			return path;
		}

		return uri.getPath();
	}

	private void showConfirmDeleteDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Confirm Delete")
				.setMessage("Are you sure you want to delete this?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								// delete note
								noteManager.deleteNote(note_id);
								Intent intent = new Intent(
										NoteDetailActivity.this,
										MainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							}
						}).setNegativeButton(R.string.action_cancel, null)
				.setIcon(0).show();
	}

	/**
	 * share not content via various application such as
	 * :messenger,facebook,etc.. only note's title and content will be shared
	 */
	public void share() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		String textShare = edt_title.getText().toString() + "\r\n"
				+ edt_detail.getText().toString();
		sendIntent.putExtra(Intent.EXTRA_TEXT, textShare);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imv_discard:
			showConfirmDeleteDialog();
			break;

		case R.id.imv_share:
			share();
			break;

		case R.id.tv_alarm:
			unHideAlarm();
			break;

		case R.id.imv_delete_alarm:
			tv_alarm.setVisibility(View.VISIBLE);
			spn_date.setVisibility(View.GONE);
			spn_time.setVisibility(View.GONE);
			imv_cancel_alarm.setVisibility(View.GONE);
			break;
		case R.id.imv_next:
			Note nextNote = noteManager.nextNote(note_id);
			if (nextNote.getId() != -1) {
				initNote(nextNote);
				imv_next_note.setEnabled(true);
				imv_prev_note.setAlpha(1f);
			} else
				imv_next_note.setAlpha(0.25f);
			break;
		case R.id.imv_previous:
			Note prevNote = noteManager.previousNote(note_id);
			if (prevNote.getId() != -1) {
				initNote(prevNote);
				imv_prev_note.setEnabled(true);
				imv_next_note.setAlpha(1f);
			} else
				imv_prev_note.setAlpha(0.25f);
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.spn_date:
			if (DATE_TIME_OTHER.equals(parent.getItemAtPosition(position)
					.toString())) {
				DialogFragment datePickerFragment = new DatePickerFragment();
				datePickerFragment.show(getFragmentManager(), "datePicker");
			} else if (TODAY.equals(parent.getItemAtPosition(position)
					.toString())) {
				Calendar todayCal = Calendar.getInstance();
				cal.set(todayCal.get(Calendar.YEAR),
						todayCal.get(Calendar.MONTH),
						todayCal.get(Calendar.DAY_OF_MONTH));
			} else if (TOMORROW.equals(parent.getItemAtPosition(position)
					.toString())) {
				Calendar todayCal = Calendar.getInstance();
				todayCal.add(Calendar.DATE, 1);
				cal.set(todayCal.get(Calendar.YEAR),
						todayCal.get(Calendar.MONTH),
						todayCal.get(Calendar.DAY_OF_MONTH));
			}
			break;
		case R.id.spn_time:
			if (DATE_TIME_OTHER.equals(parent.getItemAtPosition(position)
					.toString())) {
				DialogFragment timePickerFragment = new TimePickerFragment();
				timePickerFragment.show(getFragmentManager(), "timePicker");
			} else if (position == 0) {
				cal.set(Calendar.HOUR_OF_DAY, 9);
				cal.set(Calendar.MINUTE, 0);
			} else if (position == 1) {
				cal.set(Calendar.HOUR_OF_DAY, 13);
				cal.set(Calendar.MINUTE, 0);
			} else if (position == 2) {
				cal.set(Calendar.HOUR_OF_DAY, 17);
				cal.set(Calendar.MINUTE, 0);
			} else if (position == 3) {
				cal.set(Calendar.HOUR_OF_DAY, 20);
				cal.set(Calendar.MINUTE, 0);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		if (view.isShown()) {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			displayAlarmDate(cal);
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		if (view.isShown()) {
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, 0);
			displayAlarmTime(cal);
		}

	}

	private void displayAlarmDate(Calendar cal) {
		String[] dateArray = getResources().getStringArray(R.array.date_array);
		ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dateArray);
		// Apply the adapter to the spinner
		spn_date.setAdapter(dateAdapter);
		spn_date.setOnItemSelectedListener(this);

		if (cal != null) {
			dateArray[2] = CommonUtil.getDateFromCal("dd/MM/yyyy", cal);
			spn_date.setSelection(2);
			unHideAlarm();
		}
	}

	private void displayAlarmTime(Calendar cal) {
		String[] timeArray = getResources().getStringArray(R.array.time_array);
		ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, timeArray);
		// Apply the adapter to the spinner
		spn_time.setAdapter(timeAdapter);
		spn_time.setOnItemSelectedListener(this);
		if (cal != null) {
			timeArray[4] = String.format("%02d:%02d",
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
			spn_time.setSelection(4);
			unHideAlarm();
		}
	}

	private void unHideAlarm() {
		tv_alarm.setVisibility(View.GONE);
		spn_date.setVisibility(View.VISIBLE);
		spn_time.setVisibility(View.VISIBLE);
		imv_cancel_alarm.setVisibility(View.VISIBLE);
	}

	/**
	 * change color of the the activity screen by specific color
	 * 
	 * @param color
	 */
	public void changeViewColor(int color) {
		rl_note.setBackgroundColor(color);
		// add color to db
		note.setColor(color);
		if (note_id != -1)
			noteManager.updateNoteColor(note);
	}

	private void backToMainMenu() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayList<String> paths = new ArrayList<>();
		for (int i = 0; i < list.size(); i++)
			paths.add(list.get(i).getPath());
		Intent intent = new Intent(NoteDetailActivity.this, SwipeActivity.class);
		intent.putStringArrayListExtra("photo_path", paths);
		intent.putExtra(SwipeActivity.CURRENT_POS, position);
		startActivity(intent);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		setTitle(edt_title.getText().toString());

	}

	public void setCapturedImageFile(File file) {
		capturedImage = file;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(MainActivity.EXTRA_NOTE_ID, note_id);
		super.onSaveInstanceState(outState);
	}

	private void saveNote() {
		note.setTitle(edt_title.getText().toString());
		note.setNoteDetail(edt_detail.getText().toString());
		note.setCreatedAt(Calendar.getInstance().getTimeInMillis());
		note.setAlarmTime(cal.getTimeInMillis());
		if (note_id == -1) {
			note.setListPhoto(list);
			note_id = noteManager.createNote(note);
		} else {
			note_id = noteManager.updateNote(note);
		}
		note.setId(note_id);
		NotificationHelper.getInstance(getApplicationContext()).schedule(note);
		backToMainMenu();
	}

}
