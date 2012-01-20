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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.editor.PDEFormPage;
import org.eclipse.pde.internal.ui.elements.DefaultTableProvider;
import org.eclipse.pde.internal.ui.util.SWTUtil;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.virgo.ide.bundlerepository.domain.Artefact;
import org.eclipse.virgo.ide.bundlerepository.domain.ArtefactRepository;
import org.eclipse.virgo.ide.bundlerepository.domain.LibraryArtefact;
import org.eclipse.virgo.ide.bundlerepository.domain.OsgiVersion;
import org.eclipse.virgo.ide.manifest.core.IHeaderConstants;
import org.eclipse.virgo.ide.manifest.core.editor.model.ImportLibraryHeader;
import org.eclipse.virgo.ide.manifest.core.editor.model.ImportLibraryObject;
import org.eclipse.virgo.ide.runtime.core.provisioning.RepositoryUtils;

/**
 * @author Christian Dupuis
 * @author Leo Dos Santos
 */
public class BundleImportLibrarySection extends AbstractImportSection {

	private static final String DESCRIPTION = Messages.BundleImportLibrarySection_Description;

	private static final int ADD_INDEX = 0;

	private static final int ADD_REMOTE_BUNDLE_INDEX = 1;

	private static final int REMOVE_INDEX = 2;

	private static final int PROPERTIES_INDEX = 3;

	public BundleImportLibrarySection(PDEFormPage page, Composite parent) {
		super(page, parent, Section.DESCRIPTION, new String[] { "",
				Messages.BundleImportLibrarySection_DownloadButton, "", "" });

		getSection().setText(Messages.BundleImportLibrarySection_Title);
		getSection().setDescription(DESCRIPTION);
		getTablePart().setEditable(false);
	}

	class ImportLibraryContentProvider extends DefaultTableProvider {
		public Object[] getElements(Object parent) {
			ImportLibraryHeader header = (ImportLibraryHeader) getBundle().getManifestHeader(
					IHeaderConstants.IMPORT_LIBRARY);
			if (header == null) {
				return new Object[0];
			}
			else {
				return header.getImportedLibraries();
			}
		}
	}

	@Override
	protected IContentProvider getContentProvider() {
		return new ImportLibraryContentProvider();
	}

	@Override
	protected ITableLabelProvider getLabelProvider() {
		return new ImportLibraryLabelProvider();
	}

	private void setElements(ImportListSelectionDialog dialog, boolean addRemote) {
		IProject project = ((BundleManifestEditor) this.getPage().getEditor()).getCommonProject();
		Collection<LibraryArtefact> libraries = null;
		if (addRemote) {
			ArtefactRepository bundleRepository = RepositoryUtils.searchForArtifacts("", false, true); //$NON-NLS-1$
			libraries = bundleRepository.getLibraries();
		}
		else {
			libraries = RepositoryUtils.getImportLibraryProposals(project, ""); //$NON-NLS-1$
			removeExistingLibraries(libraries);
		}
		dialog.setElements(libraries.toArray());
	}

	private void removeExistingLibraries(Collection<LibraryArtefact> bundles) {
		ImportLibraryHeader header = (ImportLibraryHeader) getBundle().getManifestHeader(
				IHeaderConstants.IMPORT_LIBRARY);
		Set<LibraryArtefact> filteredElements = new HashSet<LibraryArtefact>();

		if (header != null) {
			ImportLibraryObject[] filter = header.getImportedLibraries();
			for (LibraryArtefact proposal : bundles) {
				for (ImportLibraryObject imported : filter) {
					if (proposal.getSymbolicName().equalsIgnoreCase(imported.getId())) {
						filteredElements.add(proposal);
					}
				}
			}
			bundles.removeAll(filteredElements);
		}
	}

	@Override
	protected void handleAdd() {
		internalHandleAdd(false);
		return;
	}

