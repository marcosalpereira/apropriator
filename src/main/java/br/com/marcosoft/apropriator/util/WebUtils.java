package br.com.marcosoft.apropriator.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

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
    	String string = 
    			downloadFile("https://github.com/marcosalpereira/apropriator/releases/latest");
    	
        final String search = "/marcosalpereira/apropriator/releases/download/";
        final int ini = string.indexOf(search) + search.length();
        final int fim = string.indexOf('/', ini);
        System.out.println(string.substring(ini + 1, fim)); 
//        System.out.println(encode("SUPDE-Atividade Não Software"));
    }
    
    public interface Progress {
    	void setProgress(int value);
    }
    
    public static String downloadFile(String fileUrl) {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	downloadFile(fileUrl, out, null);
    	return new String(out.toByteArray());
    }
    
    public static void downloadFile(String fileUrl, OutputStream outputStream, Progress progress) {
    	try {
    		
    		final URL url = new URL(fileUrl);
    		final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    		
    		urlConnection.connect();
    		
    		final InputStream inputStream = url.openStream();
    		
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            long totalBytesRead = 0;
            int percentCompleted = 0;
            long fileSize = urlConnection.getContentLength();
 
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                if (progress != null) {
                	totalBytesRead += bytesRead;
                	percentCompleted = (int) (totalBytesRead * 100 / fileSize);
                	progress.setProgress(percentCompleted);
                }
            }
            outputStream.close();
            inputStream.close();
            urlConnection.disconnect();
    		
    	} catch (final MalformedURLException e) {
    		e.printStackTrace();
    	} catch (final IOException e) {
    		e.printStackTrace();
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