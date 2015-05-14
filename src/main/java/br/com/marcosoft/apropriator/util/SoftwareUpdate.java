package br.com.marcosoft.apropriator.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.JTextComponent;

import br.com.marcosoft.apropriator.util.WebUtils.Progress;



public class SoftwareUpdate extends JFrame implements Progress {
    private static final String APROPRIATOR_RELEASES_LATEST = 
    		"https://github.com/marcosalpereira/apropriator/releases/latest";

    private static final long serialVersionUID = -6989750848125302233L;

    private JLabel lblTitle;
    private JLabel lblMessage;
    
    private JPanel jPanel1;
    private JPanel jp;

    private JLabel labelProgress;
    private JProgressBar progressBar;


    public SoftwareUpdate(String title, String message) {
        initComponents(title, message);
    }

    private void initComponents(String title, String message) {

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
            thisLayout.columnWeights = new double[] {0.0, 0.7};
            thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.2};
            thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7};
            thisLayout.columnWidths = new int[] {109};
            jp.setLayout(thisLayout);


            getContentPane().add(jp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            {
            	jPanel1 = new JPanel();
            	final GridBagLayout jPanel1Layout = new GridBagLayout();
            	jp.add(jPanel1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 15, 10), 0, 0));
            	jPanel1Layout.rowWeights = new double[] {0.1, 0.1};
            	jPanel1Layout.rowHeights = new int[] {7, 7};
            	jPanel1Layout.columnWeights = new double[] {0.1};
            	jPanel1Layout.columnWidths = new int[] {7};
            	jPanel1.setLayout(jPanel1Layout);
            	jPanel1.setBackground(new java.awt.Color(58,71,106));
            	{
            		lblTitle = new javax.swing.JLabel();
            		jPanel1.add(lblTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            		lblTitle.setBackground(new java.awt.Color(249, 249, 249));
            		lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            		lblTitle.setText(title);
            		lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            		lblTitle.setOpaque(true);
            		lblTitle.setSize(679, 22);
            		lblTitle.setPreferredSize(new java.awt.Dimension(0, 22));
            	}
            	{
            		lblMessage = new JLabel();
            		jPanel1.add(lblMessage, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
            		lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            		lblMessage.setOpaque(true);
            		lblMessage.setBackground(new java.awt.Color(249,249,249));
            		lblMessage.setPreferredSize(new java.awt.Dimension(0,22));
            		lblMessage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            		lblMessage.setSize(679, 22);
            		lblMessage.setVisible(true);
            		lblMessage.setFont(new java.awt.Font("Dialog",1,12));
            		lblMessage.setText(message);
            	}
            }

            {
                labelProgress = new javax.swing.JLabel();
                jp.add(labelProgress, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
                labelProgress.setForeground(new java.awt.Color(254, 254, 254));
                labelProgress.setText("Progresso");
            }
            {
                progressBar = new JProgressBar(0, 100);
                jp.add(progressBar, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 10), 0, 0));
                progressBar.setBackground(java.awt.SystemColor.text);
                progressBar.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
                progressBar.setOpaque(true);
                progressBar.setSize(395, 20);
                progressBar.setPreferredSize(new java.awt.Dimension(0, 20));
            }

            jp.setOpaque(true);
        }

        final MoveMouseListener mml = new MoveMouseListener(jp);
        jp.addMouseListener(mml);
        jp.addMouseMotionListener(mml);
        jp.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.setUndecorated(true);
        this.pack();
        this.setSize(751, 100);
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

    public static void main(String[] args) throws FileNotFoundException {
    	update(new Version("1.0"));
    }
	
	public static void update(Version currentVersion) throws FileNotFoundException {
		Version latestVersion = checkForNewVersion(currentVersion);
	
	
		if (latestVersion != null) {
			String url = String.format(
					"https://github.com/marcosalpereira/apropriator/releases/download/v%s/binario.zip", 
					latestVersion.get());
			SoftwareUpdate update = new SoftwareUpdate("Software Update", url);
	    	OutputStream out = new FileOutputStream("/tmp/binario.zip");
	    	WebUtils.downloadFile(url, out, update);
	    	update.dispose();
		}
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
