package uy.com.demente.ideas.wallets.util;

import java.util.Date;

import org.joda.time.DateTime;

/**
 * @author 1987diegog
 */
public class DateUtils {

	/**
	 * Returns a given date by parameters with the hour set in 00:00:00.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateFrom(Date date) {
		DateTime dateTimeDesde = new DateTime(date).withTime(0, 0, 0, 0);
		return dateTimeDesde.toDate();
	}

	/**
	 * Returns a given date by parameters with the hour set in 23:59:59.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateTo(Date date) {
		DateTime dateTimeHasta = new DateTime(date).withTime(23, 59, 59, 999);
		return dateTimeHasta.toDate();
	}
}
