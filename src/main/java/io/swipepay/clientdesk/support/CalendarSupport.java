package io.swipepay.clientdesk.support;

import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CalendarSupport {
	public String MONTH_OBJ = "monthObj";
	public String MONTH_STR = "monthStr";
	public String MONTH_STR_ABBR = "monthStrAbbr";
	public String MONTH_INT = "monthInt";
	
	public String YEAR_OBJ = "yearObj";
	public String YEAR_STR = "yearStr";
	public String YEAR_INT = "yearInt";
	
	public List<Map<String, Object>> listFutureYearsFromNow(Integer numberOfYears) {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		
		Year nowYr = Year.now();
		for (int y = 0; y < numberOfYears; y++) {
			Year futureYr = nowYr.plusYears(y);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(YEAR_OBJ, futureYr);
			map.put(YEAR_STR, futureYr.toString());
			map.put(YEAR_INT, StringUtils.substring(futureYr.toString(), 2));
			results.add(map);
		}
		return results;
	}
	
	public List<Map<String, Object>> listMonthsOfTheYear() {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		
		for (int m = 1; m <= 12; m++) {
			Month month = Month.of(m);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(MONTH_OBJ, month);
			map.put(MONTH_STR, month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
			map.put(MONTH_STR_ABBR, month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
			map.put(MONTH_INT, String.format("%02d", m));
			results.add(map);
		}
		return results;
	}
}