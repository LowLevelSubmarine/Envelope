package com.lowlevelsubmarine.envelope.core;

import com.lowlevelsubmarine.envelope.build_provider.BuildProvider;
import com.lowlevelsubmarine.envelope.versioning.VersionInterpreter;

public interface EnvelopeConnector {

    BuildProvider getBuildProvider();
    VersionInterpreter getVersionInterpreter();
    String getCurrentVersion();

    void prepareShutdown();

}
