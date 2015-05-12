package br.com.marcosoft.apropriator.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class URLUtilsTest {

    @Test
    public void testEncode() {
        assertEquals("SUPDE-Atividade%20N%C3%A3o%20Software",
            WebUtils.encode("SUPDE-Atividade Não Software"));
    }

}
