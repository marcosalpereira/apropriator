package br.com.marcosoft.apropriator.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import br.com.marcosoft.apropriator.util.WebUtils.Progress;



public class SoftwareUpdate extends JFrame implements Progress {
    private static final String APROPRIATOR_RELEASES_LATEST = 
    		"https://github.com/marcosalpereira/apropriator/releases/latest";

    private static final long serialVersionUID = -6989750848125302233L;

    private JPanel jp;

    private JProgressBar progressBar;


    public SoftwareUpdate() {
        initComponents();
    }

	private void initComponents() {

        final GridBagLayout thisLayout1 = new GridBagLayout();
        thisLayout1.columnWidths = new int[] {7};
        thisLayout1.rowHeights = new int[] {7};
        thisLayout1.columnWeights = new double[] {0.1};
        thisLayout1.rowWeights = new double[] {0.1};
        getContentPane().setLayout(thisLayout1);

        {
            jp = new JPanel();

            final Color blueBackGround = new Color(58,71,106);
            jp.setBorder(new LineBorder(new java.awt.Color(230,230,250), 3, true));
            jp.setBackground(blueBackGround);

            final GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.columnWeights = new double[] {0.1};
            thisLayout.rowWeights = new double[] {0.1};
            thisLayout.rowHeights = new int[] {7};
            thisLayout.columnWidths = new int[] {109};
            jp.setLayout(thisLayout);

            getContentPane().add(jp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

            {
                progressBar = new JProgressBar(0, 100);
                jp.add(progressBar, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                progressBar.setBackground(java.awt.SystemColor.text);
                progressBar.setOpaque(true);
                progressBar.setSize(395, 20);
                progressBar.setPreferredSize(new java.awt.Dimension(0, 20));
                progressBar.setStringPainted(true);
                progressBar.setFont(new java.awt.Font("Dialog",Font.BOLD,16));
                
            }

            jp.setOpaque(true);
        }

        final MoveMouseListener mml = new MoveMouseListener(jp);
        jp.addMouseListener(mml);
        jp.addMouseMotionListener(mml);
        jp.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.setUndecorated(true);
        this.pack();
        this.setSize(751, 30);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setScreenPosition();

        AWTUtilitiesWrapper.setOpacity(this);
        this.setAlwaysOnTop(true);
        setVisible(true);
    }

    private void setScreenPosition() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeight = screenSize.height;

        this.setLocation(screenWidth - this.getWidth(), screenHeight - this.getHeight());
    }

    public static void main(String[] args) throws IOException {
    	update(new Version("1.0"), "/tmp/a");
    }
	
	public static void update(Version currentVersion, String targetFolder) throws IOException {
		SoftwareUpdate update = new SoftwareUpdate();
		update.progressString("Apropriator v%s - Verificando se existe versão nova", currentVersion);
		
		Version latestVersion = checkForNewVersion(currentVersion);
	
		if (latestVersion != null) {
			String url = String.format(
					"https://github.com/marcosalpereira/apropriator/releases/download/v%s/binario.zip", 
					latestVersion);
			
			update.progressString("Apropriator v%s - Atualizando para v%s ...", currentVersion, latestVersion);
			
	    	String zipFile = targetFolder + File.separator +  "binario.zip";
			OutputStream out = new FileOutputStream(zipFile);			
	    	WebUtils.downloadFile(url, out, update);
	    	
	    	update.unZipIt(zipFile, targetFolder);
	    	
	    	update.progressBar.setForeground(Color.red);
	    	update.progressString("Versão Atualizada. Será usada somente na próxima execução");
	    	Util.sleep(3000);
		}
		update.dispose();
	}
	
	private void progressString(String template, Object... objects) {
		progressBar.setString(String.format(template, objects));
	}

	private void unZipIt(String zipFile, String outputFolder) throws IOException {

		byte[] buffer = new byte[1024];

		
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));

			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				progressString("Descompactando %s", newFile.getAbsoluteFile());

				FileOutputStream fos = new FileOutputStream(newFile);

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
	
    private static Version checkForNewVersion(Version currentVersion) {
        final String latestVersionStr = getLatestVersion();
        if (latestVersionStr == null) {
            return null;
        }

        Version latestVersion = new Version(latestVersionStr);
		final boolean newVersion = latestVersion.compareTo(currentVersion) > 0;
        if (newVersion) {
        	return latestVersion;
        }
        return null;
    }

    private static String getLatestVersion() {
    	final String latestVersionPage = 
    			WebUtils.downloadFile(APROPRIATOR_RELEASES_LATEST);
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
    
    public void setProgress(int value) {
    	progressBar.setValue(value);
    }
    
}
