package org.eclipse.virgo.ide.runtime.internal.core.utils;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.runtime.internal.core.utils.messages"; //$NON-NLS-1$
	public static String webDownloadUtils_DownloadingMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
