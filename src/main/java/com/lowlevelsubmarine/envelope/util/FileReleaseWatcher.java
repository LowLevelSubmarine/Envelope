package com.lowlevelsubmarine.envelope.util;

import java.io.File;

public class FileReleaseWatcher {

    private static final int MIN_MS = 200;
    private static final int MAX_MS = 10*1000;
    private static final int STATIC_INCREASE_MS = 0;
    private static final float MS_MULTIPLIER = 1.2F;

    public FileReleaseWatcher(File file, Hook hook) {
       new Thread(() -> {
           try {
               int ms = MIN_MS;
               long start = System.currentTimeMillis();
               while (!file.renameTo(file)) {
                   System.out.println("Time taken: " + (System.currentTimeMillis() - start));
                   Thread.sleep(ms);
                   ms += STATIC_INCREASE_MS;
                   ms *= MS_MULTIPLIER;
                   if (ms > MAX_MS) {
                       ms = MAX_MS;
                   }
                   System.out.println(ms);
                   start = System.currentTimeMillis();
               }
               hook.onRelease(file);
           } catch (Exception e) {
               e.printStackTrace();
           }
        }).start();
    }

    public interface Hook {
        void onRelease(File file);
    }

}
