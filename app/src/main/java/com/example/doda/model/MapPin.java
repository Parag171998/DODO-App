package com.example.doda.model;

import android.graphics.PointF;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MapPin {

	float X, Y;
	@PrimaryKey(autoGenerate = true)
	int id;


	@ColumnInfo
	long drawingId;

	@ColumnInfo
	String message;
	@ColumnInfo
	String personName;

	@ColumnInfo
	String date;
	@ColumnInfo
	String time;

	public MapPin(float x, float y, String message, String personName, String date, String time) {
		X = x;
		Y = y;
		this.message = message;
		this.personName = personName;
		this.date = date;
		this.time = time;
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

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public MapPin(float X, float Y) {
		this.X = X;
		this.Y = Y;
	}

	public long getDrawingId() {
		return drawingId;
	}

	public void setDrawingId(long drawingId) {
		this.drawingId = drawingId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MapPin() {
	}

	public float getX() {
		return X;
	}

	public void setX(float X) {
		this.X = X;
	}

	public float getY() {
		return Y;
	}

	public void setY(float Y) {
		this.Y = Y;
	}

	@Ignore
	public PointF getPoint() {
		return new PointF(this.X, this.Y);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
