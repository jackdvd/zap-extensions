/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2018 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zaproxy.zap.extension.websocket.client;

import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.zaproxy.zap.extension.websocket.utility.WebSocketUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Build HttpHandshake request in order to upgrade protocol.
 */
public class HttpHandshakeBuilder {
    
    public static final String UPGRADE_HEADER = "Upgrade";
    public static final String CONNECTION_UPGRADE_PARAMETER ="Upgrade";
    public static final String CONNECTION_KEEP_ALIVE_PARAMETER ="keep-alive";
    public static final int WEBSOCKET_VERSION_13 = 13;
    public static final String UPGRADE_PARAMETER = "websocket";
    public static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";
    public static final String SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    public static final String SEC_WEB_SOCKET_VERSION = "Sec-WebSocket-Version";
    public static final String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";
    public static final String SEC_WEB_SOCKET_ACCEPT = "Sec-WebSocket-Accept";
    public static final String METHOD_HEADER = "GET";
    public static final String SERVER_KEY_ADDON = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    public static final String CONNECTION = "Connection";
    
    /**
     * Craft a Http Handshake request which request to upgrade protocol. The
     * handshake request contains the basic headers (Host, Sec-WebSocket-key,
     * Connection, Upgrade, etc).
     * @param hostUri where the handshake will be send
     * @throws HttpMalformedHeaderException
     */
    public static HttpRequestHeader getHttpHandshakeRequestHeader(URI hostUri) throws HttpMalformedHeaderException {
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(METHOD_HEADER,hostUri,HttpRequestHeader.HTTP11);
        
        List<String> connectionParameters = new ArrayList<>();
        connectionParameters.add(CONNECTION_UPGRADE_PARAMETER);
        connectionParameters.add(CONNECTION_KEEP_ALIVE_PARAMETER);
        addConnectionHeader(httpRequestHeader,connectionParameters);
        
        addUpgradeHeader(httpRequestHeader);
        addGeneratedWebSocketKey(httpRequestHeader);
        addWebSocketVersion(httpRequestHeader,WEBSOCKET_VERSION_13);
        return httpRequestHeader;
    }
    
    /**
     * Generates and add a Sec-WebSocket-key to {@code httpRequestHeader}
     * @param httpRequestHeader the http request header
     */
    public static void addGeneratedWebSocketKey(HttpRequestHeader httpRequestHeader){
        httpRequestHeader.addHeader(SEC_WEB_SOCKET_KEY, null); //Remove if any
        httpRequestHeader.addHeader(SEC_WEB_SOCKET_KEY, WebSocketUtils.generateSecWebSocketKey());
    }
    
    
    /**
     * Generates and add a Sec-WebSocket-key to {@code httpRequestHeader}
     * @param httpRequestHeader the http request header
     */
    public static void addWebSocketKey(HttpRequestHeader httpRequestHeader, String websocketKey){
        httpRequestHeader.addHeader(SEC_WEB_SOCKET_KEY, null); //Remove if any
        httpRequestHeader.addHeader(SEC_WEB_SOCKET_KEY, websocketKey);
    }
    
    /**
     * Adds header  {@code Connection} to {@code httpRequestHeader}
     * @param httpRequestHeader the http request header
     * @param connectionValues new parameters of http header {@code Connection}
     */
    public static void addConnectionHeader(HttpRequestHeader httpRequestHeader, List<String> connectionValues){
        if(!connectionValues.isEmpty()){
            httpRequestHeader.addHeader(CONNECTION,String.join(",",connectionValues));
        }
    }
    
    /**
     * Adds http header and parameter {@code Upgrade: websocket }
     * @param httpRequestHeader the http header
     */
    public static void addUpgradeHeader(HttpRequestHeader httpRequestHeader){
        httpRequestHeader.addHeader(UPGRADE_HEADER,UPGRADE_PARAMETER);
    }
    
    /**
     * Adds {@code Sec-WebSocket-Version} header to {@code httpRequestHeader}
     * @param httpRequestHeader the http request header
     * @param wsVersion version of websocket
     */
    public static void addWebSocketVersion(HttpRequestHeader httpRequestHeader, int wsVersion){
        httpRequestHeader.addHeader(SEC_WEB_SOCKET_VERSION,String.valueOf(wsVersion));
    }
    
}
