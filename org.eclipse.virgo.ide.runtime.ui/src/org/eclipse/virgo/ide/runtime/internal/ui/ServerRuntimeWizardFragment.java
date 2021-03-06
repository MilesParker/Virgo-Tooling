/*******************************************************************************
 * Copyright (c) 2009, 2010 SpringSource, a divison of VMware, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SpringSource, a division of VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.virgo.ide.runtime.internal.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.virgo.ide.runtime.core.ServerCorePlugin;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;


/**
 * @author Christian Dupuis
 */
public class ServerRuntimeWizardFragment extends WizardFragment {
	
	protected ServerRuntimeComposite comp;

	@Override
	public boolean hasComposite() {
		return true;
	}

	@Override
	public boolean isComplete() {
		IRuntimeWorkingCopy runtime = (IRuntimeWorkingCopy) getTaskModel().getObject(TaskModel.TASK_RUNTIME);
		
		if (runtime == null) {
			return false;
		}
		IStatus status = runtime.validate(null);
		return (status == null || status.getSeverity() != IStatus.ERROR);
	}

	@Override
	public Composite createComposite(Composite parent, IWizardHandle wizard) {
		String wizardTitle = ServerUiPlugin.getResourceString("wizardTitle");
		String wizardDescription = ServerUiPlugin.getResourceString("wizardDescription");
		comp = new ServerRuntimeComposite(parent, wizard, wizardTitle, wizardDescription);
		return comp;
	}

	@Override
	public void enter() {
		if (comp != null) {
			IRuntimeWorkingCopy runtime = (IRuntimeWorkingCopy) getTaskModel().getObject(TaskModel.TASK_RUNTIME);
			comp.setRuntime(runtime);
		}
	}

	@Override
	public void exit() {
		IRuntimeWorkingCopy runtime = (IRuntimeWorkingCopy) getTaskModel().getObject(TaskModel.TASK_RUNTIME);
		IPath path = runtime.getLocation();
		if (runtime.validate(null).getSeverity() != IStatus.ERROR) {
			ServerCorePlugin.setPreference("location" + runtime.getRuntimeType().getId(), path.toString());
		}
	}
}
