package com.lowlevelsubmarine.envelope.core;

import com.google.gson.Gson;
import com.lowlevelsubmarine.envelope.build_provider.Build;
import com.lowlevelsubmarine.envelope.build_provider.BuildComparator;
import com.lowlevelsubmarine.envelope.build_provider.BuildProvider;
import com.lowlevelsubmarine.envelope.build_provider.GitHubReleasesBuildProvider;
import com.lowlevelsubmarine.envelope.util.FileBrowser;
import com.lowlevelsubmarine.envelope.util.FileReleaseWatcher;
import com.lowlevelsubmarine.envelope.versioning.IncompatibleVersionException;
import com.lowlevelsubmarine.envelope.versioning.Version;
import com.lowlevelsubmarine.envelope.versioning.VersionInterpreter;
import com.lowlevelsubmarine.envelope.versioning.WrappedSemanticVersionInterpreter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Envelope {

    private static final transient File UPDATE_INFO_FILE = FileBrowser.getJARFile().treeSibling("envelope_update_info.json").getFile();

    private final UpdateInfo updateInfo = UpdateInfo.load(UPDATE_INFO_FILE); //Can be null -> no update occurred
    private final EnvelopeConnector connector;

    public Envelope(EnvelopeConnector envelopeConnector) {
        this.connector = envelopeConnector;
        if (this.updateInfo != null && this.updateInfo.jarFile != null ) {
            new FileReleaseWatcher(new File(this.updateInfo.jarFile), (File file) -> {
                if (!file.delete()) {
                    System.out.println("[Envelope] Old build could not be deleted!");
                }
            });
        }
    }

    public boolean isUpToDate() throws IOException {
        try {
            return versionFromString(this.connector.getCurrentVersion()).isEqualTo(
                    versionFromString(getLatestAvailableBuild().getVersion())
            );
        } catch (IncompatibleVersionException e) {
            e.printStackTrace();
            return false;
        }
   }

   private Version versionFromString(String string) throws IncompatibleVersionException {
        return this.connector.getVersionInterpreter().interpret(string);
   }

    /*
     * @author LowLevelSubmarine
     * @return Whether the current process is due to a envelope update or not.
     */
    public boolean isUpdated() {
        return this.updateInfo != null;
    }

    /*
     * @author LowLevelSubmarine
     * @return The version that was running before the update was executed. Null if !isUpdate()
     */
    public String getLastVersion() {
        if (this.updateInfo != null) {
            return this.updateInfo.lastVersion;
        }
        return null;
    }

    /*
     * @author LowLevelSubmarine
     * @return A list of all the available builds to upgrade (or even downgrade) to.
     */
    public LinkedList<Build> getAvailableBuilds() throws IOException {
        return this.connector.getBuildProvider().getAvailableBuilds();
    }

    /*
     * @author LowLevelSubmarine
     * @return The build that has the highest version of all of the available builds.
     */
    public Build getLatestAvailableBuild() throws IOException {
        LinkedList<Build> builds = getAvailableBuilds();
        builds.sort(new BuildComparator(this.connector.getVersionInterpreter()));
        return builds.getFirst();
    }

    /*
     * Downloads the builds jar thread-blocking!
     * @author LowLevelSubmarine
     * @param build The build that should be downloaded.
     * @return A Update object that represents a installable download.
     */
    public Update download(Build build) {
        return new Update(build);
    }

    /*
     * Install the given update.
     * At first the update will be checked for possible problems.
     * Envelope will then notify the EnvelopeConnector about the shutdown.
     * Then the UpdateInfo file will be created and Envelope starts the new JAR.
     * @author LowLevelSubmarine
     * @param update The previously downloaded Update
     */
    public void install(Update update) throws MalformedUpdateException, IOException {
        update.verify();
        connector.prepareShutdown();
        saveUpdateInfo();
        update.runJAR();
        System.exit(0);
    }

    /*
     * Save the location of the currently running JAR and the current version in the update info file.
     * @author LowLevelSubmarine
     */
    private void saveUpdateInfo() {
        UpdateInfo json = new UpdateInfo();
        File jarFile = FileBrowser.getJARFile().getFile();
        if (jarFile.isFile()) {
            json.jarFile = jarFile.getAbsolutePath();
        }
        json.lastVersion = this.connector.getCurrentVersion();
        try {
            json.save(UPDATE_INFO_FILE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Envelope] Something went wrong wile saving the update info file!");
        }
    }

}
