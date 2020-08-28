package com.example.doda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.doda.R;
import com.example.doda.Room.MyappDatabse;
import com.example.doda.model.MapPin;
import com.example.doda.util.PinView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawingDetailsAvtivity extends AppCompatActivity {

	@BindView(R.id.txt_title)
	TextView title;
	@BindView(R.id.pin_image_view)
	PinView pinView;
	@BindView(R.id.txtDate)
	TextView date;
	@BindView(R.id.txtTime)
	TextView time;
	@BindView(R.id.txt_marker_count)
	TextView count;
	ArrayList<MapPin> mapPinArrayList = new ArrayList<>();

	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawing_details_avtivity);
		ButterKnife.bind(this);
		getSupportActionBar().hide();

		Intent intent = getIntent();

		init(intent);

	}

	@SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
	private void init(Intent intent) {
		title.setText(intent.getStringExtra("title"));
		byte[] bytes = intent.getByteArrayExtra("byteArray");

		if(bytes != null) {
			pinView.setMaxScale(10f);
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			pinView.setImage(ImageSource.bitmap(bitmap));

			date.setText("Created at: " + intent.getStringExtra("date"));
			time.setText("Time: " + intent.getStringExtra("time"));
			count.setText(count.getText() + String.valueOf(intent.getIntExtra("pinCount", -1)));

			id = intent.getIntExtra("id", -1);
			if (id != -1) {
				mapPinArrayList.addAll(MyappDatabse.getDatabase(this).mydao().getPins(id));
				pinView.setPins(mapPinArrayList);
			}

			final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {
					if (pinView.isReady()) {
						PointF sCoord = pinView.viewToSourceCoord(e.getX(), e.getY());
						int i = pinView.getPinIdByPoint(sCoord);
						if (i != -1) {
							MapPin mapPin = getMapPinById(i);
							if (mapPin != null) {
								showBottomSheet(mapPin);
							}
						}
					}
					return true;
				}
			});

			pinView.setOnTouchListener((view, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));
		}

	}

	private MapPin getMapPinById(int i) {
		for (MapPin mapPin : mapPinArrayList) {
			if (mapPin.getId() == i) {
				return mapPin;
			}
		}
		return null;
	}

	@OnClick(R.id.btnEdit)
	public void openEditDrawingScreen(){
		Intent intent = new Intent(getApplicationContext(), AddDrawingActivity.class);
		intent.putExtra("isEdit", true);
		intent.putExtra("id", id);
		startActivity(intent);
		finish();
	}
	@SuppressLint("SetTextI18n")
	private void showBottomSheet(MapPin mapPin) {
		final BottomSheetDialog dialog = new BottomSheetDialog(DrawingDetailsAvtivity.this);
		dialog.setContentView(R.layout.bottom_sheet);
		TextView name= dialog.findViewById(R.id.txt_person_name);
		TextView message = dialog.findViewById(R.id.edt_comment);
		TextView date = dialog.findViewById(R.id.txt_date);
		TextView time = dialog.findViewById(R.id.txt_time);
		Button cancelBtn = dialog.findViewById(R.id.buttonCancel);

		name.setText("Created By: " + mapPin.getPersonName());
		message.setText("Message: " + mapPin.getMessage());
		date.setText("Date: " + mapPin.getDate());
		time.setText("Time: " + mapPin.getTime());

		assert cancelBtn != null;
		cancelBtn.setOnClickListener(view -> dialog.dismiss());
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

}