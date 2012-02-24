/**
 * Play
 * 24.02.2012
 * @author Philipp Haussleiter
 *
 */
package org.jenkins.plugins.play.tasks;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.slaves.NodeSpecific;
import hudson.tasks.Builder;
import hudson.tasks.Messages;
import hudson.tools.DownloadFromUrlInstaller;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

public class Play extends Builder {

    private final static String PLAY_1_INSTALLATION_COMMON_FILE = "bin/play";

    /**
     * Represents a Play installation in a system.
     */
    public static final class PlayInstallation extends ToolInstallation implements EnvironmentSpecific<PlayInstallation>, NodeSpecific<PlayInstallation> {

        @DataBoundConstructor
        public PlayInstallation(String name, String home, List<? extends ToolProperty<?>> properties) {
            super(Util.fixEmptyAndTrim(name), Util.fixEmptyAndTrim(home), properties);
        }

        public File getHomeDir() {
            return new File(getHome());
        }
        private static final long serialVersionUID = 1L;

        @Override
        public PlayInstallation forEnvironment(EnvVars environment) {
            return new PlayInstallation(getName(), environment.expand(getHome()), getProperties().toList());
        }

        @Override
        public PlayInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
            return new PlayInstallation(getName(), translateFor(node, log), getProperties().toList());
        }
    }

    public static class PlayInstaller extends DownloadFromUrlInstaller {

        @DataBoundConstructor
        public PlayInstaller(String id) {
            super(id);
        }

        @Extension
        public static final class DescriptorImpl extends DownloadFromUrlInstaller.DescriptorImpl<PlayInstaller> {

            @Override
            public String getDisplayName() {
                return Messages.InstallFromApache();
            }

            @Override
            public boolean isApplicable(Class<? extends ToolInstallation> toolType) {
                return toolType == PlayInstallation.class;
            }
        }
    }
}
