/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.data.publisher.application.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticationDataPublisher;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorStatus;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.context.SessionContext;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.core.handler.AbstractIdentityMessageHandler;
import org.wso2.carbon.identity.data.publisher.application.authentication.internal.AuthenticationDataPublisherDataHolder;
import org.wso2.carbon.identity.event.IdentityEventConstants.EventName;
import org.wso2.carbon.identity.event.IdentityEventConstants.EventProperty;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * The iterative data publishing and invoking event handlers correspond to triggered
 * event are undertaken by this class.
 * <p>
 * The iterative data publishing will be removed in a next major release because the
 * existing data publishers are transformed into event handlers.
 */
public class AuthnDataPublisherProxy extends AbstractIdentityMessageHandler implements
        AuthenticationDataPublisher {

    private static final Log log = LogFactory.getLog(AuthnDataPublisherProxy.class);
    private List<AuthenticationDataPublisher> dataPublishers = AuthenticationDataPublisherDataHolder.getInstance()
            .getDataPublishers();

    /**
     * Publish authentication success after managing handler operations
     *
     * @param request Request which comes to the framework for authentication
     * @param context Authentication context
     * @param params  Other parameters which are need to be passed
     */
    public void publishAuthenticationStepSuccess(HttpServletRequest request, AuthenticationContext context,
                                                 Map<String, Object> params) {

        Event event = createEvent(request, context, null, params, EventName.AUTHENTICATION_STEP_SUCCESS);
        event.addEventProperty(EventProperty.AUTHENTICATION_STATUS, AuthenticatorStatus.PASS);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishAuthenticationStepSuccess(request, context, params);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    protected boolean isPublisherEnabledAndCanHandle(AuthenticationContext context, AuthenticationDataPublisher
            publisher) {

        return publisher != null && publisher.isEnabled(context) && publisher.canHandle(context);
    }

    /**
     * Published authentication step failure after managing handler operations
     *
     * @param request         Incoming Http request to framework for authentication
     * @param context         Authentication Context
     * @param unmodifiableMap Other relevant parameters which needs to be published
     */
    public void publishAuthenticationStepFailure(HttpServletRequest request, AuthenticationContext context,
                                                 Map<String, Object> unmodifiableMap) {

        Event event = createEvent(request, context, null, unmodifiableMap, EventName.AUTHENTICATION_STEP_FAILURE);
        event.addEventProperty(EventProperty.AUTHENTICATION_STATUS, AuthenticatorStatus.FAIL);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishAuthenticationStepFailure(request, context, unmodifiableMap);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    /**
     * Publishes authentication success after managing handler operations
     *
     * @param request         Incoming request for authentication
     * @param context         Authentication context
     * @param unmodifiableMap Other relevant parameters which needs to be published
     */
    public void publishAuthenticationSuccess(HttpServletRequest request, AuthenticationContext context,
                                             Map<String, Object> unmodifiableMap) {

        Event event = createEvent(request, context, null, unmodifiableMap, EventName.AUTHENTICATION_SUCCESS);
        event.addEventProperty(EventProperty.AUTHENTICATION_STATUS, AuthenticatorStatus.PASS);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishAuthenticationSuccess(request, context, unmodifiableMap);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    /**
     * Publishes authentication failure after managing handler operations
     *
     * @param request         Incoming authentication request
     * @param context         Authentication context
     * @param unmodifiableMap Other relevant parameters which needs to be published
     */
    public void publishAuthenticationFailure(HttpServletRequest request, AuthenticationContext context,
                                             Map<String, Object> unmodifiableMap) {

        Event event = createEvent(request, context, null, unmodifiableMap, EventName.AUTHENTICATION_FAILURE);
        event.addEventProperty(EventProperty.AUTHENTICATION_STATUS, AuthenticatorStatus.FAIL);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishAuthenticationFailure(request, context, unmodifiableMap);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    /**
     * Publishes session creation information after managing handler operations
     *
     * @param request         Incoming request for authentication
     * @param context         Authentication Context
     * @param sessionContext  Session context
     * @param unmodifiableMap Other relevant parameters which needs to be published
     */
    public void publishSessionCreation(HttpServletRequest request, AuthenticationContext context, SessionContext
            sessionContext, Map<String, Object> unmodifiableMap) {

        Event event = createEvent(request, context, sessionContext, unmodifiableMap, EventName.SESSION_CREATE);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishSessionCreation(request, context, sessionContext, unmodifiableMap);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    /**
     * Publishes session update after managing handler operations
     *
     * @param request         Incoming request for authentication
     * @param context         Authentication context
     * @param sessionContext  Session context
     * @param unmodifiableMap Other relevant parameters which needs to be published
     */
    public void publishSessionUpdate(HttpServletRequest request, AuthenticationContext context, SessionContext
            sessionContext, Map<String, Object> unmodifiableMap) {

        Event event = createEvent(request, context, sessionContext, unmodifiableMap, EventName.SESSION_UPDATE);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishSessionUpdate(request, context, sessionContext, unmodifiableMap);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    /**
     * Publishes session termination
     *
     * @param request         Incoming request for authentication
     * @param context         Authentication context
     * @param sessionContext  Session context
     * @param unmodifiableMap Other relevant parameters which needs to be published
     */

    public void publishSessionTermination(HttpServletRequest request, AuthenticationContext context,
                                          SessionContext sessionContext, Map<String, Object> unmodifiableMap) {

        Event event = createEvent(request, context, sessionContext, unmodifiableMap, EventName.SESSION_TERMINATE);
        doPublishEvent(event);
        for (AuthenticationDataPublisher publisher : dataPublishers) {
            if (isPublisherEnabledAndCanHandle(context, publisher)) {
                publisher.publishSessionTermination(request, context, sessionContext, unmodifiableMap);
                if (log.isDebugEnabled()) {
                    log.debug(publisher.getName() + " is invoked.");
                }
            }
        }
    }

    @Override
    public String getName() {

        return FrameworkConstants.AnalyticsAttributes.AUTHN_DATA_PUBLISHER_PROXY;
    }

    private Event createEvent(HttpServletRequest request, AuthenticationContext context, SessionContext
            sessionContext, Map<String, Object> params, EventName eventName) {

        Map<String, Object> eventProperties = new HashMap<>();
        eventProperties.put(EventProperty.REQUEST, request);
        eventProperties.put(EventProperty.CONTEXT, context);
        if (sessionContext != null) {
            eventProperties.put(EventProperty.SESSION_CONTEXT, sessionContext);
        }
        eventProperties.put(EventProperty.PARAMS, params);
        Event event = new Event(eventName.name(), eventProperties);
        return event;
    }

    private void doPublishEvent(Event event) {

        try {
            AuthenticationDataPublisherDataHolder.getInstance().getIdentityEventService().handleEvent(event);
        } catch (IdentityEventException e) {
            log.error("Error while publishing the event: " + event.getEventName() + ".", e);
        }
    }
}
