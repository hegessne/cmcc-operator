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

import com.tsystemsmms.cmcc.cmccoperator.ingress.*;
import com.tsystemsmms.cmcc.cmccoperator.resource.ResourceReconcilerManager;
import com.tsystemsmms.cmcc.cmccoperator.targetstate.DefaultResourceNamingProviderFactory;
import com.tsystemsmms.cmcc.cmccoperator.targetstate.DefaultTargetStateFactory;
import com.tsystemsmms.cmcc.cmccoperator.targetstate.ResourceNamingProviderFactory;
import com.tsystemsmms.cmcc.cmccoperator.targetstate.TargetStateFactory;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@SpringBootApplication
//@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.tsystemsmms.cmcc.cmccoperator")
@Slf4j
public class CMCCOperatorApplication {

    @Bean
    @ConditionalOnProperty(prefix="cmcc.resources", value = "useCrd", havingValue = "true", matchIfMissing = true)
    public CoreMediaContentCloudReconciler coreMediaContentCloudReconciler(
            KubernetesClient kubernetesClient,
            TargetStateFactory targetStateFactory) {
        return new CoreMediaContentCloudReconciler(
                kubernetesClient,
                targetStateFactory);
    }

    @Bean
    @ConditionalOnProperty(prefix="cmcc.resources", value = "useConfigMap", havingValue = "true")
    public CmccConfigMapReconciler cmccConfigMapReconciler(
            KubernetesClient kubernetesClient,
            TargetStateFactory targetStateFactory) {
        return new CmccConfigMapReconciler(
                kubernetesClient,
                targetStateFactory);
    }

    @Bean
    public ResourceNamingProviderFactory resourceNamingProviderFactory() {
        return new DefaultResourceNamingProviderFactory();
    }

    @Bean
    public ResourceReconcilerManager resourceReconciler(KubernetesClient kubernetesClient) {
        return new ResourceReconcilerManager(kubernetesClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public CmccIngressGeneratorFactory ingressGeneratorFactory(IngressBuilderFactory ingressBuilderFactory, CmccConfig config) {
        CmccIngressGeneratorFactory ingressGeneratorFactory = null;

        switch (config.ingress.generator) {
            case BLUEPRINT:
                ingressGeneratorFactory = new BlueprintCmccIngressGeneratorFactory(ingressBuilderFactory);
                break;
            case ONLY_LANG:
                ingressGeneratorFactory = new OnlyLangCmccIngressGeneratorFactory(ingressBuilderFactory);
                break;
        }
        log.info("Using generator {} and builder {}", ingressGeneratorFactory.toString(), ingressBuilderFactory.toString());

        return ingressGeneratorFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public IngressBuilderFactory k8sNginxIngressBuilderFactory(CmccConfig config) {
        IngressBuilderFactory ingressBuilderFactory = null;

        switch (config.ingress.builder) {
            case K8S_NGINX:
                ingressBuilderFactory = new K8sNginxIngressBuilderFactory();
                break;
            case NGINX_INC:
                ingressBuilderFactory = new NginxIncIngressBuilderFactory();
                break;
            case NULL:
                ingressBuilderFactory = new NullIngressBuilderFactory();
                break;
            case TRAEFIK:
                ingressBuilderFactory = new TraefikIngressBuilderFactory();
                break;
        }
        return ingressBuilderFactory;
    }

    @Bean
    public TargetStateFactory targetStateFactory(BeanFactory beanFactory,
                                                 KubernetesClient kubernetesClient,
                                                 CmccIngressGeneratorFactory cmccIngressGeneratorFactory,
                                                 ResourceNamingProviderFactory resourceNamingProviderFactory,
                                                 ResourceReconcilerManager resourceReconcilerManager) {
        return new DefaultTargetStateFactory(beanFactory,
                kubernetesClient,
                cmccIngressGeneratorFactory,
                resourceNamingProviderFactory,
                resourceReconcilerManager);
    }

    public static void main(String[] args) {
        SpringApplication.run(CMCCOperatorApplication.class, args);
    }

}
