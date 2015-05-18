package br.com.marcosoft.apropriator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import br.com.marcosoft.apropriator.model.ApropriationFile.Config;

@SuppressWarnings("serial")
public class ReadPasswordWindow extends JDialog {

    private JPanel jp;
    private JButton btnCancelar;
    private JButton btnConfirmar;
    private JPanel jPanel1;
    private JPasswordField txtPassword;

    private String password;
    private JTextArea lblInfo;

    public static void main(String[] args) {
        final String info = String.format("Essa versão, por default, se oferece para salvar a senha. " +
            "\nSe não quiser que a senha seja salva localmente. Use o botão 'Cancelar' agora e " +
            "\ncoloque na aba 'Config' da planilha o valor 'Não' para opção '%s'" +
            "\nOBS:A senha é salva criptografada.", Config.LOGIN_SALVAR_SENHA);
        final ReadPasswordWindow c = new ReadPasswordWindow(
            "Informe a nova senha", info);
        System.out.println(c.getPassword());
    }

    /** Creates new form NewJPanel
     * @param title title
     * @param info info message
     **/
    public ReadPasswordWindow(String title, String info) {
        initComponents(title, info);
    }

    private void initComponents(String title, String info) {

    	final GridBagLayout thisLayout1 = new GridBagLayout();
    	thisLayout1.rowWeights = new double[] {1.0};
    	thisLayout1.rowHeights = new int[] {7};
    	thisLayout1.columnWeights = new double[] {1.0};
    	thisLayout1.columnWidths = new int[] {7};
        getContentPane().setLayout(thisLayout1);

        {
            jp = new JPanel();

            final Color blueBackGround = new Color(58,71,106);
            jp.setBackground(blueBackGround);

            final GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] {1.0, 0.0, 0.0};
            thisLayout.rowHeights = new int[] {7, 7, 7};
            thisLayout.columnWeights = new double[] {0.1};
            thisLayout.columnWidths = new int[] {7};
            jp.setLayout(thisLayout);

            getContentPane().add(jp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

            jp.setOpaque(true);
            {
                lblInfo = new javax.swing.JTextArea();
                jp.add(lblInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 5, 0, 5), 0, 0));
                lblInfo.setBackground(blueBackGround);
                lblInfo.setForeground(Color.WHITE);
                //lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lblInfo.setText(info);
                lblInfo.setEditable(false);
                lblInfo.setOpaque(true);
                lblInfo.setSize(679, 22);
                lblInfo.setPreferredSize(new java.awt.Dimension(0,22));
            }
            {
            	txtPassword = new JPasswordField();
            	jp.add(txtPassword, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 5, 0, 5), 0, 0));
            }
            {
            	jPanel1 = new JPanel();
            	jp.add(jPanel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 5, 0, 5), 0, 0));
            	final FlowLayout jPanel1Layout2 = new FlowLayout();
            	jPanel1Layout2.setAlignment(FlowLayout.RIGHT);
            	jPanel1Layout2.setHgap(10);
            	jPanel1.setBackground(new java.awt.Color(58,71,106));
            	jPanel1.setLayout(jPanel1Layout2);
            	{
            		btnConfirmar = new JButton();
            		btnConfirmar.setDefaultCapable(true);
            		jPanel1.add(btnConfirmar);
            		btnConfirmar.setText("Confirmar");
            		btnConfirmar.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent evt) {
            				btnConfirmarActionPerformed(evt);
            			}
            		});
            	}
            	{
            		btnCancelar = new JButton();
            		jPanel1.add(btnCancelar);
            		btnCancelar.setText("Cancelar");
            		btnCancelar.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent evt) {
            				btnCancelarActionPerformed(evt);
            			}
            		});
            	}
            }
        }

        setTitle(title);

        this.pack();
        this.setSize(600, 170);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        centerMe();

        this.setAlwaysOnTop(true);
        setModal(true);
        txtPassword.requestFocusInWindow();
        this.setVisible(true);

    }

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeight = screenSize.height;

        this.setLocation(screenWidth / 2 - this.getWidth() / 2, screenHeight / 2 - this.getHeight() / 2);
    }

    private void btnConfirmarActionPerformed(ActionEvent evt) {
        password = new String(txtPassword.getPassword());
    	this.dispose();
    }

    private void btnCancelarActionPerformed(ActionEvent evt) {
    	this.dispose();
    	this.password = null;
    }

    public String getPassword() {
        return password;
    }


}
