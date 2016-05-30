package br.com.marcosoft.apropriator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.Timer;
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
import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.util.AWTUtilitiesWrapper;
import br.com.marcosoft.apropriator.util.MoveMouseListener;
import br.com.marcosoft.apropriator.util.Util;
import br.com.marcosoft.apropriator.util.WebUtils.Progress;


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
public class ProgressInfo extends JFrame implements ActionListener, Progress {

    private static final int SIZE_ANIMATION_DELAY = 15;
	private static final Dimension FULL_SIZE = new Dimension(751, 250);
    private static final Dimension MIN_SIZE = new Dimension(751, 90);

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
    private JLabel lblMessage;
    private JPanel panMain;
    private javax.swing.JLabel txtContexto;

    private String infoMessage;

	private JPanel panBody;

	private JPanel panTitle;
	private final Timer timerAnimateSize;
	private double stepX;
	private double stepY;
	private int stepsCountDown;
	private JProgressBar progressBar;

	private static final Color BLUE_BACK_GROUND = new Color(58,71,106);

    /** Creates new form NewJPanel */
    public ProgressInfo() {
    	timerAnimateSize = new Timer(SIZE_ANIMATION_DELAY, this);
        initComponents();
    }

    @Override
    public void setTitle(String title) {
    	lblTitle.setText(title);
    }

