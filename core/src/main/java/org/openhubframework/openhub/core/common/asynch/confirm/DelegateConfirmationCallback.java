/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhubframework.openhub.core.common.asynch.confirm;

import java.util.*;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import org.openhubframework.openhub.api.asynch.confirm.ConfirmationCallback;
import org.openhubframework.openhub.api.asynch.confirm.ExternalSystemConfirmation;
import org.openhubframework.openhub.api.entity.ExternalSystemExtEnum;
import org.openhubframework.openhub.api.entity.Message;
import org.openhubframework.openhub.api.entity.MsgStateEnum;


/**
 * Implementation of {@link ConfirmationCallback} that delegates confirmation
 * to {@link ExternalSystemConfirmation} implementation.
 *
 * @author Petr Juza
 */
public class DelegateConfirmationCallback implements ConfirmationCallback {

    private static final Logger LOG = LoggerFactory.getLogger(DelegateConfirmationCallback.class);

    private static final Set<MsgStateEnum> ALLOWED_STATES =
            Collections.unmodifiableSet(EnumSet.of(MsgStateEnum.OK, MsgStateEnum.FAILED));

    @Autowired(required = false)
    private List<ExternalSystemConfirmation> msgConfirmations;

    @Override
    public void confirm(Message msg) {
        Assert.notNull(msg, "Message must not be null");
        Assert.isTrue(ALLOWED_STATES.contains(msg.getState()), "Message must be in a final state to be confirmed");

        ExternalSystemConfirmation impl = getImplementation(msg.getSourceSystem());
        if (impl != null) {
            impl.confirm(msg);
        } else {
            LOG.debug("Confirmation {} - no suitable ExternalSystemConfirmation implementation "
                    + "for the following external system: {}", msg.getState(), msg.getSourceSystem());
        }
    }

    /**
     * Gets (first) implementation for specified external system.
     *
     * @param externalSystem the source system
     * @return implementation
     */
    @Nullable
    protected ExternalSystemConfirmation getImplementation(ExternalSystemExtEnum externalSystem) {
        if (msgConfirmations == null) {
            return null;
        }

        // select the supported implementation for the source external system
        for (ExternalSystemConfirmation msgConfirmation : msgConfirmations) {
            final Set<ExternalSystemExtEnum> systems = msgConfirmation.getExternalSystems();

            List<String> supportedSystems = new LinkedList<String>();
            for (ExternalSystemExtEnum system : systems) {
                supportedSystems.add(system.getSystemName());
            }
            if (supportedSystems.contains(externalSystem.getSystemName())) {
                return msgConfirmation;
            }
        }

        return null;
    }
}
