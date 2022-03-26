/*
 * Copyright (c) 2022. T-Systems Multimedia Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.tsystemsmms.cmcc.cmccoperator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "cmcc")
public class CmccConfig {
    Ingress ingress;
    Resources resources;

    @Data
    public static class Ingress {
        @NotBlank
        Builder builder;
        @NotBlank
        Generator generator;
    }

    @Data
    public static class Resources {
        boolean useCrd = true;
        boolean useConfigMap = false;
    }

    public enum Builder {
        K8S_NGINX,
        NGINX_INC,
        NULL,
        TRAEFIK
    }

    public enum Generator {
        BLUEPRINT,
        ONLY_LANG
    }
}
