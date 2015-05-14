package br.com.marcosoft.apropriator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import br.com.marcosoft.apropriator.model.DaySummary;
import br.com.marcosoft.apropriator.model.ItemTrabalho;
import br.com.marcosoft.apropriator.model.Task;
import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.util.AWTUtilitiesWrapper;
import br.com.marcosoft.apropriator.util.MoveMouseListener;
import br.com.marcosoft.apropriator.util.Util;


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
/**
 *
 */
public class ProgressInfo extends JFrame {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -6989750848125302233L;

    private javax.swing.JLabel lblLinhaTempo;
    private javax.swing.JLabel lblItemTrabalho;
    private javax.swing.JLabel lblPeriodo;
    private javax.swing.JLabel lblContexto;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel txtItemTrabalho;
    private javax.swing.JLabel txtPeriodo;
    private JTable tblLinhaTempo;
    private JTextField lblMessage;
    private JPanel jPanel1;
    private JPanel jp;
    private javax.swing.JLabel txtContexto;

    private String infoMessage;

    /** Creates new form NewJPanel */
    public ProgressInfo(String title) {
        initComponents(title);
    }

    @SuppressWarnings("serial")
    private void initComponents(String title) {

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
            		lblMessage = new JTextField();
            		jPanel1.add(lblMessage, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
            		lblMessage.setEditable(false);
            		lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            		lblMessage.setOpaque(true);
            		lblMessage.setBackground(new java.awt.Color(249,249,249));
            		lblMessage.setPreferredSize(new java.awt.Dimension(0,22));
            		lblMessage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            		lblMessage.setSize(679, 22);
            		lblMessage.setVisible(false);
            		lblMessage.setForeground(new java.awt.Color(255,0,0));
            		lblMessage.setFont(new java.awt.Font("Dialog",1,12));
            	}
            }

