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

import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.AnnotatedClassActionResolver;
import net.sourceforge.stripes.controller.StripesFilter;

/**
 * Provides Spring annotation detection ability for injection to the AnnotatedClassActionResolver class.
 * Field, method and constructor injections inside ActionBean's instances is managed. 
 *  
 * @author Florian Hussonnois (Zenika)
 * @author Yohan Legat (Zenika)
 *
 */
public class AnnotatedSpringClassActionResolver extends AnnotatedClassActionResolver {

    /**
     * Helper method to construct and return a new ActionBean instance. Called whenever a new
     * instance needs to be manufactured. Provides a convenient point for subclasses to add
     * specific behaviour during action bean creation.
     * 
     * ActionBean are instancied from the Spring factory AutowireCapableBeanFactory.
     * @see <a href="http://grepcode.com/file/repo1.maven.org/maven2/org.springframework/spring-beans/3.0.2.RELEASE/org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory.java#AbstractAutowireCapableBeanFactory.createBean%28java.lang.Class%29">createBean code</a>
     *
     * @param type the type of ActionBean to create
     * @param context the current ActionBeanContext
     * @return the new ActionBean instance
     * @throws Exception if anything goes wrong!
     */
	@Override
    protected ActionBean makeNewActionBean(Class<? extends ActionBean> type, ActionBeanContext context) {
    	ServletContext servletContext = StripesFilter.getConfiguration().getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        ActionBean actionBean = (ActionBean) beanFactory.createBean(type, AutowireCapableBeanFactory.AUTOWIRE_NO, false);

        return actionBean;
    }
}
