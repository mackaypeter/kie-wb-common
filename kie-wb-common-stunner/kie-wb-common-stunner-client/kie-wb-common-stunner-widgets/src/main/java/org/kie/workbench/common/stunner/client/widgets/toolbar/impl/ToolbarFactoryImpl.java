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

package org.kie.workbench.common.stunner.client.widgets.toolbar.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.stunner.client.widgets.toolbar.Toolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.ToolbarFactory;
import org.kie.workbench.common.stunner.client.widgets.toolbar.ToolbarView;
import org.kie.workbench.common.stunner.client.widgets.toolbar.item.AbstractToolbarItem;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;

@ApplicationScoped
public class ToolbarFactoryImpl implements ToolbarFactory<ClientSession> {

    private final ManagedInstance<AbstractToolbarItem<ClientSession>> itemInstances;
    private final ManagedInstance<ToolbarView> viewInstances;

    @Inject
    public ToolbarFactoryImpl(final ManagedInstance<AbstractToolbarItem<ClientSession>> itemInstances,
                              final ManagedInstance<ToolbarView> viewInstances) {
        this.itemInstances = itemInstances;
        this.viewInstances = viewInstances;
    }

    @Override
    public Toolbar<ClientSession> build(final ClientSession session) {
        final ToolbarImpl<ClientSession> toolbar = new ToolbarImpl<ClientSession>(itemInstances,
                                                                                  viewInstances.get());
        toolbar.initialize(session);
        return toolbar;
    }
}
