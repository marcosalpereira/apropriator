package br.com.marcosoft.apropriator.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebUtils {

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
        final String string = downloadFile("https://github.com/marcosalpereira/apropriator/releases/latest");
        final String search = "/marcosalpereira/apropriator/releases/download/";
        final int ini = string.indexOf(search) + search.length();
        final int fim = string.indexOf('/', ini);
        System.out.println(string.substring(ini + 1, fim));
//        System.out.println(encode("SUPDE-Atividade Não Software"));
    }

    public static String downloadFile(String fileUrl) {
        try {

            final URL url = new URL(fileUrl);
            final URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            final InputStream input = url.openStream();

            final Reader reader = new InputStreamReader(input);
            final BufferedReader bufferedReader = new BufferedReader(reader);

            final StringBuilder sb = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            bufferedReader.close();
            input.close();

            return sb.toString();

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
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