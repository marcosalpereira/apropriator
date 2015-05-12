package br.com.marcosoft.apropriator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.marcosoft.apropriator.util.Util;

public class TestUtil {

    @Test
    public final void testFormatMinutes() {
        assertEquals("02:00", Util.formatMinutes(120));
        assertEquals("00:10", Util.formatMinutes(10));
        assertEquals("00:09", Util.formatMinutes(9));
    }

    @Test
    public final void testIsSimilar() {
        assertTrue(
            Util.isSimilar("(p)- projeto", "(p) - projeto"));
        assertTrue(
            Util.isSimilar("(p)- projeto com nome menor que ante"
                , "(p) - projeto com nome menor que anterior"));
        assertTrue(
            Util.isSimilar("(p)- projeto ==> com dif sem ser letra/numero"
                , "(p)- projeto => com dif sem ser letra/numero"));
        assertTrue(
            Util.isSimilar("(p)- projeto ==> com dif sem ser letra/numero"
                , "(p)- projeto com dif sem ser letra/numero"));

        assertFalse(
            Util.isSimilar("(p)- projeto ==> com dif letra", "(p)- projet com dif letra"));
    }


}
