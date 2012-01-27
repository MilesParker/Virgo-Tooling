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
package org.eclipse.virgo.ide.runtime.internal.core.utils;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.virgo.ide.runtime.core.ServerCorePlugin;

/**
 * 
 * @author mparker
 *
 */
public class StatusUtil {
	public static void error(String message, Exception e) {
		StatusManager.getManager().handle(new Status(IStatus.ERROR, ServerCorePlugin.PLUGIN_ID, "An IO Exception occurred.", e));
	}
	
	public static void error(Exception e) {
		if (e instanceof IOException) {
			error("An IO Exception occurred.", e);
		}
		error("An unexpected exception occurred.", e);
	}
}
