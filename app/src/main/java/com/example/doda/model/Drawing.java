package com.example.doda.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Drawing {

	@PrimaryKey(autoGenerate = true)
	int id;
	@ColumnInfo
	String name;
	@ColumnInfo
	String date;
	@ColumnInfo
	String time;
	@ColumnInfo
	int totalMarkers;
	@ColumnInfo
	byte[] bytes;
	@ColumnInfo
	String imgUrl;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTotalMarkers() {
		return totalMarkers;
	}

	public void setTotalMarkers(int totalMarkers) {
		this.totalMarkers = totalMarkers;
	}

	public Drawing(String imgUrl,String name, String date, String time, int totalMarkers) {
		this.imgUrl = imgUrl;
		this.name = name;
		this.date = date;
		this.time = time;
		this.totalMarkers = totalMarkers;
	}
}
