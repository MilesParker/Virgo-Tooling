/*******************************************************************************
 * Copyright (c) 2010 SpringSource, a divison of VMware, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SpringSource, a division of VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.virgo.ide.ui.editors.plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.virgo.ide.bundlerepository.domain.BundleArtefact;
import org.eclipse.virgo.ide.bundlerepository.domain.OsgiVersion;
import org.eclipse.virgo.ide.runtime.core.provisioning.RepositoryUtils;
import org.eclipse.virgo.ide.ui.ServerIdeUiPlugin;
import org.springframework.ide.eclipse.beans.ui.editor.contentassist.IContentAssistCalculator;
import org.springframework.ide.eclipse.beans.ui.editor.contentassist.IContentAssistContext;
import org.springframework.ide.eclipse.beans.ui.editor.contentassist.IContentAssistProposalRecorder;
import org.springframework.ide.eclipse.beans.ui.editor.contentassist.NamespaceContentAssistProcessorSupport;
import org.springframework.ide.eclipse.beans.ui.editor.util.BeansEditorUtils;


/**
 * Content assist processor for plan files.
 * @author Christian Dupuis
 * @since 2.3.1
 */
public class PlanContentAssistProcessor extends NamespaceContentAssistProcessorSupport {

	@Override
	public void init() {
		registerContentAssistCalculator("type", new TypeContentAssistCalculator()); //$NON-NLS-1$
		registerContentAssistCalculator("name", new NameContentAssistCalculator()); //$NON-NLS-1$
		registerContentAssistCalculator("version", new VersionContentAssistCalculator()); //$NON-NLS-1$
	}

	private static class TypeContentAssistCalculator implements IContentAssistCalculator {

		private static final List<String> TYPES = Arrays
				.asList(new String[] { "configuration", "bundle", "plan", "par" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		public void computeProposals(IContentAssistContext context, IContentAssistProposalRecorder recorder) {
			String prefix = context.getMatchString();
			for (String type : TYPES) {
				if (type.startsWith(prefix)) {
					recorder.recordProposal(null, 10, type, type);
				}
			}
		}
	}

	private static class NameContentAssistCalculator implements IContentAssistCalculator {

		public void computeProposals(IContentAssistContext context, IContentAssistProposalRecorder recorder) {
			String prefix = context.getMatchString();
			String type = BeansEditorUtils.getAttribute(context.getNode(), "type"); //$NON-NLS-1$
			if (type == null || "bundle".equals(type)) { //$NON-NLS-1$
				Set<BundleArtefact> bundles = RepositoryUtils.getImportBundleProposals(context.getFile().getProject(),
						prefix);
				for (BundleArtefact bundle : bundles) {
					recorder.recordProposal(ServerIdeUiPlugin.getImage("full/obj16/osgi_obj.gif"), 10, bundle //$NON-NLS-1$
							.getSymbolicName(), bundle.getSymbolicName());
				}
			}
		}
	}

	private static class VersionContentAssistCalculator implements IContentAssistCalculator {

		public void computeProposals(IContentAssistContext context, IContentAssistProposalRecorder recorder) {
			String prefix = context.getMatchString();
			String bundleId = BeansEditorUtils.getAttribute(context.getNode(), "name"); //$NON-NLS-1$
			String type = BeansEditorUtils.getAttribute(context.getNode(), "type"); //$NON-NLS-1$
			if (type == null || "bundle".equals(type)) { //$NON-NLS-1$
				Set<BundleArtefact> bundles = RepositoryUtils.getImportBundleProposals(context.getFile().getProject(),
						bundleId);
				for (BundleArtefact element : bundles) {
					if (element.getSymbolicName().equalsIgnoreCase(bundleId)) {
						List<String> proposalValues = getVersionProposals(element.getVersion());
						for (String proposalValue : proposalValues) {
							if (proposalValue.regionMatches(0, prefix, 0, prefix.length())) {
								recorder.recordProposal(null, 10, proposalValue, proposalValue);
							}
						}
					}
				}
			}
		}

		private List<String> getVersionProposals(OsgiVersion version) {
			Set<String> versionStrings = new LinkedHashSet<String>();
			versionStrings.add(version.toString());
			List<String> versions = new ArrayList<String>();
			for (String ver : RepositoryUtils.getVersionProposals(versionStrings)) {
				versions.add(ver);
			}
			return versions;
		}
	}
}
