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
package org.eclipse.virgo.ide.runtime.internal.core.actions;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.runtime.internal.core.actions.messages"; //$NON-NLS-1$
	public static String ModifyArtefactOrderCommand_Name;
	public static String ModifyCleanStartupCommand_Name;
	public static String ModifyDeployerPasswordCommand_Name;
	public static String ModifyDeployerPortCommand_Name;
	public static String ModifyDeployerUsernameCommand_Name;
	public static String ModifyStaticResourcesCommand_Name;
	public static String ModifyTailLogFilesCommand_Name;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
