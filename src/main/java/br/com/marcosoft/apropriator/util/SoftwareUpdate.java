package br.com.marcosoft.apropriator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import br.com.marcosoft.apropriator.util.WebUtils.Progress;



public class SoftwareUpdate {
    private static final String APROPRIATOR_RELEASES_LATEST =
    		"https://github.com/marcosalpereira/apropriator/releases/latest";

	private final Version currentVersion;

	private final String targetFolder;

	private final Progress progress;

    private SoftwareUpdate(Version currentVersion, String targetFolder, Progress progress) {
    	this.currentVersion = currentVersion;
    	this.targetFolder = targetFolder;
    	this.progress = progress;
    }

    public static void main(String[] args) throws IOException {
    	//update(new Version("1.0"), "/tmp/a");
    }

	public static Version update(Version currentVersion, String targetFolder, Progress progress)  {
		final SoftwareUpdate update = new SoftwareUpdate(currentVersion, targetFolder, progress);
		try {
			return update.doIt();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Version doIt() throws IOException {
		progressString("Apropriator v%s - Verificando se existe versão nova", currentVersion);

		final Version latestVersion = checkForNewVersion(currentVersion);

		if (latestVersion != null) {
			final String url = String.format(
					"https://github.com/marcosalpereira/apropriator/releases/download/v%s/binario.zip",
					latestVersion);

			progressString("Apropriator v%s - Atualizando para v%s ...", currentVersion, latestVersion);

	    	final String zipFile = targetFolder + File.separator +  "binario.zip";
			final OutputStream out = new FileOutputStream(zipFile);
	    	WebUtils.downloadFile(url, out, progress);

	    	unZipIt(zipFile, targetFolder);
	    	return latestVersion;
		}
		return null;

	}



	private void progressString(String template, Object... objects) {
		progress.setProgress(String.format(template, objects));
	}

	private void unZipIt(String zipFile, String outputFolder) throws IOException {

		final byte[] buffer = new byte[1024];


			final ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));

			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				final String fileName = ze.getName();
				final File newFile = new File(outputFolder + File.separator + fileName);

				progressString("Descompactando %s", newFile.getAbsoluteFile());

				final FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
	}

    public Version checkForNewVersion(Version currentVersion) {
        final String latestVersionStr = getLatestVersion();
        if (latestVersionStr == null) {
            return null;
        }

        final Version latestVersion = new Version(latestVersionStr);
		final boolean newVersion = latestVersion.compareTo(currentVersion) > 0;
        if (newVersion) {
        	return latestVersion;
        }
        return null;
    }

    private String getLatestVersion() {
    	final String latestVersionPage =
    			WebUtils.downloadFile(APROPRIATOR_RELEASES_LATEST, progress);
    	if (latestVersionPage == null) {
    		return null;
    	}
        final String searchStr = "/marcosalpereira/apropriator/releases/download/";
        final int ini = latestVersionPage.indexOf(searchStr) + searchStr.length();
        final int fim = latestVersionPage.indexOf('/', ini);
        if (fim <= ini) {
            return null;
        }
        return latestVersionPage.substring(ini + 1, fim);

    }

}
