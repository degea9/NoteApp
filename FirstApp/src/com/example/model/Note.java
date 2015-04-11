package com.example.model;

import java.util.ArrayList;

public class Note {
	private long id;
	private String mTitle;
	private String mNoteDetail;
	private int color;
	private long created_at;
	private long alarm_time;
	private ArrayList<Photo> mListPhoto;
	
	public Note(){
		
	}
	
	public Note(String title,String note){
		mTitle = title;
		mNoteDetail = note;
	}
	
	public Note(String title,String note,ArrayList<Photo> listPhoto){
		mTitle = title;
		mNoteDetail = note;
		mListPhoto = listPhoto;
	}
	
	
	public ArrayList<Photo> getListPhoto() {
		return mListPhoto;
	}
	public void setListPhoto(ArrayList<Photo> mListPhoto) {
		this.mListPhoto = mListPhoto;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getNoteDetail() {
		return mNoteDetail;
	}

	public void setNoteDetail(String mNoteDetail) {
		this.mNoteDetail = mNoteDetail;
	}

	
	public void removePhoto(int location){
		//photo remove itself from db
		mListPhoto.get(location).removePhoto();
		//list remove the photo's reference
		mListPhoto.remove(location);
	}
	
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color =  color;
	}


	public long getAlarmTime() {
		return alarm_time;
	}

	public void setAlarmTime(long alarm_time) {
		this.alarm_time = alarm_time;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(long created_at) {
		this.created_at = created_at;
	}


}
