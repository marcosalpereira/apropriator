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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import br.com.marcosoft.apropriator.util.AWTUtilitiesWrapper;

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
public class EsperarAjustesUsuario extends JDialog {
    private static final long serialVersionUID = 1L;

    private static boolean ajustou;

    private JButton btnDone;

    private JButton btnCancelar;

    private JPanel jPanel1;

    private JLabel lblMensagem;

    private final String contexto;

    public static void main(final String[] args) {
        System.out.println(esperarAjustes("1/1"));
    }

    /**
     * Dialogo para que se possa ajustar os dados da apropriacao.
     * @return <code>true</code> se ajustou.
     */
    public static boolean esperarAjustes(String contexto) {
        final EsperarAjustesUsuario inst = new EsperarAjustesUsuario(contexto);
        ajustou = false;
        inst.setVisible(true);
        inst.dispose();

        return ajustou;
    }

    private EsperarAjustesUsuario(String contexto) {
        super((Frame) null, true);
        this.contexto = contexto;
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

        jPanel.setLayout(thisLayout);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jPanel.setBackground(new Color(21, 38, 82));
        {
            this.lblMensagem = new JLabel();
            jPanel.add(
                this.lblMensagem,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
            this.lblMensagem
                .setText("<html>"
                    + "<center>Ajuste " + contexto + "</center>"
                    + "Ajuste os campos na janela do SGI como se fosse fazer a apropriação."
                    + "<br />Quando terminar, acione o botão 'Terminei Ajustes' para continuar o processo de apropriação"
                	+  "</html>");
            this.lblMensagem.setForeground(new java.awt.Color(255, 255, 255));
            this.lblMensagem.setFont(new java.awt.Font("Arial", 3, 20));
            this.lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);
            this.lblMensagem.setHorizontalTextPosition(SwingConstants.CENTER);
            this.lblMensagem.setAutoscrolls(true);
            this.lblMensagem.setIconTextGap(0);
            this.lblMensagem.setVerticalTextPosition(SwingConstants.TOP);
        }
        {
            this.jPanel1 = new JPanel();
            final FlowLayout jPanel1Layout = new FlowLayout();
            this.jPanel1.setLayout(jPanel1Layout);
            jPanel.add(
                this.jPanel1,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            this.jPanel1.setOpaque(false);
            {
                this.btnDone = new JButton();
                this.jPanel1.add(this.btnDone);
                this.btnDone.setText("Terminei Ajustes");
                this.btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent evt) {
                        btnSelecionarActionPerformed(evt);
                    }
                });
            }
            {
                this.btnCancelar = new JButton();
                this.jPanel1.add(this.btnCancelar);
                this.btnCancelar.setText("Cancelar");
                this.btnCancelar.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent evt) {
                        btnCancelarActionPerformed(evt);
                    }
                });
            }
        }
        AWTUtilitiesWrapper.setOpacity(this);
        this.setUndecorated(true);
        this.setSize(707, 174);
        centerMe();
    }

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;

        this.setLocation(screenWidth / 2 - this.getWidth() / 2, 30);
    }

    private void btnSelecionarActionPerformed(
        @SuppressWarnings("unused") final ActionEvent evt) {
        ajustou = true;
        setVisible(false);
    }

    private void btnCancelarActionPerformed(
        @SuppressWarnings("unused") final ActionEvent evt) {
        ajustou = false;
        setVisible(false);
    }

}