    private void initComponents() {

        final GridBagLayout thisLayout1 = new GridBagLayout();
        thisLayout1.columnWidths = new int[] {7};
        thisLayout1.rowHeights = new int[] {7};
        thisLayout1.columnWeights = new double[] {0.1};
        thisLayout1.rowWeights = new double[] {0.1};
        getContentPane().setLayout(thisLayout1);

        {
            panMain = new JPanel();

            panMain.setBorder(new LineBorder(new java.awt.Color(230,230,250), 3, true));
            panMain.setBackground(BLUE_BACK_GROUND);

            final GridBagLayout mainLayout = new GridBagLayout();
            mainLayout.columnWeights = new double[] {1};
            mainLayout.rowWeights = new double[] {0.1, 0.1};
            mainLayout.rowHeights = new int[] {7, 7};
            mainLayout.columnWidths = new int[] {1};
            panMain.setLayout(mainLayout);

            getContentPane().add(panMain, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));

            panTitle = criarPanelTitulo();
			panMain.add(panTitle,
					new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5, 10, 15, 10),
									0, 0));

			panBody = criarPanelBody();
			panMain.add(panBody,
					new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5, 10, 15, 10),
									0, 0));

			panBody.setVisible(false);

            panMain.setOpaque(true);
        }

        final MoveMouseListener mml = new MoveMouseListener(panMain);
        panMain.addMouseListener(mml);
        panMain.addMouseMotionListener(mml);
        panMain.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.setUndecorated(true);
        this.pack();
        this.setSize(MIN_SIZE);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        centerMe();

        AWTUtilitiesWrapper.setOpacity(this);
        this.setAlwaysOnTop(true);
        this.setVisible(true);

    }

	@SuppressWarnings("serial")
	private JPanel criarPanelBody() {
		final JPanel panBody = new JPanel();
		panBody.setBackground(BLUE_BACK_GROUND);
		panBody.setOpaque(true);
        final GridBagLayout bodyLayout = new GridBagLayout();
        bodyLayout.columnWeights = new double[] {0.0, 0.7};
        bodyLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.2};
        bodyLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
        bodyLayout.columnWidths = new int[] {109};
        panBody.setLayout(bodyLayout);

		{
		    lblContexto = new javax.swing.JLabel();
		    panBody.add(lblContexto, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 0), 0, 0));
		    lblContexto.setForeground(new java.awt.Color(254, 254, 254));
		    lblContexto.setText("Contexto");
		}
		{
		    txtContexto = new javax.swing.JLabel();
		    panBody.add(txtContexto, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));
		    txtContexto.setBackground(java.awt.SystemColor.text);
		    txtContexto.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		    txtContexto.setOpaque(true);
		    txtContexto.setSize(395, 20);
		    txtContexto.setPreferredSize(new java.awt.Dimension(0, 20));
		}

		{
		    lblItemTrabalho = new javax.swing.JLabel();
		    panBody.add(lblItemTrabalho, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 0), 0, 0));
		    lblItemTrabalho.setForeground(new java.awt.Color(254, 254, 254));
		    lblItemTrabalho.setText("Item Trabalho");
		}
		{
		    txtItemTrabalho = new javax.swing.JLabel();
		    panBody.add(txtItemTrabalho, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));
		    txtItemTrabalho.setBackground(java.awt.SystemColor.text);
		    txtItemTrabalho.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		    txtItemTrabalho.setOpaque(true);
		    txtItemTrabalho.setSize(395, 20);
		    txtItemTrabalho.setPreferredSize(new java.awt.Dimension(0, 20));
		}

		{
		    lblPeriodo = new javax.swing.JLabel();
		    lblPeriodo.setText("Período");
		    panBody.add(lblPeriodo, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 0), 0, 0));
		    lblPeriodo.setForeground(new java.awt.Color(254, 254, 254));
		}
		{
		    txtPeriodo = new javax.swing.JLabel("1/1");
		    panBody.add(txtPeriodo, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));
		    txtPeriodo.setBackground(java.awt.SystemColor.text);
		    txtPeriodo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		    txtPeriodo.setOpaque(true);
		    txtPeriodo.setSize(395, 20);
		    txtPeriodo.setPreferredSize(new java.awt.Dimension(100, 20));
		    txtPeriodo.setText(" ");
		}

		{
		    lblLinhaTempo = new javax.swing.JLabel();
		    panBody.add(lblLinhaTempo, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 0), 0, 0));
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
			panBody.add(tblLinhaTempo, new GridBagConstraints(1, 3, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));
			tblLinhaTempo.setModel(tblLinhaTempoModel);

			return panBody;
		}
	}

	private JPanel criarPanelTitulo() {
		final JPanel panTitle = new JPanel();
		final GridBagLayout panTitleLayout = new GridBagLayout();
		panTitleLayout.rowWeights = new double[] {0.0, 0.1, 0.1};
		panTitleLayout.rowHeights = new int[] {0, 1, 1};
		panTitleLayout.columnWeights = new double[] {0.1};
		panTitleLayout.columnWidths = new int[] {7};
		panTitle.setLayout(panTitleLayout);
		panTitle.setBackground(BLUE_BACK_GROUND);
		{
			lblTitle = new javax.swing.JLabel();
			panTitle.add(lblTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));
			lblTitle.setBackground(new java.awt.Color(249, 249, 249));
			lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lblTitle.setText("");
			lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			lblTitle.setOpaque(true);
			lblTitle.setSize(679, 25);
			lblTitle.setPreferredSize(new java.awt.Dimension(0, 25));
		}
		{
			lblMessage = new JLabel();
			panTitle.add(lblMessage, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
			lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lblMessage.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
			lblMessage.setOpaque(false);
			lblMessage.setVisible(false);
			lblMessage.setForeground(new java.awt.Color(255, 255, 255));
			lblMessage.setFont(new java.awt.Font("Arial",3,18));
		}

		{
            progressBar = new JProgressBar(0, 100);
            panTitle.add(progressBar, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
            progressBar.setBackground(java.awt.SystemColor.text);
            progressBar.setOpaque(true);
            progressBar.setStringPainted(true);
            progressBar.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));

		}
		return panTitle;
	}

    private void centerMe() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;

        this.setLocation(screenWidth / 2 - this.getWidth() / 2, 30);
    }

    public enum TipoTempo { ANTES, APROPRIANDO, DEPOIS};

    public void setResumoApropriando(TaskWeeklySummary summary) {
    	panBody.setVisible(true);
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
        limparTempos();
        setTempo(TipoTempo.APROPRIANDO.ordinal() + 1, summary);
        changeSize(FULL_SIZE);
    }

    private void limparTempos() {
		for (int row=1; row<=3; row++) {
			for (int col=1; col<=7; col++) {
				tblLinhaTempo.setValueAt("", row, col);
			}
		}
	}

	public void setInfoFinalizando(TaskSummary summary) {
  		setInfoMessage("Finalizando Tarefa:"
  					+ summary.getTask().getItemTrabalho()
  					+ " "
  					+ summary.getComentario());
  		panBody.setVisible(false);
  		changeSize(MIN_SIZE);
    }

	public void setInfoRecuperandoTitulos(String contexto, Integer id) {
		setInfoMessage("Recuperando Título:"
				+ contexto
				+ " "
				+ id);
		panBody.setVisible(false);
		changeSize(MIN_SIZE);
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
        final ProgressInfo progressInfo = new ProgressInfo();
        progressInfo.setTitle("Atualizando ...");
        progressInfo.changeSize(MIN_SIZE);
        progressInfo.setTempo(TipoTempo.ANTES, tds);
        progressInfo.setResumoApropriando(tds);
        progressInfo.setTempo(TipoTempo.DEPOIS, tds.somar(tds));
        progressInfo.setInfoMessage("foo");
        Util.sleep(2000);
        progressInfo.limparTempos();
    }

    public void setInfoMessage(String message) {
    	lblMessage.setText(message);
        lblMessage.setVisible(message != null);
    }

    public void changeSize(Dimension targetSize) {
    	while(timerAnimateSize.isRunning()) {Util.sleep(200);};
    	animateChangeSize(new Dimension(getWidth(), 10));

    	while(timerAnimateSize.isRunning()) {Util.sleep(200);};
    	animateChangeSize(targetSize);
    }

	private void animateChangeSize(Dimension targetSize) {
		stepsCountDown = 20;
		timerAnimateSize.restart();
    	stepX = (targetSize.width - getWidth()) / (double) stepsCountDown;
    	stepY = (targetSize.height - getHeight()) / (double) stepsCountDown;
	}

    public void actionPerformed(ActionEvent e) {
    	if (stepsCountDown-- == 0) {
    		timerAnimateSize.stop();
    	}
    	setSize((int) Math.round(getWidth() + stepX), (int) Math.round(getHeight() + stepY));
    }

    public void setProgress(String progress) {
    	lblMessage.setVisible(true);
    	lblMessage.setText(progress);
    }

    public void setProgress(int value) {
    	progressBar.setVisible(true);
    	progressBar.setValue(value);
    }

    public void finished() {
    	progressBar.setVisible(false);
    	lblMessage.setVisible(false);
    }

}
