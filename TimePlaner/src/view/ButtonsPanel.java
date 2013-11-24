package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.CloseButtonListener;
import controller.LoadButtonListener;
import controller.PdfButtonListener;
import controller.SaveButtonListener;

import net.java.dev.designgridlayout.DesignGridLayout;
import utils.TimeDocument;

public class ButtonsPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField timeField;
	
	private JButton loadBtn;
	private JButton saveBtn;
	private JButton toPdfBtn;
	private JButton closeBtn;
	
	public ButtonsPanel(MainView mainView) {
		
		DesignGridLayout thisLayout = new DesignGridLayout(this);
		
		this.setBorder(new TitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY ), "Aktionen"));
		
		this.timeField = new JTextField(new TimeDocument(10), "", 5);
		this.timeField.setEnabled(false);
		this.timeField.setText("0:00");
		
		thisLayout.row().grid(new JLabel("")).add(new JLabel("Wochenstunden:"), 2).add(this.timeField);

		this.loadBtn = new JButton("alten Schichtplan laden");
		this.saveBtn = new JButton("aktuellen Schichtplan speichern");
		this.toPdfBtn = new JButton("aktuellen Schichtplan ausgeben");
		this.closeBtn = new JButton("Programm beenden");
		
		int buttonSize = 50;
		
		this.loadBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
		this.saveBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
		this.toPdfBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
		this.closeBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
		
		this.saveBtn.addActionListener(new SaveButtonListener(mainView));
		this.loadBtn.addActionListener(new LoadButtonListener(mainView));
		this.closeBtn.addActionListener(new CloseButtonListener(mainView));
		this.toPdfBtn.addActionListener(new PdfButtonListener(mainView));
		
		thisLayout.row().grid().add(new JLabel(""));
		
		thisLayout.row().grid().add(this.loadBtn);
		thisLayout.row().grid().add(this.saveBtn);
		thisLayout.row().grid().add(this.toPdfBtn);
		thisLayout.row().grid().add(this.closeBtn);
		
	}
	
	public JTextField getTimeFeld() {
		return this.timeField;
	}
}
