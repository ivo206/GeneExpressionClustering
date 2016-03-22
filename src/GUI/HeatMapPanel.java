package GUI;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class HeatMapPanel extends JPanel {
	
	private BufferedImage image;
	
	public HeatMapPanel(BufferedImage imageHeatMap)
	{
		image = imageHeatMap;
	}
	
	@Override 
	public void paintComponent(Graphics g)
	{
		int positionX =(int) (1000/2)-(image.getWidth()/2);
		int positionY =(int) (535/2)-(image.getHeight()/2);
		super.paintComponent(g);
		g.drawImage(image, positionX, positionY, null);
	}

}
