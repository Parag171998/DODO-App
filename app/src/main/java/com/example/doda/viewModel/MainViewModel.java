package com.example.doda.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doda.model.Drawing;
import com.example.doda.repository.MyRepository;
import com.example.doda.model.MapPin;

import java.util.List;

public class MainViewModel extends ViewModel {
	private MutableLiveData<List<MapPin>> mapPinMutableList = null;
	private MutableLiveData<List<Drawing>> drawingMutableList = null;

	private MyRepository myRepository;

	public void init(Context context)
	{
		if(drawingMutableList != null)
			return;

		myRepository = MyRepository.getInstance();
		drawingMutableList = myRepository.getDrawings(context);
	}

	public LiveData<List<MapPin>> getPins()
	{
		return mapPinMutableList;
	}

	public LiveData<List<Drawing>> getRoomDrawings(){
		return drawingMutableList;
	}
}
