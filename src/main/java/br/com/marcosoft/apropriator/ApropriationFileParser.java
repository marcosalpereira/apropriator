package br.com.marcosoft.apropriator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.marcosoft.apropriator.model.ApropriationFile;
import br.com.marcosoft.apropriator.model.ItemTrabalho;
import br.com.marcosoft.apropriator.model.Task;
import br.com.marcosoft.apropriator.model.TaskRecord;
import br.com.marcosoft.apropriator.util.CharsetDetector;
import br.com.marcosoft.apropriator.util.Util;

public class ApropriationFileParser {
    //Posicao do tipo de registro
    private static final int POS_TIPO_REGISTRO = 0;

    //Tipos de registro
    private static final String TR_ATIVIDADE = "reg";
    private static final String TR_CONFIG = "cfg";
    private static final String TR_PROJETO = "prj";
    private static final String TR_CAPTURE = "cap";

    //Registros de configuracao
    private static final int POS_CFG_VALOR_PROPRIEDADE = 2;
    private static final int POS_CFG_PROPRIEDADE = 1;
    private static final int CFG_QUANTIDADE_CAMPOS = 3;

    //Registros de registro de atividade
    private static final int POS_REG_NUMERO_LINHA = 1;
    private static final int POS_REG_REGISTRADO = 2;
    private static final int POS_REG_DATA = 3;
    private static final int POS_REG_FINALIZAR = 4;
    private static final int POS_REG_CONTEXTO = 5;
    private static final int POS_REG_ITEM_TRABALHO = 6;
    private static final int POS_REG_COMENTARIO = 7;
    private static final int POS_REG_HORA_INICIO = 8;
    private static final int POS_REG_HORA_TERMINO = 9;
    private static final int POS_REG_DURACAO_MIN = 10;
    @SuppressWarnings("unused")
    private static final int POS_REG_DURACAO_HOR = 11;
    @SuppressWarnings("unused")
    private static final int POS_REG_DURACAO_HOR_DEC = 12;
    @SuppressWarnings("unused")
    private static final int POS_REG_DIA_SEMANA = 13;
    @SuppressWarnings("unused")
    private static final int POS_REG_SEMANA = 14;

    private static final int REG_QUANTIDADE_CAMPOS = 15;

    private final File inputFile;

    private int numeroLinha;

    public ApropriationFileParser(final File inputFile) {
        this.inputFile = inputFile;
    }

    public ApropriationFile parse() throws IOException {
        final ApropriationFile ret = new ApropriationFile(this.inputFile);

        final BufferedReader input = getReader(this.inputFile);

        try {
            String line = null;
            while ((line = input.readLine()) != null) {
                parseLine(ret, line);
            }
        }
        finally {
            input.close();
        }


        return ret;
    }

    private void parseLine(final ApropriationFile ret, String line) throws IOException {
        final String[] fields = line.split("\\|", -1);
        if (TR_CONFIG.equals(fields[POS_TIPO_REGISTRO])) {
            parseConfig(ret, fields);

        } else if (TR_PROJETO.equals(fields[POS_TIPO_REGISTRO])) {
            //NOP

        } else if (TR_ATIVIDADE.equals(fields[POS_TIPO_REGISTRO])) {
            parseAtividade(ret, fields);

        } else if (TR_CAPTURE.equals(fields[POS_TIPO_REGISTRO])) {
            //NOP
        }
    }

    private void parseAtividade(final ApropriationFile ret, final String[] fields)
        throws IOException {
        if (fields.length != REG_QUANTIDADE_CAMPOS) {
            throw new IOException(
                "Erro lendo as atividades. Quantidade de campos difere da esperada!");
        }
        final int duracao = Integer.parseInt(fields[POS_REG_DURACAO_MIN], 10);
        if (duracao != 0) {
            numeroLinha =  Integer.parseInt(fields[POS_REG_NUMERO_LINHA], 10);
            final TaskRecord taskRecord = new TaskRecord();
            taskRecord.setData(Util.parseDate(Util.DD_MM_YY_FORMAT, fields[POS_REG_DATA].substring(0, 8)));
            taskRecord.setRegistrado(!"Não".equals(fields[POS_REG_REGISTRADO]));
            taskRecord.setDuracao(duracao);
            taskRecord.setFinalizar("Sim".equals(fields[POS_REG_FINALIZAR]));
            taskRecord.setNumeroLinha(numeroLinha);
            taskRecord.setTask(parseTask(fields));
            taskRecord.setHoraInicio(parseHora(fields[POS_REG_HORA_INICIO]));
            taskRecord.setHoraTermino(parseHora(fields[POS_REG_HORA_TERMINO]));

            ret.adicionarTasksRecord(taskRecord);
        }
    }

    private String parseHora(String hm) {
        if (hm == null || !hm.trim().matches("[0-9][0-9][.:][0-9][0-9]")) {
            throw new IllegalStateException(
                String.format("Linha %d:Hora Inválida:", numeroLinha+1, hm));
        }
        return hm;
    }

    private void parseConfig(final ApropriationFile ret, final String[] fields)
        throws IOException {
        if (fields.length != CFG_QUANTIDADE_CAMPOS) {
            throw new IOException(
                "Erro lendo as configurações da planilha. Quantidade de campos difere da esperada!");
        }
        ret.getConfig().setProperty(
            fields[POS_CFG_PROPRIEDADE].trim(), fields[POS_CFG_VALOR_PROPRIEDADE].trim());
    }

    private BufferedReader getReader(File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final String charset = CharsetDetector.detect(file);
        final InputStreamReader inputStreamReader;
        if (charset != null) {
            inputStreamReader = new InputStreamReader(inputStream, charset);
        } else {
            inputStreamReader = new InputStreamReader(inputStream);
        }
        return new BufferedReader(inputStreamReader);
    }

    private Task parseTask(String[] fields) throws IOException {
        final String[] dados = fields[POS_REG_ITEM_TRABALHO].split(";");
        if (dados.length < 2) {
            throw new IOException(
                "Erro lendo Item Trabalho ALM: Quantidade de campos difere da esperada!");
        }
        final ItemTrabalho itemTrabalho;
        try {
            itemTrabalho = new ItemTrabalho(Integer.parseInt(dados[0], 10), dados[1]);
        } catch (final NumberFormatException e) {
            throw new IOException(
                "Erro convertendo id do item de trabalho: Primeiro campo tem que ser um numero, foi encontrado "
                    + fields[POS_REG_ITEM_TRABALHO]);
        }

        final String contexto = fields[POS_REG_CONTEXTO].trim();
        if (contexto.isEmpty()) {
            throw new IllegalStateException("Contexto não pode ser vazio");
        }

        return new Task(contexto, itemTrabalho, fields[POS_REG_COMENTARIO]);
    }

}
