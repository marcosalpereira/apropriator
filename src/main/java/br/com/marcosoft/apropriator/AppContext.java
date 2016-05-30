package br.com.marcosoft.apropriator;

import br.com.marcosoft.apropriator.model.ApropriationFile;
import br.com.marcosoft.apropriator.model.ApropriationFile.Config;
import br.com.marcosoft.apropriator.util.ApplicationProperties;

public class AppContext {
	private ApropriationFile apropriationFile;
	private ProgressInfo progressInfo;

	private ApplicationProperties applicationProperties;

	public ApropriationFile getApropriationFile() {
		return apropriationFile;
	}

	public ProgressInfo getProgressInfo() {
		return progressInfo;
	}

	public Config getConfig() {
		return apropriationFile.getConfig();
	}

	public void setApropriationFile(ApropriationFile apropriationFile) {
		this.apropriationFile = apropriationFile;
	}

	public void setProgressInfo(ProgressInfo progressInfo) {
		this.progressInfo = progressInfo;
	}

	public void setApplicationProperties(
			ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public ApplicationProperties getApplicationProperties() {
		return applicationProperties;
	}

}