            {
                lblContexto = new javax.swing.JLabel();
                jp.add(lblContexto, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
                lblContexto.setForeground(new java.awt.Color(254, 254, 254));
                lblContexto.setText("Contexto");
            }
            {
                txtContexto = new javax.swing.JLabel();
                jp.add(txtContexto, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 10), 0, 0));
                txtContexto.setBackground(java.awt.SystemColor.text);
                txtContexto.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
                txtContexto.setOpaque(true);
                txtContexto.setSize(395, 20);
                txtContexto.setPreferredSize(new java.awt.Dimension(0, 20));
            }

            {
                lblItemTrabalho = new javax.swing.JLabel();
                jp.add(lblItemTrabalho, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
                lblItemTrabalho.setForeground(new java.awt.Color(254, 254, 254));
                lblItemTrabalho.setText("Item Trabalho");
            }
            {
                txtItemTrabalho = new javax.swing.JLabel();
                jp.add(txtItemTrabalho, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 10), 0, 0));
                txtItemTrabalho.setBackground(java.awt.SystemColor.text);
                txtItemTrabalho.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
                txtItemTrabalho.setOpaque(true);
                txtItemTrabalho.setSize(395, 20);
                txtItemTrabalho.setPreferredSize(new java.awt.Dimension(0, 20));
            }

            {
                lblPeriodo = new javax.swing.JLabel();
                lblPeriodo.setText("Período");
                jp.add(lblPeriodo, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
                lblPeriodo.setForeground(new java.awt.Color(254, 254, 254));
            }
            {
                txtPeriodo = new javax.swing.JLabel("1/1");
                jp.add(txtPeriodo, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 0, 3, 10), 0, 0));
                txtPeriodo.setBackground(java.awt.SystemColor.text);
                txtPeriodo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
                txtPeriodo.setOpaque(true);
                txtPeriodo.setSize(395, 20);
                txtPeriodo.setPreferredSize(new java.awt.Dimension(100, 20));
                txtPeriodo.setText(" ");
            }

            {
                lblLinhaTempo = new javax.swing.JLabel();
                jp.add(lblLinhaTempo, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
                lblLinhaTempo.setForeground(new java.awt.Color(254, 254, 254));
                lblLinhaTempo.setText("Linha Tempo");
            }
            {
            	final TableModel tblLinhaTempoModel =
            			new DefaultTableModel(
            					new String[][] {
            					        { "", "dom", "seg", "ter", "qua", "qui", "sex", "sab" },
            					        { "Antes", "", "", "", "", "", "", "" },
            							{ "Apropriando", "", "", "", "", "", "", "" },
            							{ "Depois", "", "", "", "", "", "", "" },
            					},
            					new String[] { "", "", "", "", "", "", "", "" });

            	tblLinhaTempo = new JTable() {

            		@Override
            		public TableCellRenderer getCellRenderer(int row, int column) {
            			return new DefaultTableCellRenderer() {
            				@Override
            				public int getHorizontalAlignment() {
            					return CENTER;
            				}
            			};
            		}

            		@Override
            		public boolean isCellEditable(int row, int column) {
            			return false;
            		}

            		@Override
            		public Component prepareRenderer(TableCellRenderer renderer,
            				int rowIndex, int columnIndex) {
            			final JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);
            			if (columnIndex == 0 || rowIndex == 0) {
            				component.setBackground(getTableHeader().getBackground());
            				component.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            			} else {
            				component.setBackground(Color.white);
            			}
            			component.setForeground(Color.black);
            			return component;
            		}

            	};
            	jp.add(tblLinhaTempo, new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 10), 0, 0));
            	tblLinhaTempo.setModel(tblLinhaTempoModel);
            }
            jp.setOpaque(true);
        }

        final MoveMouseListener mml = new MoveMouseListener(jp);
        jp.addMouseListener(mml);
        jp.addMouseMotionListener(mml);
        jp.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.setUndecorated(true);
        this.pack();
        this.setSize(751, 220);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        centerMe();

        AWTUtilitiesWrapper.setOpacity(this);
        this.setAlwaysOnTop(true);
        this.setVisible(true);

    }

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;

        this.setLocation(screenWidth / 2 - this.getWidth() / 2, 30);
    }

    public enum TipoTempo { ANTES, APROPRIANDO, DEPOIS};

    public void setResumoApropriando(TaskWeeklySummary summary) {
        if (this.infoMessage == null) {
            lblMessage.setVisible(false);
        } else {
            lblMessage.setText(infoMessage);
        }
        txtPeriodo.setText(
            Util.DD_MM_YYYY_FORMAT.format(summary.getDataInicio())
            + " - "
            + Util.DD_MM_YYYY_FORMAT.format(summary.getDataInicio())
        );
        txtContexto.setText(summary.getContexto());
        txtItemTrabalho.setText(summary.getItemTrabalho().toString());
        setTempo(TipoTempo.APROPRIANDO.ordinal() + 1, summary);
    }
    
    public void setInfoFinalizando(TaskSummary summary) {
  		setInfoMessage("Finalizando Tarefa");
  		tblLinhaTempo.setVisible(false);
  		lblLinhaTempo.setVisible(false);
  		
  		txtPeriodo.setVisible(false);
  		lblPeriodo.setVisible(false);
  		
    	txtContexto.setText(summary.getTask().getContexto());
    	txtItemTrabalho.setText(summary.getComentario());
    }
    
    public void setTempo(TipoTempo tipoTempo, TaskWeeklySummary summary) {
        setTempo(tipoTempo.ordinal() + 1, summary);
    }

    private void setTempo(int row, TaskWeeklySummary summary) {
        for (final DaySummary daySummary : summary.getDaysSummary()) {
            final String str;
            if (daySummary.getSum() == 0) {
                str = "";
            } else {
                str = Util.formatMinutesDecimal(daySummary.getHoras());
            }
            tblLinhaTempo.setValueAt(str, row, daySummary.getDay());
        }
    }

    public static void main(String[] args) {
        final TaskWeeklySummary tds = new TaskWeeklySummary("ctx", new ItemTrabalho(1,
            "impl"), new Date());
        int sum = 0;
        for (int day = Calendar.SUNDAY; day<= Calendar.SATURDAY; day++) {
            if (day != Calendar.TUESDAY) {
                tds.getDaysSummary().add(new DaySummary(day, sum += 30));
            }
        }
        final ProgressInfo progressInfo = new ProgressInfo("casa");
//        progressInfo.setTempo(TipoTempo.ANTES, tds);
//        progressInfo.setResumoApropriando(tds);
//        progressInfo.setTempo(TipoTempo.DEPOIS, tds.somar(tds));
        progressInfo.setInfoFinalizando(new TaskSummary(new Task("ctx", new ItemTrabalho(1, "foo"), "task")));
    }

    public void setInfoMessage(String message) {
    	lblMessage.setText(message);
        lblMessage.setVisible(message != null);        
    }

}
