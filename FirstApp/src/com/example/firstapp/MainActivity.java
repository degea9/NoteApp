package com.example.firstapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.model.DatabaseHandler;
import com.example.model.Note;
import com.example.model.NoteManager;
import com.exmaple.adapter.GridNoteAdapter;

public class MainActivity extends Activity {
	private GridNoteAdapter mAdapter;
	private NoteManager noteManager;
	private static int currentPosition;
	private GridView gridView;
	public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		/this.deleteDatabase(DatabaseHandler.DATABASE_NAME);
		noteManager = new NoteManager(getApplicationContext());
		 gridView = (GridView)findViewById(R.id.mainActivity_grid);
		ArrayList<Note> list = noteManager.getAllNotes();
		mAdapter = new GridNoteAdapter(this.getApplicationContext(), list);
		gridView.setAdapter(mAdapter);
		gridView.setSelection(currentPosition);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPosition = gridView.getFirstVisiblePosition();
				TextView edt_note_id = (TextView)view.findViewById(R.id.tv_note_id);
				String node_id = edt_note_id.getText().toString();
				Intent intent = new Intent(MainActivity.this,NoteDetailActivity.class);
				intent.putExtra(EXTRA_NOTE_ID, Long.valueOf(node_id));
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add) {
			Intent intent = new Intent(this,NoteDetailActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
