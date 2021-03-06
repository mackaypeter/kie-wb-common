/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.stunner.core.client.session.impl;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.stunner.core.client.session.ClientReadOnlySession;
import org.kie.workbench.common.stunner.core.client.session.ClientSessionFactory;

/**
 * Stunner's default session factory for sessions of type <code>ClientReadOnlySession</code>.
 */
@ApplicationScoped
public class ClientReadOnlySessionFactory implements ClientSessionFactory<ClientReadOnlySession> {

    private static Logger LOGGER = Logger.getLogger(ClientReadOnlySessionFactory.class.getName());

    private final ManagedInstance<ClientReadOnlySessionImpl> readOnlySessionInstances;

    protected ClientReadOnlySessionFactory() {
        this(null);
    }

    @Inject
    public ClientReadOnlySessionFactory(final ManagedInstance<ClientReadOnlySessionImpl> readOnlySessionInstances) {
        this.readOnlySessionInstances = readOnlySessionInstances;
    }

    @Override
    public ClientReadOnlySession newSession() {
        return this.readOnlySessionInstances.get();
    }

    @Override
    public Class<ClientReadOnlySession> getSessionType() {
        return ClientReadOnlySession.class;
    }
}
