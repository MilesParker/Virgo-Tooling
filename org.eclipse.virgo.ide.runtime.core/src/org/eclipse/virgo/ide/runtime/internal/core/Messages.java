/**
 * <copyright>
 *
 * Copyright (c) 2010 Metascape, LLC.
 * All rights reserved.   This program and the accompanying materials
 * are the confidential and proprietary property of Metascape, LLC, and subject to the terms
 * of the accompanying license.
 * THIS SOFTWARE IS NOT OPEN SOURCE
 * http://butterflyzer.com  http://metascapeabm.com
 * 
 * Contributors:
 *   Metascape - Initial API and Implementation
 *
 * </copyright>
 *
 */
package org.eclipse.virgo.ide.runtime.internal.core;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.runtime.internal.core.messages"; //$NON-NLS-1$
	public static String ServerLaunchConfigurationDelegate_ConfiguringMessage;
	public static String ServerLaunchConfigurationDelegate_ConfiguringTPTPMessage;
	public static String ServerLaunchConfigurationDelegate_LaunchingMessage;
	public static String ServerLaunchConfigurationDelegate_PublishingMessage;
	public static String ServerLaunchConfigurationDelegate_SettingUpSourceMessage;
	public static String ServerLaunchConfigurationDelegate_StartingServerMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
