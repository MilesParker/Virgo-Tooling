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
package org.eclipse.virgo.ide.export;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.virgo.ide.facet.core.FacetCorePlugin;
import org.eclipse.virgo.ide.manifest.core.BundleManifestCorePlugin;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import org.eclipse.virgo.util.osgi.manifest.BundleManifest;

/**
 * Export wizard page for exporting bundle project
 * @author Christian Dupuis
 * @author Terry Hon
 */
public class BundleExportWizardPage extends AbstractProjectExportWizardPage {

	public BundleExportWizardPage(IStructuredSelection selection) {
		super("bundleExportWizardPage", selection);

		setTitle("JAR File Specification");
		setDescription("Define which bundle project should be exported into the JAR.");
	}

	@Override
	protected ViewerFilter getTreeViewerFilter() {
		return new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot root = (IPackageFragmentRoot) element;
					return !root.isArchive() && !root.isExternal();
				}
				else if (element instanceof IJavaProject) {
					IJavaProject javaProject = (IJavaProject) element;
					try {
						IFacetedProject facetedProject = ProjectFacetsManager.create(javaProject
								.getProject());
						if (facetedProject != null) {
							return facetedProject.hasProjectFacet(ProjectFacetsManager
									.getProjectFacet(FacetCorePlugin.BUNDLE_FACET_ID)
									.getDefaultVersion());
						}
					}
					catch (CoreException e) {
					}
					return false;
				}
				return true;
			}
		};
	}

	@Override
	protected ITreeContentProvider getTreeContentProvider() {
		return new StandardJavaElementContentProvider() {
			@Override
			public Object[] getChildren(Object element) {
				if (element instanceof IJavaProject) {
					return NO_CHILDREN;
				}
				return super.getChildren(element);
			}
		};
	}

	@Override
	protected Object getInput() {
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
	}

	@Override
	protected String getExtension() {
		return ".jar";
	}

	@Override
	protected String getDestinationLabel() {
		return "JAR file:";
	}

	@Override
	protected String getSymbolicName(BundleManifest bundleManifest) {
		if (bundleManifest.getBundleSymbolicName() != null) {
			return bundleManifest.getBundleSymbolicName().getSymbolicName();
		}
		return null;
	}

	@Override
	protected String getVersion(BundleManifest bundleManifest) {
		return (bundleManifest.getBundleVersion() != null ? bundleManifest.getBundleVersion().toString() : "");
	}

	@Override
	protected BundleManifest getBundleManifest(IProject project) {
		if (project != null) {
			return BundleManifestCorePlugin.getBundleManifestManager().getBundleManifest(
					JavaCore.create(project));
		}
		return null;
	}

}
