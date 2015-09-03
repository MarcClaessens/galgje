package net.mcl.galgje.ui.swing;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import net.mcl.galgje.ui.KeyboardLayout;
import net.miginfocom.swing.MigLayout;

public class KeyboardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Map<Character, JComponent> keyButtons = new HashMap<>(20); 
	private final GameUI ui;

	
	private final ActionListener action = new ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			JComponent comp = ((JComponent) e.getSource());
			ui.keyPressed(getKeyValue(comp));
		};
	};
	
	public KeyboardPanel(GameUI ui) {
		this.ui = ui;
		setName("KeyboardPanel");
	}

	protected Character getKeyValue(JComponent btn) {
		return (Character) btn.getClientProperty("KEY");
	}

	public void apply(KeyboardLayout kb) {
		this.removeAll();
		keyButtons.clear();
		
		this.setLayout(new GridLayout(kb.getRows().size(), 1));
		
		for (char[] keys :  kb.getRows()) {
			JPanel rowPanel = new JPanel(new MigLayout("align center", "[fill]", "[]"));
			rowPanel.setBorder(new EtchedBorder());
			for (char c : keys) {
				JComponent btn = createKeyButton(c, kb);
				keyButtons.put(c, btn);
				rowPanel.add(btn);
			}
			this.add(rowPanel, "wrap");
		}
	}

	private JComponent createKeyButton(char c, KeyboardLayout kb) {
		JButton btn = new JButton("" + c);
		btn.putClientProperty("KEY", Character.valueOf(c));
		btn.addActionListener(action);
		btn.setVisible(true);
		return btn;
	}

	public void applyFontSize(int fontSize) {
		for (JComponent btn : keyButtons.values()) {
			applyFontSize(btn, fontSize);
		}
	}
	
	public void disableKey(Character c) {
		for (JComponent btn : keyButtons.values()) {
			if (getKeyValue(btn).equals(c)) {
				btn.setEnabled(false);
			}
		}
	}
	
	public void enableAllKeys() {
		for (JComponent btn : keyButtons.values()) {
			btn.setEnabled(true);
		}
	}
	
	private void applyFontSize(JComponent jc, int fontSize) {
		Font font1 = jc.getFont();
		if (font1.getSize() != fontSize) {
			Font font2 = new Font(font1.getName(), font1.getStyle(), fontSize);
			jc.setFont(font2);		
		}
	}

}
