package br.com.marcosoft.apropriator;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.marcosoft.lib.ApplicationProperties;


public class ApplicationPropertiesTest {

    @Test
    public void testApplicationProperties() throws IOException {
        final ApplicationProperties app = new ApplicationProperties("test");
        app.setProperty("key", "value");

        final String fileName = System.getProperty("user.home") + File.separator
                + ".test" + File.separator + "application.properties";
        final File file = new File(fileName);
        final BufferedReader input = new BufferedReader(new FileReader(file));

        String line = null;
        while ((line = input.readLine()) != null) {
            if (line.startsWith("key="))
                break;
        }
        input.close();

        assertEquals("key=value", line);
        assertEquals("value", app.getProperty("key"));

    }

}
