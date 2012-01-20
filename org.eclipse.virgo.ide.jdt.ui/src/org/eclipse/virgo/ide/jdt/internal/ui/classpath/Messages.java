package org.eclipse.virgo.ide.jdt.internal.ui.classpath;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.jdt.internal.ui.classpath.messages"; //$NON-NLS-1$
	public static String ServerClasspathContainerPage_BundleCalsspathTitle;
	public static String ServerClasspathContainerPage_FinishMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
