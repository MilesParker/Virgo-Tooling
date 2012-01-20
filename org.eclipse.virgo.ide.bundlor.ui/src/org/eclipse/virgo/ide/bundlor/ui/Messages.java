package org.eclipse.virgo.ide.bundlor.ui;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.bundlor.ui.messages"; //$NON-NLS-1$
	public static String BundlorUiPlugin_GenerateManifest_Text;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
