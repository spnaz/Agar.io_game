package ir.spnaz.paint.App;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JMenuItem;


public class ColorMenuItem extends JMenuItem implements Serializable{
	Color color=Color.BLUE;
	
	public ColorMenuItem() {
		super();
		// TODO Auto-generated constructor stub
		
	}
	
	public ColorMenuItem(String name,Color color) {
		super(name);
		this.color=color;
		// TODO Auto-generated constructor stub
		
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color=color;
	}
}
