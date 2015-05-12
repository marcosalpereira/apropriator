package br.com.marcosoft.apropriator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import br.com.marcosoft.apropriator.model.ApropriationFile;

public class ApropriationFileParserTest {

    @Test
    public void testParseProjectsQtdParametrosInvalido() {
        try {
            parse("/parse/projects/qtdParametrosInvalido.csv");
        } catch (final IOException e) {
            assertEquals(
                "Erro lendo os projetos: Quantidade de campos difere da esperada!",
                e.getMessage());
        }
    }

    @Test
    public void testParseConfigQtdParametrosInvalido() {
        try {
            parse("/parse/config/qtdParametrosInvalido.csv");
        } catch (final IOException e) {
            assertEquals(
                "Erro lendo as configurações da planilha. Quantidade de campos difere da esperada!",
                e.getMessage());
        }
    }

    @Test
    public void testParseConfigAppProperty() throws IOException {
        final ApropriationFile apropriationFile = parse("/parse/config/appProperty.csv");
        assertEquals("123", apropriationFile.getConfig().getCpf());
    }

    @Test
    public void testParseConfigSysProperty() throws IOException {
        final String sysProperty = "sys.property";
        System.clearProperty(sysProperty);
        assertNull(System.getProperty(sysProperty));

        parse("/parse/config/sysProperty.csv");
        assertEquals("valor", System.getProperty(sysProperty));
    }

    private ApropriationFile parse(String arquivoCsv) throws IOException {
        final ApropriationFileParser fileParser =
            new ApropriationFileParser(getFile(arquivoCsv));
        return fileParser.parse();
    }

    private File getFile(String filename) {
        final String fullname = this.getClass().getResource(filename).getFile();
        return new File(fullname);
    }

}
