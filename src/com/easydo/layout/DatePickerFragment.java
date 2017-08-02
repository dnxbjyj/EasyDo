package com.easydo.layout;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.app.DatePickerDialog;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return new EasyDoDatePickerDialog(getActivity(), this, year, month,
				day, "Ñ¡ÔñÈÕÆÚ", EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

	}

}
