package com.softmine.drpedia.profile.customlayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;

public class DatePickerEditText extends EditText {
    Context mContext;

    public DatePickerEditText(Context context) {
        super(context);
        init(context);
    }

    public DatePickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    void init(Context context) {
        mContext = context;
        setFocusable(false);
        setHint("Enter Date");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextAppearance(android.R.style.TextAppearance);
        } else {
            setTextAppearance(context, android.R.style.TextAppearance);
        }
        ButterKnife.bind(this);
    }



    @Override
    public boolean performClick() {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(mContext, listener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.setTitle("Select date");
        datepicker.show();
        return true;
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
            Calendar newDate = Calendar.getInstance();
            newDate.set(selectedyear, selectedmonth, selectedday);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            setText(dateFormatter.format(newDate.getTime()));
        }
    };
}
