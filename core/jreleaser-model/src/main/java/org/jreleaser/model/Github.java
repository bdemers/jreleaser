/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.model;

import java.util.Map;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
public class Github extends GitService {
    public static final String NAME = "github";

    private String targetCommitish;
    private boolean draft;
    private Boolean prerelease;

    public Github() {
        super(NAME);
        setHost("github.com");
        setRepoUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}");
        setRepoCloneUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}.git");
        setCommitUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}/commits");
        setDownloadUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}/releases/download/{{tagName}}/{{artifactFileName}}");
        setReleaseNotesUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}/releases/tag/{{tagName}}");
        setLatestReleaseUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}/releases/latest");
        setIssueTrackerUrlFormat("https://{{repoHost}}/{{repoOwner}}/{{repoName}}/issues");
    }

    void setAll(Github service) {
        super.setAll(service);
        this.targetCommitish = service.targetCommitish;
        this.draft = service.draft;
        this.prerelease = service.prerelease;
    }

    public String getTargetCommitish() {
        return targetCommitish;
    }

    public void setTargetCommitish(String targetCommitish) {
        this.targetCommitish = targetCommitish;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isPrerelease() {
        return prerelease != null && prerelease;
    }

    public void setPrerelease(Boolean prerelease) {
        this.prerelease = prerelease;
    }

    public boolean isPrereleaseSet() {
        return prerelease != null;
    }

    @Override
    public String getBranch() {
        return getTargetCommitish();
    }

    @Override
    public void setBranch(String branch) {
        setTargetCommitish(branch);
    }

    @Override
    public String getReverseRepoHost() {
        return "com.github";
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = super.asMap();
        map.put("targetCommitish", targetCommitish);
        map.put("draft", draft);
        map.put("prerelease", isPrerelease());
        return map;
    }
}
