package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.java.dev.designgridlayout.DesignGridLayout;
import utils.NameDocument;

public class HeaderPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField titleTextField;
	
	public HeaderPanel() {
		
		DesignGridLayout thisLayout = new DesignGridLayout(this);
		
		this.setBorder(new TitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY ), "Schichtplanung V. 1.1 - RS Gastronomie GmbH & Co.KG, Herford"));
		
		this.titleTextField = new JTextField(new NameDocument(100), "", 100);
		
		thisLayout.row().grid(new JLabel("Titel:")).add(this.titleTextField);
		
	}
	
	public String getTitle () {
		return this.titleTextField.getText().trim();
	}

	public void setTitle(String title) {
		
		this.titleTextField.setText(title);
		
	}
}
