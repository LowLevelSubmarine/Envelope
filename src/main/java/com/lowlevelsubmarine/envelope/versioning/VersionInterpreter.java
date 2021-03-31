package com.lowlevelsubmarine.envelope.versioning;

public interface VersionInterpreter {

    Version interpret(String string) throws IncompatibleVersionException;

}
