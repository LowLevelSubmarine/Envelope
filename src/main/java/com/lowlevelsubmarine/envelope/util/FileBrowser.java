package com.lowlevelsubmarine.envelope.util;

import com.lowlevelsubmarine.envelope.core.Envelope;

import java.io.File;

public class FileBrowser {

    private File file;

    public FileBrowser(String file) {
        this(new File(file));
    }

    public FileBrowser(File file) {
        this.file = file;
    }

    public FileBrowser treeUp() {
        return new FileBrowser(this.file.getParentFile());
    }

    public FileBrowser treeDown(String name) {
        return new FileBrowser(this.file.getAbsolutePath() + File.pathSeparator + name);
    }

    public FileBrowser treeSibling(String name) {
        return treeUp().treeDown(name);
    }

    public File getFile() {
        return this.file;
    }

    public static FileBrowser getJARFile() {
        return new FileBrowser(Envelope.class.getProtectionDomain().getCodeSource().getLocation().getFile());
    }

}
