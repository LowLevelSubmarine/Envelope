package com.lowlevelsubmarine.envelope.build_provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lowlevelsubmarine.envelope.core.Envelope;
import com.lowlevelsubmarine.envelope.core.EnvelopeConnector;
import com.lowlevelsubmarine.envelope.util.HttpRequest;
import com.lowlevelsubmarine.envelope.util.JsonSurfer;
import com.lowlevelsubmarine.envelope.versioning.VersionInterpreter;
import com.lowlevelsubmarine.envelope.versioning.WrappedSemanticVersionInterpreter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.LinkedList;

public class GitHubReleasesBuildProvider implements BuildProvider {

    private static final String API_LIST_RELEASES = "https://api.github.com/repos/{owner}/{repo}/releases?accept=application/vnd.github.v3+json&per_page=100";
    private final URL urlApiListReleases;
    private final VersionProvider versionProvider;

    public GitHubReleasesBuildProvider(String user, String repo) {
        this(user, repo, new DefaultVersionProvider());
    }

    public GitHubReleasesBuildProvider(String user, String repo, VersionProvider versionProvider) throws InvalidParameterException {
        try {
            this.urlApiListReleases = new URL(API_LIST_RELEASES.replace("{owner}", user).replace("{repo}", repo));
        } catch (MalformedURLException e) {
            throw new InvalidParameterException();
        }
        this.versionProvider = versionProvider;
    }

    @Override
    public LinkedList<Build> getAvailableBuilds() throws IOException {
        LinkedList<Build> builds = new LinkedList<>();
        JsonArray root = JsonParser.parseString(HttpRequest.get(this.urlApiListReleases)).getAsJsonArray();
        for (JsonElement element : root) {
            JsonSurfer surfer = new JsonSurfer(element);
            if (surfer.get("assets").getAsJsonArray().size() != 0) {
                Build build = new Build(
                        surfer.get("name").getAsString(),
                        this.versionProvider.getVersion(surfer.get("tag_name").getAsString(), surfer.get("name").getAsString()),
                        new URL(surfer.get("assets", 0, "browser_download_url").getAsString())
                );
                String changelog = surfer.get("body").getAsString();
                if (!changelog.isEmpty()) {
                    build.setChangelog(changelog);
                }
                builds.add(build);
            }
        }
        return builds;
    }

    public interface VersionProvider {
        String getVersion(String releaseTag, String releaseName);
    }

    private static class DefaultVersionProvider implements VersionProvider {
        @Override
        public String getVersion(String releaseTag, String releaseName) {
            return releaseTag;
        }
    }

}
