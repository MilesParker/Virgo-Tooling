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

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.PDEFormPage;
import org.eclipse.pde.internal.ui.util.SharedLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.virgo.ide.export.ParExportWizard;
import org.eclipse.virgo.ide.ui.ServerIdeUiPlugin;
import org.springframework.ide.eclipse.beans.ui.graph.BeansGraphImages;

/**
 * @author Christian Dupuis
 */
public class ParOverviewPage extends PDEFormPage implements IHyperlinkListener {

	public static final String PAGE_ID = "par_overview"; //$NON-NLS-1$

	private static final String BUNDLE_CONTENT_SECTION_TEXT = org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_BundleContentSectionTitle;

	private ParGeneralInfoSection fInfoSection;

	private static String PAR_ACTION_SECTION_TEXT = org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_ParActionText
			+ org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_ParActionText2
			+ org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_ParActionText3;

	public ParOverviewPage(FormEditor editor) {
		super(editor, PAGE_ID, org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_Title);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setImage(ServerIdeUiPlugin.getImage("full/obj16/par_obj.gif")); //$NON-NLS-1$
		form.setText(PDEUIMessages.ManifestEditor_OverviewPage_title);
		fillBody(managedForm, toolkit);
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));

		Composite left = toolkit.createComposite(body);
		left.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		fInfoSection = new ParGeneralInfoSection(this, left);
		managedForm.addPart(fInfoSection);

		Composite right = toolkit.createComposite(body);
		right.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		right.setLayoutData(new TableWrapData(TableWrapData.FILL));
		createParActionSection(managedForm, right, toolkit);
		createParContentSection(managedForm, right, toolkit);
	}

	private void createParContentSection(IManagedForm managedForm, Composite parent, FormToolkit toolkit) {
		String sectionTitle;
		sectionTitle = org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_ParSectionTitle;
		Section section = createStaticSection(toolkit, parent, sectionTitle);

		Composite container = createStaticSectionClient(toolkit, section);

		FormText text = createClient(container, BUNDLE_CONTENT_SECTION_TEXT, true, toolkit);
		PDELabelProvider lp = PDEPlugin.getDefault().getLabelProvider();
		text.setImage("page", lp.get(PDEPluginImages.DESC_PAGE_OBJ, SharedLabelProvider.F_EDIT)); //$NON-NLS-1$
		text.addHyperlinkListener(this);
		section.setClient(container);
	}

	private void createParActionSection(IManagedForm managedForm, Composite parent, FormToolkit toolkit) {
		String sectionTitle = org.eclipse.virgo.ide.ui.editors.Messages.ParOverviewPage_ParActionsTitle;
		Section section = createStaticSection(toolkit, parent, sectionTitle);

		Composite container = createStaticSectionClient(toolkit, section);

		FormText text = createClient(container, PAR_ACTION_SECTION_TEXT, true, toolkit);
		text.setImage("export", BeansGraphImages.getImage(BeansGraphImages.IMG_OBJS_EXPORT_ENABLED)); //$NON-NLS-1$
		text.addHyperlinkListener(this);

		section.setClient(container);
	}

	protected final Section createStaticSection(FormToolkit toolkit, Composite parent, String text) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(text);
		section.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);
		return section;
	}

	protected Composite createStaticSectionClient(FormToolkit toolkit, Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		container.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		container.setLayoutData(data);
		return container;
	}

	protected final FormText createClient(Composite section, String content, boolean parseTags, FormToolkit toolkit) {
		FormText text = toolkit.createFormText(section, true);
		try {
			text.setText(content, parseTags, false);
		}
		catch (SWTException e) {
			text.setText(e.getMessage(), false, false);
		}
		return text;
	}

	public void linkActivated(HyperlinkEvent e) {
		if (e.getHref().equals("dependencies")) { //$NON-NLS-1$
			getEditor().setActivePage(ParXmlEditorPage.ID_EDITOR);
		}
		else if (e.getHref().equals("exportpar")) { //$NON-NLS-1$
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					ParExportWizard wizard = new ParExportWizard();
					WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
					wizard.init(PlatformUI.getWorkbench(),
							new StructuredSelection(new Object[] { fInfoSection.getParProject() }));
					dialog.open();
				}
			});
		}
	}

	public void linkEntered(HyperlinkEvent e) {
		// ignore
	}

	public void linkExited(HyperlinkEvent e) {
		// ignore
	}

}
