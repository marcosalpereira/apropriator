package br.com.marcosoft.apropriator.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Util {

    public static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public static final DateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String[] DIAS_SEMANA = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};

    /**
     * DD_MM_YYYY Date formatter não "Lenient".
     */
    public static final DateFormat DD_MM_YYYY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    static {
        DD_MM_YYYY_FORMAT.setLenient(false);
    }

    /**
     * DD_MM_YY Date formatter não "Lenient".
     */
    public static final DateFormat DD_MM_YY_FORMAT = new SimpleDateFormat("dd/MM/yy");
    static {
        DD_MM_YY_FORMAT.setLenient(false);
    }

    public static String formatDate(Date data) {
        return DD_MM_YYYY_FORMAT.format(data);
    }

    public static Date parseDate(DateFormat dateFormat, String value)
        throws IllegalArgumentException {
        if (value != null && !value.equals("") && !value.equals("-")) {
            final ParsePosition pos = new ParsePosition(0);
            final Date date = dateFormat.parse(value, pos);
            if (date != null && pos.getIndex() == value.length()) {
                return date;
            }
            throw new IllegalArgumentException("Data inválida.");
        }
        return null;
    }

    public static Calendar parseCalendar(DateFormat dateFormat, String value) {
        final Date date = parseDate(dateFormat, value);
        return toCalendar(date);

    }
    public static Calendar toCalendar(Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public static String formatMinutes(int minutes) {
        final int hor = minutes / 60;
        final int min = minutes - hor * 60;
        return String.format("%02d:%02d", hor, min);
    }

    public static String formatMinutesDecimal(double valor) {
        return DECIMAL_FORMAT.format(valor);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            return;
        }
    }

    public static boolean isSimilar(String str1, String str2) {
        final String str1SemSpc = str1.replaceAll("\\W", "").toLowerCase();
        final String str2SemSpc = str2.replaceAll("\\W", "").toLowerCase();
        if (str1SemSpc.equals(str2SemSpc))
            return true;
        for (int i = str1SemSpc.length() - 1; i > 15; i--) {
            final String substring = str1SemSpc.substring(0, i);
            if (str2SemSpc.startsWith(substring)) {
                return true;
            }
        }
        return false;
    }

    public static int getWeekDay(Date data) {
        final Calendar c = Calendar.getInstance();
        c.setTime(data);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static double parseDouble(String str, double errorValue) {
        try {
            return Double.parseDouble(str);
        } catch (final NumberFormatException e) {
            return errorValue;
        }
    }

    public static int parseInt(String str, int errorValue) {
        try {
            return Integer.parseInt(str, 10);
        } catch (final NumberFormatException e) {
            return errorValue;
        }
    }

    public static Calendar addDay(final Calendar date, int days) {
        final Calendar novaData = (Calendar) date.clone();
        novaData.add(Calendar.DAY_OF_MONTH, days);
        return novaData;
    }

	public static String nomeDiaSemana(int diaSemana) {
		return DIAS_SEMANA[diaSemana];
	}

    public static String getAppVersion() {
        final String ret = "?";
		final InputStream stream = Thread.currentThread().getClass()
				.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
        if (stream != null) {
            final Properties prop = new Properties();
            try {
                prop.load(stream);
                return prop.getProperty("version");
            } catch (final IOException e) {
            }
        }
        return ret;
    }
	
}
