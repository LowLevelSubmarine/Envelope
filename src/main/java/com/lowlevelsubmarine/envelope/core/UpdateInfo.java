package com.lowlevelsubmarine.envelope.core;

import com.google.gson.Gson;

import java.io.*;

public class UpdateInfo implements Serializable {

    /*
     * Java representation of a json file that preserves status and update information from a update.
     * The file is only meant to exist if an update has occurred and thus must be deleted after any successful update.
     */

    private static final transient Gson GSON = new Gson();

    //Holds the version from before the update
    public String lastVersion;
    //Holds the path of the jarFile that was running before the update which should be deleted
    public String jarFile;

    public static UpdateInfo load(File file) {
        try {
            FileReader reader = new FileReader(file);
            UpdateInfo info = GSON.fromJson(reader, UpdateInfo.class);
            reader.close();
            file.delete();
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    public void save(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        GSON.toJson(this, writer);
        writer.close();
    }

}