	private void internalHandleAdd(final boolean addRemote) {

		final ImportListSelectionDialog dialog = new ImportListSelectionDialog(PDEPlugin.getActiveWorkbenchShell(),
				new BundleImportDialogLabelProvider());

		Runnable runnable = new Runnable() {
			public void run() {
				setElements(dialog, addRemote);
				dialog.setMultipleSelection(true);
				dialog.setTitle(Messages.BundleImportLibrarySection_SelectionDialogTitle);
				dialog.setMessage(Messages.BundleImportLibrarySection_SelectionDialogMessage);
				dialog.create();
				SWTUtil.setDialogSize(dialog, 400, 500);
			}
		};

		BusyIndicator.showWhile(Display.getCurrent(), runnable);
		if (dialog.open() == Window.OK) {

			Object[] selected = dialog.getResult();

			if (addRemote) {
				addRemoteLibraries(selected);
			}
			else {
				addLocalLibraries(selected);
			}
		}

	}

	private void addLocalLibraries(Object[] selected) {
		ImportLibraryHeader importLibraryHeader = (ImportLibraryHeader) getBundle().getManifestHeader(
				IHeaderConstants.IMPORT_LIBRARY);
		for (Object currSelectedElement : selected) {
			LibraryArtefact currBundle = (LibraryArtefact) currSelectedElement;
			if (null == importLibraryHeader) {
				getBundle().setHeader(IHeaderConstants.IMPORT_LIBRARY, ""); //$NON-NLS-1$
				importLibraryHeader = (ImportLibraryHeader) getBundle().getManifestHeader(
						IHeaderConstants.IMPORT_LIBRARY);
			}

			String versionString = null;
			OsgiVersion osgiVers = currBundle.getVersion();
			if (osgiVers.getMajor() != 0 || osgiVers.getMinor() != 0 || osgiVers.getService() != 0
					|| (osgiVers.getQualifier() != null && !osgiVers.getQualifier().trim().equals(""))) { //$NON-NLS-1$
				versionString = "[" + currBundle.getVersion().toString() + "," + currBundle.getVersion().toString() //$NON-NLS-1$ //$NON-NLS-2$
						+ "]"; //$NON-NLS-1$
			}
			importLibraryHeader.addLibrary(currBundle.getSymbolicName(), versionString);
		}
	}

	private void addRemoteLibraries(Object[] selected) {
		ImportLibraryHeader importLibraryHeader = (ImportLibraryHeader) getBundle().getManifestHeader(
				IHeaderConstants.IMPORT_LIBRARY);

		Set<Artefact> remoteArtifactDefinitions = new HashSet<Artefact>(selected.length);

		for (Object currSelectedElement : selected) {
			remoteArtifactDefinitions.add((Artefact) currSelectedElement);
		}

		IProject project = ((BundleManifestEditor) this.getPage().getEditor()).getCommonProject();
		RepositoryUtils.downloadArifacts(remoteArtifactDefinitions, project, Display.getDefault().getActiveShell(),
				false);

		for (Object currSelectedElement : selected) {
			LibraryArtefact currLibrary = (LibraryArtefact) currSelectedElement;
			if (null == importLibraryHeader) {
				getBundle().setHeader(IHeaderConstants.IMPORT_LIBRARY, ""); //$NON-NLS-1$
				importLibraryHeader = (ImportLibraryHeader) getBundle().getManifestHeader(
						IHeaderConstants.IMPORT_LIBRARY);
			}

			String versionString = null;
			OsgiVersion osgiVers = currLibrary.getVersion();
			if (osgiVers.getMajor() != 0 || osgiVers.getMinor() != 0 || osgiVers.getService() != 0
					|| (osgiVers.getQualifier() != null && !osgiVers.getQualifier().trim().equals(""))) { //$NON-NLS-1$
				versionString = "[" + currLibrary.getVersion().toString() + "," + currLibrary.getVersion().toString() //$NON-NLS-1$
						+ "]";
			}

			if (importLibraryHeader.hasElement(currLibrary.getSymbolicName())) {
				importLibraryHeader.removeLibrary(currLibrary.getSymbolicName());
			}
			importLibraryHeader.addLibrary(currLibrary.getSymbolicName(), versionString);
		}
	}

