package org.eclipse.virgo.ide.jdt.internal.ui.properties;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.jdt.internal.ui.properties.messages"; //$NON-NLS-1$
	public static String TestSourceFolderPreferencePage_SelectJavaFoldersMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
