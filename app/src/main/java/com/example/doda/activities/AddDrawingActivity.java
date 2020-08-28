package com.example.doda.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.doda.R;
import com.example.doda.Room.MyappDatabse;
import com.example.doda.Room.Mydao;
import com.example.doda.model.Drawing;
import com.example.doda.model.MapPin;
import com.example.doda.util.PinView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDrawingActivity extends AppCompatActivity {

	private final int SELECT_IMAGE = 101;
	@BindView(R.id.btn_choose_img)
	Button addImg;
	@BindView(R.id.pin_image_view)
	PinView pinView;
	@BindView((R.id.txt_title))
	EditText title;
	@BindView(R.id.btn_add_pin)
	Button addPin;

	String currentTime;
	Date date;
	Mydao mydao;
	String currentDate;

	final ArrayList<MapPin> mapPinArrayList = new ArrayList<>();
	final ArrayList<MapPin> oldMapPinList = new ArrayList<>();
	HashMap<List<Float>, Boolean> lastPinsIds = new HashMap<>();
	float x = 0, y = 0;
	Bitmap bitmap = null;
	Drawing drawing;
	boolean isEdit;
	boolean isMarkerSaved = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_drawing);
		getSupportActionBar().hide();
		ButterKnife.bind(this);

		mydao = MyappDatabse.getDatabase(this).mydao();
		Intent intent = getIntent();

		isEdit = intent.getBooleanExtra("isEdit", false);
		int id = intent.getIntExtra("id", -1);

		if (isEdit) {
			if (id != -1) {
				drawing = mydao.getDrawingById(id);
				if (drawing != null) {
					mapPinArrayList.addAll(mydao.getPins((long) drawing.getId()));
					oldMapPinList.addAll(mapPinArrayList);
					bitmap = BitmapFactory.decodeByteArray(drawing.getBytes(), 0, drawing.getBytes().length);
					pinView.setImage(ImageSource.bitmap(bitmap));

					pinView.setPins(mapPinArrayList);
					title.setText(drawing.getName());
				}
			}
		}
		init();

	}

	@SuppressLint("ClickableViewAccessibility")
	private void init() {
		date = new Date();
		currentDate = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new Date()));
		Time time = new Time(date.getTime());
		currentTime = time.toString();

		pinView.setMaxScale(10f);
		pinView.setPins(mapPinArrayList);
		final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@RequiresApi(api = Build.VERSION_CODES.O)
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (pinView.isReady()) {
					PointF sCoord = pinView.viewToSourceCoord(e.getX(), e.getY());
					pinView.setPin(new PointF(sCoord.x, sCoord.y));
					pinView.setPins(mapPinArrayList);
					x = sCoord.x;
					y = sCoord.y;
					isMarkerSaved = false;
				}
				return true;
			}
		});

		pinView.setOnTouchListener((view, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));

		addImg.setOnClickListener(view -> {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
		});
	}

	private void showDialogBar() {
		final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
		final EditText name = dialogView.findViewById(R.id.txt_person_name);
		final EditText message = (EditText) dialogView.findViewById(R.id.edt_comment);
		Button btnSubmit = (Button) dialogView.findViewById(R.id.buttonSubmit);
		Button btnCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

		btnCancel.setOnClickListener(view -> {
			pinView.setPin(null);
			pinView.setPins(mapPinArrayList);
			x = 0;
			y = 0;
			dialogBuilder.dismiss();
		});
		btnSubmit.setOnClickListener(view -> {
			// DO SOMETHINGS
			if (checkDialogTextValidity(name, message)) {
				final List<Float> floatList = new ArrayList<>();
				floatList.add(x);
				floatList.add(y);
				if (!lastPinsIds.containsKey(floatList)) {
					lastPinsIds.put(floatList, true);
					mapPinArrayList.add(new MapPin(x, y, message.getText().toString(), name.getText().toString().trim(), currentDate, currentTime));
					pinView.setPins(mapPinArrayList);
					x = 0;
					y = 0;
					isMarkerSaved = true;
				}

				dialogBuilder.dismiss();
			}
		});

		dialogBuilder.setView(dialogView);
		dialogBuilder.show();
	}

	private boolean checkDialogTextValidity(EditText name, EditText message) {
		if (name.getText().toString().trim().isEmpty()) {
			name.setError("Please fill this box");
			return false;
		} else if (message.getText().toString().trim().isEmpty()) {
			message.setError("Please fill this box");
			return false;
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {

					try {
						bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
					} catch (IOException e) {
						e.printStackTrace();
					}

					pinView.setImage(ImageSource.bitmap(bitmap));
					mapPinArrayList.clear();
					pinView.setPin(null);
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@OnClick(R.id.btn_add_pin)
	public void addPin(View view) {
		if (x != 0 && y != 0) {
			showDialogBar();
		} else {
			Toast.makeText(this, "Please add a new marker", Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.btn_save_drawing)
	public void saveDrawing() {
		if (checkValidity()) {
			if (isMarkerSaved) {
				if (!isEdit) {
					new insertDrawing(AddDrawingActivity.this, mydao, getbytesFromBitmap(), title.getText().toString().trim(), currentDate, currentTime, mapPinArrayList).execute();
				} else {
					drawing.setBytes(getbytesFromBitmap());
					drawing.setName(title.getText().toString().trim());
					drawing.setDate(currentDate);
					drawing.setTime(currentTime);
					drawing.setTotalMarkers(mapPinArrayList.size());
					mydao.updateDrawing(drawing);
					mapPinArrayList.removeAll(oldMapPinList);
					for (MapPin mapPin : mapPinArrayList) {
						mapPin.setDrawingId(drawing.getId());
						mydao.addPins(mapPin);
					}
					Toast.makeText(this, "Drawing Updated", Toast.LENGTH_SHORT).show();
				}
				finish();
			} else {
				Toast.makeText(this, "Please save the marker.", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private boolean checkValidity() {
		if (bitmap == null) {
			Toast.makeText(this, "Please select the image", Toast.LENGTH_SHORT).show();
			return false;
		} else if (title.getText().toString().trim().isEmpty()) {
			title.setError("Please fill this box");
			return false;
		}
		return true;
	}

	private byte[] getbytesFromBitmap() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		bitmap.recycle();
		return byteArray;
	}

	private static class insertDrawing extends AsyncTask<Void, Void, Void> {

		byte[] bytes;
		String title;
		String currentDate;
		String time;
		ArrayList<MapPin> mapPinArrayList;
		Mydao mydao;
		@SuppressLint("StaticFieldLeak")
		AddDrawingActivity context;

		public insertDrawing(AddDrawingActivity addDrawingActivity, Mydao maydao, byte[] bytes, String title, String currentDate, String time, ArrayList<MapPin> mapPinArrayList) {
			context = addDrawingActivity;
			this.mydao = maydao;
			this.bytes = bytes;
			this.title = title;
			this.currentDate = currentDate;
			this.time = time;
			this.mapPinArrayList = mapPinArrayList;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			Drawing drawing = new Drawing(bytes, title, currentDate, time, mapPinArrayList.size());

			long drawingId = mydao.addDrawing(drawing);

			for (MapPin mapPin : mapPinArrayList) {
				mapPin.setDrawingId(drawingId);
				mydao.addPins(mapPin);
			}
			Toast.makeText(context, "New Drawing Added", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
}