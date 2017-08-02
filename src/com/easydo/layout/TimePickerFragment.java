package com.easydo.layout;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		return new EasyDoTimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()), "—°‘Ò ±º‰");
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

	}
}
