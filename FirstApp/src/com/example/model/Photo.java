package com.example.model;

public class Photo {
	//id that use to store in sqlite db
	private int mPhotoId;
	//path of the photo
	private String mPath;
	
	public Photo(){
		
	}
	
	public Photo(String path){
		mPath = path;
	}
	public int getPhotoId() {
		return mPhotoId;
	}
	public void setPhotoId(int mPhotoId) {
		this.mPhotoId = mPhotoId;
	}
	public String getPath() {
		return mPath;
	}
	public void setPath(String mPath) {
		this.mPath = mPath;
	}
	
	public void removePhoto(){
		//use sqlite manager to remove photo with the specify ID from db
	}
	
	

}
