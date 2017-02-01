package br.com.marcosoft.apropriator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import org.marcosoft.lib.AWTUtilitiesWrapper;
import org.marcosoft.lib.MoveMouseListener;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SelectOptionWindow extends JDialog {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -6989750848125302233L;

    private JPanel jp;
    private JButton btnCancelar;
    private JButton btnConfirmar;
    private JPanel jPanel1;
    private JComboBox cboProjetos;
    private JLabel lblTitle;

    private String selectedOption;

    public static void main(String[] args) {
        final SelectOptionWindow c = new SelectOptionWindow(
            "tit", Arrays.asList("casa", "aviao"));
        System.out.println(c.getSelectedOption());
    }

    /** Creates new form NewJPanel
     * @param title titulo da janela
     * @param options projetos atuais
     **/
    public SelectOptionWindow(String title, Collection<String> options) {
        initComponents(title, options);
    }

    private void initComponents(String title, Collection<String> options) {

    	final GridBagLayout thisLayout1 = new GridBagLayout();
    	thisLayout1.rowWeights = new double[] {1.0};
    	thisLayout1.rowHeights = new int[] {7};
    	thisLayout1.columnWeights = new double[] {1.0};
    	thisLayout1.columnWidths = new int[] {7};
        getContentPane().setLayout(thisLayout1);

        {
            jp = new JPanel();

            final Color blueBackGround = new Color(58,71,106);
            jp.setBorder(new LineBorder(new java.awt.Color(230,230,250), 3, true));
            jp.setBackground(blueBackGround);

            final GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] {0.0, 0.0, 1.0};
            thisLayout.rowHeights = new int[] {7, 7, 7};
            thisLayout.columnWeights = new double[] {0.1};
            thisLayout.columnWidths = new int[] {7};
            jp.setLayout(thisLayout);

            getContentPane().add(jp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

            jp.setOpaque(true);
            {
            	lblTitle = new javax.swing.JLabel();
            	jp.add(lblTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 5, 0, 5), 0, 0));
            	lblTitle.setBackground(new java.awt.Color(249,249,249));
            	lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            	lblTitle.setText(title);
            	lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            	lblTitle.setOpaque(true);
            	lblTitle.setSize(679, 22);
            	lblTitle.setPreferredSize(new java.awt.Dimension(0,22));
            }
            {
            	final ComboBoxModel cboProjetosModel = new DefaultComboBoxModel(options.toArray());
            	cboProjetos = new JComboBox();
            	jp.add(cboProjetos, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 5, 0, 5), 0, 0));
            	cboProjetos.setModel(cboProjetosModel);
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

        final MoveMouseListener mml = new MoveMouseListener(jp);
        jp.addMouseListener(mml);
        jp.addMouseMotionListener(mml);
        jp.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.setUndecorated(true);
        this.pack();
        this.setSize(843, 96);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        centerMe();

        AWTUtilitiesWrapper.setOpacity(this);
        this.setTitle("Importar projetos");
        this.setAlwaysOnTop(true);
        setModal(true);
        this.setVisible(true);

    }

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;

        this.setLocation(screenWidth / 2 - this.getWidth() / 2, 30);
    }

    private void btnConfirmarActionPerformed(ActionEvent evt) {
        selectedOption = (String) cboProjetos.getSelectedItem();
    	this.dispose();
    }

    private void btnCancelarActionPerformed(ActionEvent evt) {
    	this.dispose();
    	this.selectedOption = null;
    }

    public String getSelectedOption() {
        return selectedOption;
    }


}
