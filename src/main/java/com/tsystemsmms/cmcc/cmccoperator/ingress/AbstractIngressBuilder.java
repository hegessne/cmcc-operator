/*
 * Copyright (c) 2022. T-Systems Multimedia Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.tsystemsmms.cmcc.cmccoperator.ingress;

import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.Data;

import java.util.Collection;

public abstract class AbstractIngressBuilder implements IngressBuilder{

    public static enum PathType {
        EXACT,
        PREFIX,
        PATTERN;
    }
    @Data
    public static class Path {
        private final String pattern;
        private final PathType type;
        private final String service;
    }
}
