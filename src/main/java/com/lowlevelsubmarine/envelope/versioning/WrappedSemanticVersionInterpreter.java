package com.lowlevelsubmarine.envelope.versioning;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WrappedSemanticVersionInterpreter implements VersionInterpreter {

    Pattern SEMANTIC_VERSION_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+");

    @Override
    public Version interpret(String string) throws IncompatibleVersionException {
        Matcher matcher = SEMANTIC_VERSION_PATTERN.matcher(string);
        if (matcher.find()) {
            return new Version(matcher.group());
        } else {
            throw new IncompatibleVersionException();
        }
    }

}
