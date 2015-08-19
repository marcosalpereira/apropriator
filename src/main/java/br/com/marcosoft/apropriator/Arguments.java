package br.com.marcosoft.apropriator;

import java.io.File;
import java.util.Arrays;

public class Arguments {
	private File csvFile;

	public static Arguments parse(String args[]) {
		final Arguments arguments = new Arguments();

		System.out.println("Arguments:" + Arrays.asList(args));

		String nomeCsv = null;
	    if (args.length > 0) {
	    	nomeCsv = args[0];
	    }
		if (nomeCsv == null) {
	    	throw new IllegalStateException("Arquivo CSV não informado!");
	    }
	    final File file = new File(nomeCsv);
	    if (!file.canRead()) {
	    	throw new IllegalStateException("Arquivo " + nomeCsv + " não pode ser lido!");
	    }
	    arguments.setCsvFile(file);

	    return arguments;
	}

	public File getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}


}
