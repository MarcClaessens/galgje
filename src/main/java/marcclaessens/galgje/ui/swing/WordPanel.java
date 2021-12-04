package marcclaessens.galgje.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class WordPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<JLabel> labels = new ArrayList<>(22);
	private int fontSize = 16;

	public WordPanel() {
		setLayout(new MigLayout("align center"));
		setBackground(new Color(0xee, 0xdd, 0x82));
	}

	public void applyFontSize(int fontSize) {
		this.fontSize = fontSize;
		if (!labels.isEmpty()) {
			Font font1 = labels.get(0).getFont();
			if (font1.getSize() != fontSize) {
				Font font2 = new Font(font1.getName(), font1.getStyle(),
						fontSize);
				for (JLabel label : labels) {
					label.setFont(font2);
				}
			}
		}
	}

	public void setText(String guessWord) {
		char[] c = guessWord.toCharArray();
		if (labels.size() != c.length) {
			initLabels(c.length);
		}
		for (int i = 0; i < c.length; i++) {
			labels.get(i).setText("" + c[i]);
		}
		repaint();
	}

	private void initLabels(int length) {
		labels.clear();
		removeAll();

		for (int i = 0; i < length; i++) {
			JLabel label = createJLabel();
			add(label);
			labels.add(label);
		}
	}

	private JLabel createJLabel() {
		JLabel label = new JLabel(" ");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setText("select word list first");

		Font font1 = label.getFont();
		Font font2 = new Font("Monospaced", font1.getStyle(), fontSize);
		label.setFont(font2);

		return label;
	}

}
