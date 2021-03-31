package com.lowlevelsubmarine.envelope.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {

    private final URL origin;
    private final File destination;

    public FileDownloader(String origin, File destination) throws MalformedURLException {
        this(new URL(origin), destination);
    }

    public FileDownloader(URL origin, File destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public void download() {
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(this.origin.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(this.destination);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
