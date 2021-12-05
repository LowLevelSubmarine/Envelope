package com.lowlevelsubmarine.envelope.core;

import com.lowlevelsubmarine.envelope.build_provider.Build;
import com.lowlevelsubmarine.envelope.util.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Update {

    private static final Pattern PATTERN_FILENAME = Pattern.compile("[^\\/\\\\]+\\Z");

    private final Build build;
    private final File localJAR;

    Update(Build build) {
        this.build = build;
        this.localJAR = new File(fileNameFromURL(build.getDownloadURL()));
        new FileDownloader(build.getDownloadURL(), this.localJAR).download();
    }

    Update(Build build, File localJAR) {
        this.build = build;
        this.localJAR = localJAR;
    }

    void runJAR() throws IOException {
        new Executor(this.localJAR).exec();
    }

    public Build getBuild() {
        return this.build;
    }

    public boolean delete() {
        return this.localJAR.delete();
    }

    public void verify() throws MalformedUpdateException {
        if (!this.localJAR.exists()) throw new MalformedUpdateException();
    }

    private static String fileNameFromURL(URL url) {
        Matcher matcher = PATTERN_FILENAME.matcher(url.getFile());
        matcher.find();
        return matcher.group();
    }

}
