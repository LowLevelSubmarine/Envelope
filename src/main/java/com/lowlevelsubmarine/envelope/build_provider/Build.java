package com.lowlevelsubmarine.envelope.build_provider;

import com.lowlevelsubmarine.envelope.core.Envelope;

import java.net.URL;

public class Build {

    private Envelope envelope;
    private final String name;
    private final String version;
    private final URL downloadURL;
    private String changelog;

    public Build(String name, String version, URL downloadURL) {
        this.name = name;
        this.version = version;
        this.downloadURL = downloadURL;
    }

    public String getName() {
        return name;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getChangelog() {
        return changelog;
    }

    public String getVersion() {
        return version;
    }

    public URL getDownloadURL() {
        return downloadURL;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

}
