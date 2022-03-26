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

import com.tsystemsmms.cmcc.cmccoperator.crds.IngressTls;
import com.tsystemsmms.cmcc.cmccoperator.targetstate.TargetState;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.networking.v1.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements an Ingress builder that doesn't generate any resources.
 */
public class NullIngressBuilder extends AbstractIngressBuilder {

    public NullIngressBuilder(TargetState targetState, String name, String hostname, IngressTls tls) {
    }

    @Override
    public Collection<? extends HasMetadata> build() {
        return Collections.emptyList();
    }

    @Override
    public IngressBuilder pathExact(String path, String service) {
        return this;
    }

    @Override
    public IngressBuilder pathPattern(String path, String service) {
        return this;
    }

    @Override
    public IngressBuilder pathPrefix(String path, String service) {
        return this;
    }

    @Override
    public IngressBuilder redirect(String uri) {
        return this;
    }

    @Override
    public IngressBuilder rewrite(String pattern) {
        return this;
    }

    static HTTPIngressPathBuilder withPath(HTTPIngressPathBuilder b, Path path) {
        return b;
    }

    @Override
    public IngressBuilder uploadSize(String size) {
        return this;
    }
}