	@Override
	protected void handleRemove() {
		Object[] removed = ((IStructuredSelection) fViewer.getSelection()).toArray();
		for (Object element : removed) {
			ImportLibraryHeader header = (ImportLibraryHeader) getBundle().getManifestHeader(
					IHeaderConstants.IMPORT_LIBRARY);
			header.removeLibrary((ImportLibraryObject) element);
		}
	}

	class BundleImportDialogLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			return PDEPluginImages.DESC_JAR_LIB_OBJ.createImage();
		}

		@Override
		public String getText(Object element) {
			LibraryArtefact libraryArtifact = (LibraryArtefact) element;
			String label = libraryArtifact.getSymbolicName();
			if (null != libraryArtifact.getVersion()) {
				label += " " + libraryArtifact.getVersion();
			}
			return label;

		}
	}

	@Override
	protected int getAddIndex() {
		return ADD_INDEX;
	}

	@Override
	protected int getRemoveIndex() {
		return REMOVE_INDEX;
	}

	@Override
	protected int getPropertiesIndex() {
		return PROPERTIES_INDEX;
	}

	class ImportLibraryLabelProvider extends AbstractSectionViewerLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return PDEPluginImages.DESC_JAR_LIB_OBJ.createImage();
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ImportLibraryObject importLibraryObject = (ImportLibraryObject) element;
			String label = importLibraryObject.getValue();
			if (null != importLibraryObject.getVersion()) {
				label += " " + importLibraryObject.getVersion();
			}
			return label;
		}
	}

	@Override
	protected String getHeaderConstant() {
		return IHeaderConstants.IMPORT_LIBRARY;
	}

	@Override
	protected boolean shouldEnableProperties(Object[] selected) {
		if (selected.length == 0) {
			return false;
		}
		if (selected.length == 1) {
			return true;
		}

		String version = ((ImportLibraryObject) selected[0]).getVersion();
		boolean optional = ((ImportLibraryObject) selected[0]).isOptional();
		for (int i = 1; i < selected.length; i++) {
			ImportLibraryObject object = (ImportLibraryObject) selected[i];
			if (version == null) {
				if (object.getVersion() != null || !(optional == object.isOptional())) {
					return false;
				}
			}
			else if (!version.equals(object.getVersion()) || !(optional == object.isOptional())) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void handleOpenProperties() {
		Object[] selected = ((IStructuredSelection) fViewer.getSelection()).toArray();
		ImportLibraryObject first = (ImportLibraryObject) selected[0];
		BundleDependencyPropertiesDialog dialog = new BundleDependencyPropertiesDialog(isEditable(), false, false,
				first.isOptional(), first.getVersion(), true, true);
		dialog.create();
		SWTUtil.setDialogSize(dialog, 400, -1);
		if (selected.length == 1) {
			dialog.setTitle(((ImportLibraryObject) selected[0]).getValue());
		}
		else {
			dialog.setTitle(Messages.BundleImportLibrarySection_PropertiesDialogTitle);
		}
		if (dialog.open() == Window.OK && isEditable()) {
			String newVersion = dialog.getVersion();
			boolean newOptional = dialog.isOptional();
			for (Object element : selected) {
				ImportLibraryObject object = (ImportLibraryObject) element;
				if (!newVersion.equals(object.getVersion())) {
					object.setVersion(newVersion);
				}
				if (!newOptional == object.isOptional()) {
					object.setOptional(newOptional);
				}
			}
		}
	}

	@Override
	protected void buttonSelected(int index) {
		if (index == ADD_REMOTE_BUNDLE_INDEX) {
			internalHandleAdd(true);
		}
		else {
			super.buttonSelected(index);
		}
	}

}
