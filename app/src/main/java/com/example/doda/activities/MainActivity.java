package com.example.doda.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.doda.R;
import com.example.doda.adapter.DrawingAdapter;
import com.example.doda.model.Drawing;
import com.example.doda.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	MainViewModel viewModel;
	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	List<Drawing> drawingList;
	DrawingAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		init();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

	private void initRecycler() {
		drawingList = new ArrayList<>();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new DrawingAdapter(MainActivity.this, drawingList);
		recyclerView.setAdapter(adapter);
	}

	private void init(){
		initRecycler();
		viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

		viewModel.init(this);

		viewModel.getRoomDrawings().observe(this, drawings -> {
			if(drawings != null){
				drawingList.addAll(drawings);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@OnClick(R.id.btn_add)
	public void addNewDrawing(){
		startActivity(new Intent(getApplicationContext(), AddDrawingActivity.class));
	}
}