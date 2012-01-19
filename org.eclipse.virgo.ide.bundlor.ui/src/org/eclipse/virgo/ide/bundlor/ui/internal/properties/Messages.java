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
package org.eclipse.virgo.ide.bundlor.ui.internal.properties;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.bundlor.ui.internal.properties.messages"; //$NON-NLS-1$
	public static String BundlorPreferencePage_AddButton;
	public static String BundlorPreferencePage_AutoFormatMessage;
	public static String BundlorPreferencePage_DefinePropertiesMessage;
	public static String BundlorPreferencePage_DeleteButton;
	public static String BundlorPreferencePage_ScanOutputMessage;
	public static String BundlorPreferencePage_SelectPropertiesMessage;
	public static String BundlorPreferencePage_SelectPropertiesTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
