package com.example.agorjux.myapplication.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.agorjux.myapplication.CreateEventActivity;
import com.agorjux.moveyourbody.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by arthur on 09/06/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        WeakReference<CreateEventActivity> activity = new WeakReference<CreateEventActivity>((CreateEventActivity) getActivity());
        CreateEventActivity createEventActivity = activity.get();
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(c.getTime());
        if(createEventActivity != null){
            EditText dateInput = (EditText) createEventActivity.findViewById(R.id.date_event);
            dateInput.setText(formattedDate);
        }
    }
}
