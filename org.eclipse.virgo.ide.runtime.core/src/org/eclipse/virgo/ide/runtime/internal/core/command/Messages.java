package org.eclipse.virgo.ide.runtime.internal.core.command;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.runtime.internal.core.command.messages"; //$NON-NLS-1$
	public static String AbstractJmxServerCommand_MBeanNotOpenMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
