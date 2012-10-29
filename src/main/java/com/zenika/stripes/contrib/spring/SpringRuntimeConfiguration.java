/* 
 * Copyright (c) 2012 Zenika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zenika.stripes.contrib.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.sourceforge.stripes.config.RuntimeConfiguration;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.StripesRuntimeException;

/**
 * Provides Spring annotation detection ability for injection to the RuntomeConfiguration class.
 * Field, method and constructor injections inside Interceptor's instances is managed. 
 *  
 * @author Florian Hussonnois (Zenika)
 * @author Yohan Legat (Zenika)
 *
 */
public class SpringRuntimeConfiguration extends RuntimeConfiguration {

    /**
     * Splits a comma-separated list of class names and maps each {@link LifecycleStage} to the
     * interceptors in the list that intercept it. Also automatically finds Interceptors in
     * packages listed in {@link BootstrapPropertyResolver#PACKAGES} if searchExtensionPackages is true.
     * 
     * Interceptors are instancied from the Spring factory AutowireCapableBeanFactory.
     * @see <a href="http://grepcode.com/file/repo1.maven.org/maven2/org.springframework/spring-beans/3.0.2.RELEASE/org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory.java#AbstractAutowireCapableBeanFactory.createBean%28java.lang.Class%29">createBean code</a>
     * 
     * @return a Map of {@link LifecycleStage} to Collection of {@link Interceptor}
     */
	@Override
	protected Map<LifecycleStage, Collection<Interceptor>> initInterceptors(List classes) {

        Map<LifecycleStage, Collection<Interceptor>> map = new HashMap<LifecycleStage, Collection<Interceptor>>();

        for (Object type : classes) {
            try {
                ServletContext servletContext = getServletContext();
                ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                
                AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
                Interceptor interceptor = (Interceptor) beanFactory.createBean((Class) type, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
                addInterceptor(map, interceptor);
            }
            catch (Exception e) {
                throw new StripesRuntimeException("Could not instantiate configured Interceptor ["
                        + type.getClass().getName() + "].", e);
            }
        }

        return map;
	}
}
