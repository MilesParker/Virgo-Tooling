package org.eclipse.virgo.ide.jdt.internal.core.util;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.jdt.internal.core.util.messages"; //$NON-NLS-1$
	public static String MarkerUtils_CouldNotResolveMessageEnd;
	public static String MarkerUtils_ManagingMarkersMessageStart;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
