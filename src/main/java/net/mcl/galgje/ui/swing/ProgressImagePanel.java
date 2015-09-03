package net.mcl.galgje.ui.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ProgressImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final List<BufferedImage> images = new ArrayList<>();
	private JLabel label = new JLabel();

	
	public ProgressImagePanel() {
		setName("ProgressPanel");
		
		setLayout(new MigLayout("align center"));
		
		for (int i=0; i<9; i++) {
			BufferedImage img = null;
			try (InputStream is = getClass().getResourceAsStream("/images/alien/" + i +".png")) {
				img = ImageIO.read(is);
				images.add(img);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		label.setIcon(new ImageIcon(images.get(0)));
		add(label);
	}

	public void showImage(int i) {
		label.setIcon(new ImageIcon(images.get(i)));
	}


}
