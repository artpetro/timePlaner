package view;

import java.awt.Color;

import javax.swing.BorderFactory;
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
		
		this.setBorder(new TitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY ), "Titel"));
		
		this.titleTextField = new JTextField(new NameDocument(100), "", 100);
		
		thisLayout.row().grid().add(this.titleTextField);
		
	}
	
	public String getTitle () {
		return this.titleTextField.getText().trim();
	}

	public void setTitle(String title) {
		
		this.titleTextField.setText(title);
		
	}
}
