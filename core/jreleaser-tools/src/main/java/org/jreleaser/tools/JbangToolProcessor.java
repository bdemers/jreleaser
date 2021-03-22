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
package org.jreleaser.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jreleaser.model.Distribution;
import org.jreleaser.model.JReleaserContext;
import org.jreleaser.model.Jbang;
import org.jreleaser.model.Project;
import org.jreleaser.model.tool.spi.ToolProcessingException;
import org.jreleaser.util.Constants;
import org.jreleaser.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.jreleaser.templates.TemplateUtils.trimTplExtension;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
public class JbangToolProcessor extends AbstractToolProcessor<Jbang> {
    public JbangToolProcessor(JReleaserContext context, Jbang jbang) {
        super(context, jbang);
    }

    @Override
    protected boolean doPackageDistribution(Distribution distribution, Map<String, Object> props) throws ToolProcessingException {
        copyPreparedFiles(distribution, props);
        return true;
    }

    @Override
    protected boolean verifyAndAddArtifacts(Map<String, Object> props,
                                            Distribution distribution) throws ToolProcessingException {
        return true;
    }

    @Override
    protected Set<String> resolveByExtensionsFor(Distribution.DistributionType type) {
        return Collections.emptySet();
    }

    @Override
    protected void fillToolProperties(Map<String, Object> props, Distribution distribution) throws ToolProcessingException {
        String distributionName = distribution.getName();
        String aliasName = distributionName;
        String aliasClassName = distributionName;
        if (context.getModel().getProject().isSnapshot()) {
            aliasName += "-snapshot";
            aliasClassName += "_snapshot";
        }

        props.put(Constants.KEY_MAIN_CLASS, distribution.getMainClass());
        props.put(Constants.KEY_GROUP_ID, distribution.getGroupId());
        props.put(Constants.KEY_ARTIFACT_ID, distribution.getArtifactId());
        props.put(Constants.KEY_JBANG_ALIAS_NAME, aliasName);
        props.put(Constants.KEY_JBANG_ALIAS_CLASS_NAME, aliasClassName);
    }

    @Override
    protected void writeFile(Project project, Distribution distribution, String content, Map<String, Object> props, String fileName)
        throws ToolProcessingException {
        fileName = trimTplExtension(fileName);

        Path outputDirectory = (Path) props.get(Constants.KEY_PREPARE_DIRECTORY);
        String aliasClassName = (String) props.get(Constants.KEY_JBANG_ALIAS_CLASS_NAME);
        Path outputFile = "jbang.java".equals(fileName) ?
            outputDirectory.resolve(aliasClassName.concat(".java")) :
            outputDirectory.resolve(fileName);

        writeFile(content, outputFile);
    }

    @Override
    protected void prepareWorkingCopy(Map<String, Object> props, Path directory) throws IOException {
        Path catalog = directory.resolve("jbang-catalog.json");

        if (catalog.toFile().exists()) {
            // read previous catalog
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode previous = objectMapper.readTree(Files.readAllBytes(catalog));

            // copy all files
            super.prepareWorkingCopy(props, directory);

            // read current catalog
            JsonNode current = objectMapper.readTree(Files.readAllBytes(catalog));

            // merge catalogs
            JsonNode merged = JsonUtils.merge(previous, current);

            // write merged catalog
            Files.write(catalog, merged.toPrettyString().getBytes());
        } else {
            // copy all files
            super.prepareWorkingCopy(props, directory);
        }
    }
}