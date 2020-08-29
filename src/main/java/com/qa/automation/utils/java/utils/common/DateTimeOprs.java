package com.qa.automation.utils.java.utils.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeOprs {
	
	public DateTimeOprs() {

	}

	public String getCurrentTime(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}
}
