package org.eclipse.virgo.ide.jdt.internal.core.classpath;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.jdt.internal.core.classpath.messages"; //$NON-NLS-1$
	public static String ServerClasspathContainer_BundleDependencies;
	public static String ServerClasspathContainerUpdateJob_UpdatingClasspathMessage;
	public static String ServerClasspathContainerUpdateJob_UpdatingContainerMessage;
	public static String ServerClasspathContainerUpdateJob_UpdatingSpringSourceContainerMessage;
	public static String ServerClasspathUtils_CannotSaveMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
