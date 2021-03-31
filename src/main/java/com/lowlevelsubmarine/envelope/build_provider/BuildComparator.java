package com.lowlevelsubmarine.envelope.build_provider;

import com.lowlevelsubmarine.envelope.versioning.IncompatibleVersionException;
import com.lowlevelsubmarine.envelope.versioning.Version;
import com.lowlevelsubmarine.envelope.versioning.VersionInterpreter;

import java.util.Comparator;

public class BuildComparator implements Comparator<Build> {

    private final VersionInterpreter interpreter;

    public BuildComparator(VersionInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int compare(Build b1, Build b2) {
        try {
            Version v1 = interpreter.interpret(b1.getVersion());
            Version v2 = interpreter.interpret(b2.getVersion());
            if (v1.isHigherThan(v2)) {
                return 1;
            } else if (v1.isEqualTo(v2)) {
                return 0;
            } else {
                return -1;
            }
        } catch (IncompatibleVersionException e) {
            return 0;
        }
    }

}
