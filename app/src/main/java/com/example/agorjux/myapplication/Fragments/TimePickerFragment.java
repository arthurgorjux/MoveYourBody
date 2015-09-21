package com.example.agorjux.myapplication.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.agorjux.myapplication.CreateEventActivity;
import com.agorjux.moveyourbody.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by arthur on 09/06/15.
 */
public class TimePickerFragment extends DialogFragment  implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        WeakReference<CreateEventActivity> activity = new WeakReference<CreateEventActivity>((CreateEventActivity) getActivity());
        CreateEventActivity createEventActivity = activity.get();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedTime = sdf.format(c.getTime());
        if(createEventActivity != null){
            EditText dateInput = (EditText) createEventActivity.findViewById(R.id.hour_event);
            dateInput.setText(formattedTime);
        }
    }


}
