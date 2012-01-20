package org.eclipse.virgo.ide.runtime.internal.ui.actions;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.runtime.internal.ui.actions.messages"; //$NON-NLS-1$
	public static String RedeployBundleAction_RedeployMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
