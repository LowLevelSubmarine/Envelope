package com.lowlevelsubmarine.envelope.versioning;

import java.util.LinkedList;

public class Version {

    private final LinkedList<Integer> subVersions;
    private String string;

    public Version(int version) {
        this.subVersions = new LinkedList<>();
        this.subVersions.add(version);
    }

    public Version(LinkedList<Integer> subVersions) {
        this.subVersions = subVersions;
    }

    public Version(String string) throws IncompatibleVersionException {
        this.subVersions = new LinkedList<>();
        for (String subString : string.split("\\.")) {
            try {
                this.subVersions.add(Integer.parseInt(subString));
            } catch (NumberFormatException e) {
                throw new IncompatibleVersionException();
            }
        }
    }

    public boolean isHigherThan(Version version) throws IncompatibleVersionException {
        if (version instanceof Version) {
            Version other = (Version) version;
            if (other.subVersions.size() != this.subVersions.size()) {
                return other.subVersions.size() < this.subVersions.size();
            }
            for (int i = 0; i < subVersions.size(); i++) {
                if (!this.subVersions.get(i).equals(other.subVersions.get(i))) {
                    return this.subVersions.get(i) > other.subVersions.get(i);
                }
            }
        }
        throw new IncompatibleVersionException();
    }

    public boolean isLowerThan(Version version) throws IncompatibleVersionException {
        return !isHigherThan(version) && !isEqualTo(version);
    }

    public boolean isEqualTo(Version version) {
        if (version instanceof Version) {
            Version other = (Version) version;
            if (other.subVersions.size() != this.subVersions.size()) {
                return false;
            }
            for (int i = 0; i < subVersions.size(); i++) {
                if (!this.subVersions.get(i).equals(other.subVersions.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
}
