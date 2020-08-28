package com.example.doda.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.doda.Room.MyappDatabse;
import com.example.doda.Room.Mydao;
import com.example.doda.model.Drawing;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRepository {

	private Mydao mydao;
	private Context context;
	private static MyRepository myRepositoryInstance;

	public static MyRepository getInstance()
	{
		if(myRepositoryInstance == null)
			myRepositoryInstance = new MyRepository();
		return myRepositoryInstance;
	}

	public MutableLiveData<List<Drawing>> getDrawings(Context context){
		MutableLiveData<List<Drawing>> drawingListMutableLiveData = new MutableLiveData<>();
		mydao = MyappDatabse.getDatabase(context).mydao();
		try {
			drawingListMutableLiveData.setValue(new getRoomDataAsyncTask(mydao).execute().get());
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return drawingListMutableLiveData;
	}

	private static class getRoomDataAsyncTask extends AsyncTask<Void, Void, List<Drawing> > {

		Mydao mydao;

		public getRoomDataAsyncTask(Mydao db) {
			this.mydao = db;
		}

		@Override
		protected List<Drawing> doInBackground(Void... voids) {
			return mydao.getDrawings();
		}

		@Override
		protected void onPostExecute(List<Drawing> drawings) {
			returnMethod(drawings);
		}

		public List<Drawing> returnMethod(List<Drawing> drawings) {
			return drawings;
		}
	}
}
