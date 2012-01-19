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
package org.eclipse.virgo.ide.export;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author mparker
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.virgo.ide.export.messages"; //$NON-NLS-1$
	public static String AbstractProjectExportWizardPage_BrowseButtonText;
	public static String AbstractProjectExportWizardPage_BundleProjectErrorMessage;
	public static String AbstractProjectExportWizardPage_ExportDestinationMessageEnd;
	public static String AbstractProjectExportWizardPage_ExportDestinationMessageStart;
	public static String AbstractProjectExportWizardPage_OverwriteWithoutWarningMessage;
	public static String AbstractProjectExportWizardPage_SelectDestinationMessage;
	public static String AbstractProjectExportWizardPage_SelectProjectMessage;
	public static String BundleExportUtils_MissingEOL_MessageEnd;
	public static String BundleExportUtils_MissingEOL_MessageStart;
	public static String BundleExportUtils_WarningMessageTitle;
	public static String BundleExportUtils_WarningsMessage;
	public static String BundleExportWizard_OverwriteMessageEnd;
	public static String BundleExportWizard_OverwriteMessageStart;
	public static String BundleExportWizard_OverwriteTitle;
	public static String BundleExportWizard_Title;
	public static String BundleExportWizardPage_JarFileLabel;
	public static String BundleExportWizardPage_JarFileSpec_Message;
	public static String BundleExportWizardPage_JarFileSpec_Title;
	public static String ParExportWizard_ExportWarningsMessage;
	public static String ParExportWizard_ExportWarningsTitle;
	public static String ParExportWizard_OverwriteEnd;
	public static String ParExportWizard_OverwriteStart;
	public static String ParExportWizard_OverwriteTitle;
	public static String ParExportWizard_Title;
	public static String ParExportWizardPage_JarFIleSpecMessage;
	public static String ParExportWizardPage_JarFIleSpecTitle;
	public static String ParExportWizardPage_PAR_EXTENSION;
	public static String ParExportWizardPage_ParFileName;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
