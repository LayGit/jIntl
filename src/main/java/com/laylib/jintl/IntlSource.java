/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laylib.jintl;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.config.DefaultProviderConfig;
import com.laylib.jintl.config.IntlConfig;
import com.laylib.jintl.provider.DefaultMessageProvider;
import com.laylib.jintl.provider.MessageProvider;
import com.laylib.jintl.provider.MessageProviderFactory;
import com.laylib.jintl.util.StringUtils;

import java.util.Locale;

/**
 * Intl Source
 *
 * @author Lay
 */
public class IntlSource {

    private static final IntlConfig DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new IntlConfig();
    }

    /**
     * jIntl configuration
     */
    private IntlConfig config;

    /**
     * message provider
     */
    private MessageProvider messageProvider;

    public IntlSource(IntlConfig config, MessageProvider messageProvider) {
        if (config == null) {
            config = DEFAULT_CONFIG;
        }

        if (messageProvider == null) {
            messageProvider = new DefaultMessageProvider(new DefaultProviderConfig());
        }

        this.config = config;
        this.messageProvider = messageProvider;
    }

    public IntlSource() {
        this(null, (MessageProvider) null);
    }

    public IntlSource(IntlConfig config) {
        this(config, (MessageProvider) null);
    }
    public IntlSource(MessageProvider messageProvider) {
        this(null, messageProvider);
    }

    public IntlSource(IntlConfig intlConfig, BaseProviderConfig providerConfig) {
        this(intlConfig, MessageProviderFactory.build(providerConfig));
    }

    public IntlSource(DefaultProviderConfig providerConfig) {
        this(null, providerConfig);
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        // return null if code is null
        if (code == null) {
            return null;
        }

        // use Locale.getDefault() if locale is null
        if (locale == null) {
            locale = Locale.getDefault();
        }

        // get message
        String message = messageProvider.getMessage(code, args, locale);
        if (message == null) {
            // 1 - fallback to Locale without country
            if (StringUtils.isNotEmpty(locale.getCountry()) && config.isFallbackLanguageOnly()) {
                message = messageProvider.getMessage(code, Locale.forLanguageTag(locale.getLanguage()));
            }

            // 2 - fallback to default message
            if (message == null && defaultMessage != null) {
                // default message
                message = defaultMessage;
            }

            // 3 - fallback to code
            if (message == null && config.isUseCodeAsDefaultMessage()) {
                message = code;
            }
        }

        return message;
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, null, locale);
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        return getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, null, locale);
    }

    public IntlConfig getConfig() {
        return config;
    }

    public void setConfig(IntlConfig config) {
        this.config = config;
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }
}
