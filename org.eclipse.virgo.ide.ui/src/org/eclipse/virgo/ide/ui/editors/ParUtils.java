/*******************************************************************************
 * Copyright (c) 2009 SpringSource, a divison of VMware, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SpringSource, a division of VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.virgo.ide.ui.editors;

import org.eclipse.core.resources.IProject;

/**
 * @author Christian Dupuis
 */
public class ParUtils {

	public static String getSymbolicName(IProject project) {
		// For now we are going to use project names instead of
		// Bundle-SymbolicName cause these *must*
		// be unique within the eclipse workspace
		return project.getName();
	}

}
