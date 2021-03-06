/*******************************************************************************
 * Copyright (c) 2009, 2011 SpringSource, a divison of VMware, Inc. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SpringSource, a division of VMware, Inc. - initial API and implementation
 *     SAP AG - moving to Eclipse Libra project and enhancements
 *******************************************************************************/
package org.eclipse.virgo.ide.runtime.internal.core;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.libra.framework.editor.core.model.IBundle;
import org.eclipse.virgo.ide.runtime.core.IServerBehaviour;
import org.eclipse.virgo.ide.runtime.core.IServerVersionHandler;
import org.eclipse.virgo.ide.runtime.core.ServerCorePlugin;
import org.eclipse.virgo.ide.runtime.core.ServerUtils;
import org.eclipse.virgo.ide.runtime.internal.core.command.GenericJmxServerDeployCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.IServerCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxBundleAdminExecuteCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxBundleAdminServerCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxServerDeployCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxServerPingCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxServerRefreshCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxServerShutdownCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxServerUndeployCommand;
import org.eclipse.virgo.ide.runtime.internal.core.command.JmxServerUpdateCommand;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.util.PublishHelper;


/**
 * {@link IServerVersionHandler} for version 1.0 of the dm Server.
 * @author Christian Dupuis
 * @author Kaloyan Raev
 * @since 1.0.0
 */
public class Server10Handler implements IServerVersionHandler {

	private static final String DEPLOYER_MBEAN_NAME = "com.springsource.server:type=Deployer";

	private static final String RECOVERY_MONITOR_MBEAN_NAME = "com.springsource.server:type=RecoveryMonitor";

	private static final String SHUTDOWN_MBEAN_NAME = "com.springsource.server:type=Shutdown";

	/**
	 * {@inheritDoc}
	 */
	public IStatus canAddModule(IModule module) {
		return Status.OK_STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getExcludedRuntimeProgramArguments(boolean starting) {
		List<String> list = new ArrayList<String>();
		return list.toArray(new String[list.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPath getRuntimeBaseDirectory(IServer server) {
		return server.getRuntime().getLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRuntimeClass() {
		return "com.springsource.server.kernel.bootstrap.Bootstrap";
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IRuntimeClasspathEntry> getRuntimeClasspath(IPath installPath) {
		List<IRuntimeClasspathEntry> cp = new ArrayList<IRuntimeClasspathEntry>();

		IPath binPath = installPath.append("lib");
		if (binPath.toFile().exists()) {
			File libFolder = binPath.toFile();
			for (File library : libFolder.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isFile() && pathname.toString().endsWith(".jar");
				}
			})) {
				IPath path = binPath.append(library.getName());
				cp.add(JavaRuntime.newArchiveRuntimeClasspathEntry(path));
			}
		}

		return cp;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getRuntimeProgramArguments(IServerBehaviour IServerBehaviour) {
		return new String[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getRuntimeVMArguments(IServerBehaviour behaviour, IPath installPath, IPath configPath,
			IPath deployPath) {
		List<String> list = new ArrayList<String>();
		list.add("-Djava.rmi.server.hostname=127.0.0.1");
		list.add("-Dcom.springsource.server.home=\""
				+ ServerUtils.getServer(behaviour).getRuntimeBaseDirectory().toOSString() + "\"");
		list.add("-Djava.io.tmpdir=\"" + ServerUtils.getServer(behaviour).getRuntimeBaseDirectory().toOSString()
				+ "/work/tmp/\"");
		list.add("-Dcom.sun.management.jmxremote");
		list.add("-Dcom.sun.management.jmxremote.port=" + ServerUtils.getServer(behaviour).getMBeanServerPort());
		list.add("-Dcom.sun.management.jmxremote.authenticate=false");
		list.add("-Dcom.sun.management.jmxremote.ssl=false");
		list.add("-Dcom.springsource.server.clean="
				+ Boolean.toString(ServerUtils.getServer(behaviour).shouldCleanStartup()));
		return list.toArray(new String[list.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public IStatus verifyInstallation(IPath installPath) {
		String version = installPath.append("lib").append(".version").toOSString();
		File versionFile = new File(version);
		if (versionFile.exists()) {
			return new Status(Status.ERROR, ServerCorePlugin.PLUGIN_ID,
					".version file in lib directory exists. Make sure to point to a dm Server 1.0 installation.");
		}
		return Status.OK_STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getProfilePath(IRuntime runtime) {
		return runtime.getLocation().append("lib").append("server.profile").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getConfigPath(IRuntime runtime) {
		return runtime.getLocation().append("config").append("server.config").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserLevelBundleRepositoryPath(IRuntime runtime) {
		return runtime.getLocation().append("repository").append("bundles").append("usr").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserLevelLibraryRepositoryPath(IRuntime runtime) {
		return runtime.getLocation().append("repository").append("libraries").append("usr").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDeployerMBeanName() {
		return DEPLOYER_MBEAN_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRecoveryMonitorMBeanName() {
		return RECOVERY_MONITOR_MBEAN_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getShutdownMBeanName() {
		return SHUTDOWN_MBEAN_NAME;
	}

	public IServerCommand<Boolean> getServerPingCommand(IServerBehaviour IServerBehaviour) {
		return new JmxServerPingCommand(IServerBehaviour);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<Void> getServerShutdownCommand(IServerBehaviour IServerBehaviour) {
		return new JmxServerShutdownCommand(IServerBehaviour);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<DeploymentIdentity> getServerDeployCommand(IServerBehaviour IServerBehaviour, IModule module) {
		return new JmxServerDeployCommand(IServerBehaviour, module);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<Void> getServerRefreshCommand(IServerBehaviour IServerBehaviour, IModule module,
			String bundleSymbolicName) {
		return new JmxServerRefreshCommand(IServerBehaviour, module, bundleSymbolicName);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<Void> getServerUpdateCommand(IServerBehaviour IServerBehaviour, IModule module,
			IModuleFile moduleFile, DeploymentIdentity identity, String bundleSymbolicName, String targetPath) {
		return new JmxServerUpdateCommand(IServerBehaviour, module, moduleFile, identity, bundleSymbolicName,
				targetPath);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<Void> getServerUndeployCommand(IServerBehaviour IServerBehaviour, IModule module) {
		return new JmxServerUndeployCommand(IServerBehaviour, module);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<DeploymentIdentity> getServerDeployCommand(IServerBehaviour IServerBehaviour,
			URI connectorBundleUri) {
		return new GenericJmxServerDeployCommand(IServerBehaviour, connectorBundleUri);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<Map<Long, IBundle>> getServerBundleAdminCommand(IServerBehaviour serverBehaviour) {
		return new JmxBundleAdminServerCommand(serverBehaviour);
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommand<String> getServerBundleAdminExecuteCommand(IServerBehaviour serverBehaviour, String command) {
		return new JmxBundleAdminExecuteCommand(serverBehaviour, command);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void preStartup(IServerBehaviour serverBehaviour) {
		if (ServerUtils.getServer(serverBehaviour).shouldCleanStartup()) {
			File serverHome = ServerUtils.getServer(serverBehaviour).getRuntimeBaseDirectory().toFile();
			PublishHelper.deleteDirectory(new File(serverHome, "work"), new NullProgressMonitor());
			PublishHelper.deleteDirectory(new File(serverHome, "serviceability"), new NullProgressMonitor());
		}
	}
}
