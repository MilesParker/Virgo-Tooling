package org.eclipse.virgo.ide.runtime.internal.ui;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.runtime.internal.ui.messages"; //$NON-NLS-1$
	public static String BasicRepositoryLabelProvider_BundlesLibrary;
	public static String BasicRepositoryLabelProvider_BundlesText;
	public static String ServerDeploymentDecorator_DeployedDecoration;
	public static String ServerDeploymentDecorator_NotRunningDecoration;
	public static String ServerUiPlugin_InternalErrorMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
