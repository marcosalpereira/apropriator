package br.com.marcosoft.apropriator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Arguments {
	private File csvFile;
	private boolean update;
	
	public static Arguments parse(String args[]) {
		Arguments arguments = new Arguments();
		
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
	    
	    if (args.length > 1) {
	    	if ("update".equalsIgnoreCase(args[1].trim())) {
	    		arguments.setUpdate(true);
	    	} 
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

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	
	
}
