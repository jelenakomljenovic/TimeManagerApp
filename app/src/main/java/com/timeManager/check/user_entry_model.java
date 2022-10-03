package com.timeManager.check;

import java.text.SimpleDateFormat;
import java.util.Date;

public class user_entry_model {

    public user_entry_model(String in, String out_time) {
        this.in_date = getFormattedDate(in);
        this.in = "In : " + getFormattedTime(in);
        if (out_time.equals("null")) {
            this.out_time = "Out : Still In";
        } else {
            if (getFormattedDate(in).equals(getFormattedDate(out_time))) {
                this.out_time = "Out : " + getFormattedTime(out_time);
            } else {
                this.out_time = "Out : " + getFormattedDate(out_time) + " at " + getFormattedTime(out_time);
            }
        }
    }

    private String getFormattedTime(String time) {
        try {
            SimpleDateFormat stf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date Time = stf.parse(time);
            SimpleDateFormat outputsdf = new SimpleDateFormat("HH:mm:ss");
            String output = outputsdf.format(Time);
            return output;
        } catch (java.text.ParseException e) {
            return time;
        }
    }

    private String getFormattedDate(String Date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date date = sdf.parse(Date);
            SimpleDateFormat outputsdf = new SimpleDateFormat("EEE, dd MMM yyyy");
            String output = outputsdf.format(date);
            return output;
        } catch (java.text.ParseException e) {
            return Date;
        }
    }

    String in_date;
    String in;
    String out_time;
}
