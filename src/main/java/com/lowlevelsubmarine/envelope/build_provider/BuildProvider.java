package com.lowlevelsubmarine.envelope.build_provider;

import java.io.IOException;
import java.util.LinkedList;


public interface BuildProvider {

    LinkedList<Build> getAvailableBuilds() throws IOException;

}
