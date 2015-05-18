package br.com.marcosoft.apropriator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import br.com.marcosoft.apropriator.util.AWTUtilitiesWrapper;
import br.com.marcosoft.apropriator.util.MoveMouseListener;
import br.com.marcosoft.apropriator.util.Util;

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
public class WaitWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    private JLabel lblMensagem;

    private final String message;
    private JButton btnSinalizarErro;

    private boolean interrompidoPeloUsuario;

    public static void main(String[] args) {
        new WaitWindow("Esperando pelo login do usuário").habilitarSinalizacaoErro();
    }

    public WaitWindow(String message) {
        super((Frame) null, false);
        this.message = message;
        initGUI();
        setAlwaysOnTop(true);
        setVisible(true);
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

        jPanel.setLayout(thisLayout);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        jPanel.setBackground(new Color(21, 38, 82));
        {
            this.lblMensagem = new JLabel();
            jPanel.add(
                this.lblMensagem,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
            this.lblMensagem.setText(message);
            this.lblMensagem.setForeground(new java.awt.Color(255, 255, 255));
            this.lblMensagem.setFont(new java.awt.Font("Arial", 3, 20));
            this.lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);
            this.lblMensagem.setHorizontalTextPosition(SwingConstants.CENTER);
            this.lblMensagem.setAutoscrolls(true);
            this.lblMensagem.setIconTextGap(0);
            this.lblMensagem.setVerticalTextPosition(SwingConstants.TOP);
        }
        {
        	btnSinalizarErro = new JButton();
        	jPanel.add(btnSinalizarErro, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
        	btnSinalizarErro.setText("Interromper Espera");
        	btnSinalizarErro.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent evt) {
        			btnSinalizarErroActionPerformed(evt);
        		}
        	});
        	btnSinalizarErro.setVisible(false);
        }

        final MoveMouseListener mml = new MoveMouseListener(jPanel);
        jPanel.addMouseListener(mml);
        jPanel.addMouseMotionListener(mml);
        jPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.setUndecorated(true);
        pack();
        this.setSize(707, 100);
        AWTUtilitiesWrapper.setOpacity(this);
        centerMe();
    }

    public void habilitarSinalizacaoErro() {
        this.btnSinalizarErro.setVisible(true);
    }

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;

        this.setLocation(screenWidth / 2 - this.getWidth() / 2, 30);
    }

    private void btnSinalizarErroActionPerformed(ActionEvent evt) {
    	this.interrompidoPeloUsuario = true;
    }

    public boolean isInterrompidoPeloUsuario() {
        return interrompidoPeloUsuario;
    }

    public static void waitForCondition(Condition condition, String message) {
        WaitWindow waitWindow = null;
        boolean conditionSatisfied = false;
        for(int i=0; i<60; i++) {
            if (condition.satisfied()) {
                conditionSatisfied = true;
                break;
            }
            if (waitWindow == null && i > 3) {
                waitWindow = new WaitWindow(message);
            }
            if (waitWindow != null) {
                if (i > 15) {
                    waitWindow.habilitarSinalizacaoErro();
                }
                if (waitWindow.isInterrompidoPeloUsuario()) {
                    break;
                }
            }
            Util.sleep(1000);
        }
        if (waitWindow != null) {
            waitWindow.dispose();
        }
        if (!conditionSatisfied) {
            throw new RuntimeException(
                "Algo inesperado aconteceu quando estava " + message);
        }
    }

}