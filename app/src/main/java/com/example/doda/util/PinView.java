package com.example.doda.util;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.doda.R;
import com.example.doda.model.MapPin;

import java.util.ArrayList;

public class PinView extends SubsamplingScaleImageView {

	private PointF sPin;
	private final PointF vPin = new PointF();
	ArrayList<MapPin> mapPins;
	ArrayList<DrawPin> drawnPins;
	Context context;
	private Bitmap pin;


	public PinView(Context context) {
		this(context, null);
		this.context = context;
	}

	public PinView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
		initialise();
	}

	public void setPins(ArrayList<MapPin> mapPins) {
		this.mapPins = mapPins;
		initialise();
		invalidate();
	}

	public void setPin(PointF pin) {
		this.sPin = pin;
		initialise();
		invalidate();
	}

	public PointF getPin() {
		return sPin;
	}

	private void initialise() {
		float density = getResources().getDisplayMetrics().densityDpi;

		pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_green);

		float w = (density / 1420f) * pin.getWidth();

		float h = (density / 1420f) * pin.getHeight();

		pin = Bitmap.createScaledBitmap(pin, (int) w, (int) h, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Don't draw pin before image is ready so it doesn't move around during       setup.
		if (!isReady()) {
			return;
		}

		drawnPins = new ArrayList<>();

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		float density = getResources().getDisplayMetrics().densityDpi;

		if (sPin != null && pin != null) {
			sourceToViewCoord(sPin, vPin);
			float vX = vPin.x - (pin.getWidth() / 2);
			float vY = vPin.y - pin.getHeight();
			canvas.drawBitmap(pin, vX, vY, paint);
		}

		for (int i = 0; i < mapPins.size(); i++) {
			MapPin mPin = mapPins.get(i);
			//Bitmap bmpPin = Utils.getBitmapFromAsset(context, mPin.getPinImgSrc());
			Bitmap bmpPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_green);
			float w = (density / 1420f) * bmpPin.getWidth();
			float h = (density / 1420f) * bmpPin.getHeight();
			bmpPin = Bitmap.createScaledBitmap(bmpPin, (int) w, (int) h, true);

			sourceToViewCoord(mPin.getPoint(), vPin);
			//in my case value of point are at center point of pin image, so we need to adjust it here

			float vX = vPin.x - (bmpPin.getWidth() / 2);
			float vY = vPin.y - bmpPin.getHeight();


			canvas.drawBitmap(bmpPin, vX, vY, paint);

			//add added pin to an Array list to get touched pin
			DrawPin dPin = new DrawPin();
			dPin.setStartX(mPin.getX() - w / 2);
			dPin.setEndX(mPin.getX() + w / 2);
			dPin.setStartY(mPin.getY() - h / 2);
			dPin.setEndY(mPin.getY() + h / 2);
			dPin.setId(mPin.getId());
			drawnPins.add(dPin);
		}
	}

	public int getPinIdByPoint(PointF point) {

		for (int i = drawnPins.size() - 1; i >= 0; i--) {
			DrawPin dPin = drawnPins.get(i);
			if (point.x >= dPin.getStartX() && point.x <= dPin.getEndX()) {
				if (point.y >= dPin.getStartY() && point.y <= dPin.getEndY()) {
					return dPin.getId();
				}
			}
		}
		return -1; //negative no means no pin selected
	}
}
