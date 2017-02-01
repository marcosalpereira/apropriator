package br.com.marcosoft.apropriator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import org.marcosoft.lib.AWTUtilitiesWrapper;


/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class TopMostMessage extends JDialog {
    private static final long serialVersionUID = 1L;

    private JButton btnDone;

    private JPanel jPanel1;

    private JTextArea lblMensagem;

    private final String message;

    public static void main(final String[] args) {
        message("teste");
    }

    /**
     * Dialogo para que se possa ajustar os dados da apropriacao.
     */
    public static void message(String message) {
        final TopMostMessage inst = new TopMostMessage(message);

        inst.setVisible(true);
    }

    private TopMostMessage(String message) {
        super((Frame) null, true);
        this.message = message;
        initGUI();
    }

    private void initGUI() {
        setAlwaysOnTop(true);
        final GridBagLayout thisLayout = new GridBagLayout();
        thisLayout.rowWeights = new double[] {0.1, 0.0};
        thisLayout.rowHeights = new int[] {7, 7};
        thisLayout.columnWeights = new double[] {0.1};
        thisLayout.columnWidths = new int[] {7};
        final JPanel jPanel = new JPanel();
        jPanel.setBorder(new LineBorder(new java.awt.Color(230,230,250), 2, true));
        getContentPane().add(jPanel);
        jPanel.setLayout(thisLayout);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jPanel.setBackground(new Color(21, 38, 82));
        this.setUndecorated(true);
        {
        	this.lblMensagem = new JTextArea(message);
            jPanel.add(lblMensagem, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
            this.lblMensagem.setForeground(new java.awt.Color(255, 255, 255));
            lblMensagem.setFont(new java.awt.Font("Arial",3,18));
            this.lblMensagem.setAutoscrolls(true);
            lblMensagem.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            lblMensagem.setBackground(new java.awt.Color(21,38,82));
            lblMensagem.setEditable(false);
            lblMensagem.setLineWrap(true);
        }
        {
            this.jPanel1 = new JPanel();
            final FlowLayout jPanel1Layout = new FlowLayout();
            this.jPanel1.setLayout(jPanel1Layout);
            jPanel.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
            this.jPanel1.setOpaque(false);
            {
                this.btnDone = new JButton();
                this.jPanel1.add(this.btnDone);
                btnDone.setText("OK");
                btnDone.setPreferredSize(new java.awt.Dimension(80, 22));
                this.btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent evt) {
                        btnSelecionarActionPerformed(evt);
                    }
                });
            }
        }
        this.setSize(705, 135);
        centerMe();
        AWTUtilitiesWrapper.setOpacity(this);
    }

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeigh = screenSize.height;

        this.setLocation(
            screenWidth / 2 - this.getWidth() / 2, screenHeigh / 2 - this.getHeight() / 2);
    }

    private void btnSelecionarActionPerformed(
        @SuppressWarnings("unused") final ActionEvent evt) {
        dispose();
    }

}