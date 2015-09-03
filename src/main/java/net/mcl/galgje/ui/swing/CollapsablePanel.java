package net.mcl.galgje.ui.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * Original source : http://www.coderanch.com/t/341737/GUI/java/Expand-Collapse-Panels
 * but reworked quite a bit.
 */
public class CollapsablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean selected;
	private JPanel contentPanel;
	private HeaderPanel headerPanel;

	private static class HeaderPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final String textInactive;
		private final String textActive;
		private final JLabel label = new JLabel();
		
		public HeaderPanel(String textInactive, String textActive) {
			setName("a HeaderPanel");
			this.textInactive = textInactive;
			this.textActive = textActive;
			this.setLayout(new MigLayout());
			label.setText(textInactive);
			add(label);
		}

		public void toggle(boolean active) {
			if (active) {
				label.setText(textActive);
			} else {
				label.setText(textInactive);
			}
			label.setVisible(true);
		}
	}

	public CollapsablePanel(JPanel panel, String textInactive, String textActive, boolean selected) {
		super(new GridBagLayout());
		setName("a collapsable panel");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 3, 0, 3);
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		
		headerPanel = new HeaderPanel(textInactive, textActive);
		this.selected = selected;
		
		setBackground(new Color(200, 200, 220));
		contentPanel = panel;

		add(headerPanel, gbc);
		add(contentPanel, gbc);
		contentPanel.setVisible(false);

		JLabel padding = new JLabel();
		gbc.weighty = 1.0;
		add(padding, gbc);
		toggleSelection();
		
		headerPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggleSelection();
			}
		});
	}

	public void toggleSelection() {
		selected = !selected;

		if (selected) {
			contentPanel.setVisible(false);
			headerPanel.toggle(false);
		} else {
			contentPanel.setVisible(true);
			headerPanel.toggle(true);
		}
		validate();

		Container parent = this.getParent();
		while (parent != null && ! (parent instanceof Window)) {
			parent = parent.getParent();
		}
		if (parent != null && parent instanceof Window) {
			((JFrame) parent).pack();
		}
		
		headerPanel.repaint();
	}

}
