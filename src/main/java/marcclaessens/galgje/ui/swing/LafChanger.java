package marcclaessens.galgje.ui.swing;

import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class to modify Look and Feel.
 * 
 * @author Marc
 */
public final class LafChanger {
	private static final Logger LOG = LogManager.getLogger(LafChanger.class);

	/**
	 * Constructor.
	 * 
	 * @param listener - the object listening to LaF implementation changes.
	 */
	public LafChanger() {
	}

	/**
	 * Changes the look and feel and updates the configuration to the current LaF.
	 * 
	 * @return true on success, false on failure
	 */
	public void change(final String lafclassname, final Window owner) {
		if (lafclassname != null) {
			LOG.info("changing to " + lafclassname);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						UIManager.setLookAndFeel(lafclassname);
						Thread.sleep(1000);
						SwingUtilities.updateComponentTreeUI(owner);
						owner.invalidate();
						owner.repaint();
						owner.pack();
					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}
}
