package com.example.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NoteManager {

	private DatabaseHandler databaseHandler;
	private SQLiteDatabase db;

	public NoteManager(Context context) {
		databaseHandler = DatabaseHandler.getInstance(context);
		db = databaseHandler.getWritableDatabase();
	}

	public long createNote(Note note) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_TITLE, note.getTitle());
		values.put(DatabaseHandler.KEY_DETAIL, note.getNoteDetail());
		values.put(DatabaseHandler.KEY_CREATED_AT, note.getCreatedAt());
		values.put(DatabaseHandler.KEY_ALARM, note.getAlarmTime());
		values.put(DatabaseHandler.KEY_COLOR, note.getColor());

		// insert row
		long note_id = db.insert(DatabaseHandler.TABLE_NOTE, null, values);
		List<Photo> listPhotos = note.getListPhoto();
		for (Photo photo : listPhotos) {
			createPhotoAssociateWithNote(note_id, photo);
		}

		return note_id;
	}

	public long createPhotoAssociateWithNote(long note_id, Photo photo) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_PHOTO_PATH, photo.getPath());
		values.put(DatabaseHandler.KEY_NOTE_ID, note_id);
		long photo_id = db.insert(DatabaseHandler.TABLE_PHOTO, null, values);
		return photo_id;
	}

	public Note getNote(long note_id) {
		String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_NOTE
				+ " WHERE " + DatabaseHandler.KEY_ID + " = " + note_id;
		Cursor cursor = db.rawQuery(selectQuery, null);
		Note note = new Note();
		if (cursor.moveToFirst()) {
			note.setId(cursor.getInt(cursor
					.getColumnIndex(DatabaseHandler.KEY_ID)));
			note.setTitle(cursor.getString(cursor
					.getColumnIndex(DatabaseHandler.KEY_TITLE)));
			note.setNoteDetail(cursor.getString(cursor
					.getColumnIndex(DatabaseHandler.KEY_DETAIL)));
			note.setAlarmTime(cursor.getLong(cursor
					.getColumnIndex(DatabaseHandler.KEY_ALARM)));
			note.setCreatedAt(cursor.getLong(cursor
					.getColumnIndex(DatabaseHandler.KEY_CREATED_AT)));
			note.setColor((cursor.getInt(cursor
					.getColumnIndex(DatabaseHandler.KEY_COLOR))));
			note.setListPhoto(getPhotoList(note.getId()));
		}
		return note;
	}

	public long updateNote(Note note) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_TITLE, note.getTitle());
		values.put(DatabaseHandler.KEY_DETAIL, note.getNoteDetail());
		values.put(DatabaseHandler.KEY_ALARM, note.getAlarmTime());

		// updating row
		db.update(DatabaseHandler.TABLE_NOTE, values,
				DatabaseHandler.KEY_ID + " = ?",
				new String[] { String.valueOf(note.getId()) });
		// List<Photo> listPhotos = note.getListPhoto();
		// for (Photo photo : listPhotos) {
		// createPhotoAssociateWithNote(note_id, photo);
		// }
		return note.getId();
	}

	public int updateNoteColor(Note note) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_COLOR, note.getColor());
		// updating row
		int note_id = db.update(DatabaseHandler.TABLE_NOTE, values,
				DatabaseHandler.KEY_ID + " = ?",
				new String[] { String.valueOf(note.getId()) });
		return note_id;

	}

	public ArrayList<Photo> getPhotoList(long note_id) {
		ArrayList<Photo> listPhotos = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_PHOTO
				+ " WHERE " + DatabaseHandler.KEY_NOTE_ID + " = " + note_id;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Photo photo = new Photo();
				photo.setPhotoId(cursor.getInt(cursor
						.getColumnIndex(DatabaseHandler.KEY_ID)));
				photo.setPath(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.KEY_PHOTO_PATH)));
				listPhotos.add(photo);
				Log.e("updatelogic", "path " + photo.getPath());
			} while (cursor.moveToNext());
		}

		return listPhotos;
	}

	public ArrayList<Note> getAllNotes() {
		ArrayList<Note> listNotes = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_NOTE
				+ " ORDER BY " + DatabaseHandler.KEY_ID + " DESC ";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Note note = new Note();
				note.setId(cursor.getInt(cursor
						.getColumnIndex(DatabaseHandler.KEY_ID)));
				Log.e("tuandang", "id " + note.getId());
				note.setTitle(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.KEY_TITLE)));
				note.setNoteDetail(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.KEY_DETAIL)));
				note.setCreatedAt(cursor.getLong(cursor
						.getColumnIndex(DatabaseHandler.KEY_CREATED_AT)));
				note.setAlarmTime(cursor.getLong(cursor
						.getColumnIndex(DatabaseHandler.KEY_ALARM)));
				note.setColor(cursor.getInt(cursor
						.getColumnIndex(DatabaseHandler.KEY_COLOR)));
				listNotes.add(note);
			} while (cursor.moveToNext());
		}
		return listNotes;
	}

	public void deletePhoto(long photoId) {
		db.delete(DatabaseHandler.TABLE_PHOTO, DatabaseHandler.KEY_ID + " = ?",
				new String[] { String.valueOf(photoId) });
	}

	public void deletePhotoByNoteId(long noteId) {
		db.delete(DatabaseHandler.TABLE_PHOTO, DatabaseHandler.KEY_NOTE_ID
				+ " = ?", new String[] { String.valueOf(noteId) });
	}

	public void deletePhoto(String photoId) {
		db.delete(DatabaseHandler.TABLE_PHOTO, DatabaseHandler.KEY_ID + " = ?",
				new String[] { photoId });
	}

	public void deleteNote(long noteId) {
		db.delete(DatabaseHandler.TABLE_NOTE, DatabaseHandler.KEY_ID + " = ?",
				new String[] { String.valueOf(noteId) });
		// delete all photo attached with note
		deletePhotoByNoteId(noteId);

	}

	public Note nextNote(long noteId) {
		String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_NOTE
				+ " WHERE " + DatabaseHandler.KEY_ID + " > " + noteId
				+ " ORDER BY " + DatabaseHandler.KEY_ID + " ASC ";
		Cursor cursor = db.rawQuery(selectQuery, null);
		Note note = new Note();
		note.setId(-1);
		if (cursor.moveToFirst()) {
			updateNote(note, cursor);
		}
		// current note is the latest note
		return note;
	}

	public Note previousNote(long noteId) {
		String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_NOTE
				+ " WHERE " + DatabaseHandler.KEY_ID + " < " + noteId
				+ " ORDER BY " + DatabaseHandler.KEY_ID + " DESC ";
		Cursor cursor = db.rawQuery(selectQuery, null);
		Note note = new Note();
		note.setId(-1);
		if (cursor.moveToFirst()) {
			updateNote(note, cursor);
		}
		// current note is the latest note
		return note;
	}

	private void updateNote(Note note, Cursor cursor) {
		note.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
		note.setTitle((cursor.getString(cursor
				.getColumnIndex(DatabaseHandler.KEY_TITLE))));
		note.setNoteDetail(cursor.getString(cursor
				.getColumnIndex(DatabaseHandler.KEY_DETAIL)));
		note.setCreatedAt(cursor.getLong(cursor
				.getColumnIndex(DatabaseHandler.KEY_CREATED_AT)));
		note.setAlarmTime(cursor.getLong(cursor
				.getColumnIndex(DatabaseHandler.KEY_ALARM)));
		note.setColor(cursor.getInt(cursor
				.getColumnIndex(DatabaseHandler.KEY_COLOR)));
		note.setListPhoto(getPhotoList(note.getId()));
	}

}
