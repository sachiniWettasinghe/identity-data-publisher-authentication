/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.data.publisher.authentication.analytics.session;

/**
 * Constants for session DataPublisher.
 */
public class SessionDataPublisherConstants {

    public static final String ANALYTICS_SESSION_PUBLISHER_NAME = "analyticsSessionDataPublisher";

    public static final String SESSION_DATA_STREAM_NAME = "org.wso2.is.analytics.stream.OverallSession:1.0.0";

    // Session status codes
    public static final int SESSION_CREATION_STATUS = 1;
    public static final int SESSION_TERMINATION_STATUS = 0;
    public static final int SESSION_UPDATE_STATUS = 2;

    public static final String ANALYTICS_SESSION_DATA_PUBLISHER_ENABLED = "analyticsSessionDataPublisher.enable";

    private SessionDataPublisherConstants(){

    }
}
