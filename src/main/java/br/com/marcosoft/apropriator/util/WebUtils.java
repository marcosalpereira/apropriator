package br.com.marcosoft.apropriator.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class WebUtils {
	private static final int BUFFER_SIZE = 4096;

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            // HttpURLConnection.setInstanceFollowRedirects(false)
            final HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                .openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
    }

    public interface Progress {
    	void setProgress(String progress);
    	void setProgress(int value);
		void finished();
    }

    public static String downloadFile(String fileUrl, Progress progress) {
    	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    	downloadFile(fileUrl, out, progress);
    	return new String(out.toByteArray());
    }

    public static void downloadFile(String fileUrl, OutputStream outputStream, Progress progress) {
    	try {

    		final URL url = new URL(fileUrl);
    		final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

    		urlConnection.connect();

    		final InputStream inputStream = url.openStream();

            final byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            long totalBytesRead = 0;
            int percentCompleted = 0;
            final long fileSize = urlConnection.getContentLength();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);

                totalBytesRead += bytesRead;
                percentCompleted = (int) (totalBytesRead * 100 / fileSize);
                progress.setProgress(percentCompleted);

            }
            outputStream.close();
            inputStream.close();
            urlConnection.disconnect();

    	} catch (final MalformedURLException e) {
    		e.printStackTrace();
    	} catch (final IOException e) {
    		e.printStackTrace();
    	} finally {
    		progress.finished();
    	}

    }

    public static String encode(String url) {

            try {
                return URLEncoder.encode(url, "UTF-8").replaceAll("\\+", "%20");
            } catch (final UnsupportedEncodingException e) {
                return url;
            }
        }
}