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

package org.openhubframework.openhub.core.monitoring;


import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import org.openhubframework.openhub.api.entity.MsgStateEnum;
import org.openhubframework.openhub.spi.msg.MessageService;

/**
 * JMX exporter of message processing statistics.
 *
 * @author Jaromir Stradej
 * @since 0.1
 */
@Service
@ManagedResource(objectName = "org.openhubframework.openhub.core.monitoring:name=MessagesStatus",
        description = "Message State Information")
public class MessagesStatus {

    @Autowired
    private MessageService messageService;


    @ManagedAttribute(description = "Count of messages in state FAILED")
    public int getCountOfFailed() {
        return messageService.getCountMessages(MsgStateEnum.FAILED, null);
    }

    @ManagedAttribute(description = "Count of messages in state PROCESSING")
    public int getCountOfProcessing() {
        return messageService.getCountMessages(MsgStateEnum.PROCESSING, null);
    }

    @ManagedAttribute(description = "Count of messages in state CANCEL")
    public int getCountOfCanceled() {
        return messageService.getCountMessages(MsgStateEnum.CANCEL, null);
    }

    @ManagedAttribute(description = "Count of messages in state NEW")
    public int getCountOfNew() {
        return messageService.getCountMessages(MsgStateEnum.NEW, null);
    }

    @ManagedAttribute(description = "Count of messages in state OK")
    public int getCountOfOk() {
        return messageService.getCountMessages(MsgStateEnum.OK, null);
    }

    @ManagedAttribute(description = "Count of messages in state PARTLY_FAILED")
    public int getCountOfPartlyFailed() {
        return messageService.getCountMessages(MsgStateEnum.PARTLY_FAILED, null);
    }

    @ManagedAttribute(description = "Count of messages in state WAITING")
    public int getCountOfWaiting() {
        return messageService.getCountMessages(MsgStateEnum.WAITING, null);
    }

    @ManagedAttribute(description = "Count of messages in state WAITING_FOR_RES")
    public int getCountOfWaitingForResponse() {
        return messageService.getCountMessages(MsgStateEnum.WAITING_FOR_RES, null);
    }

    @ManagedAttribute(description = "Count of messages in state POSTPONED")
    public int getCountOfPostponed() {
        return messageService.getCountMessages(MsgStateEnum.POSTPONED, null);
    }

    @ManagedAttribute(description = "Count of messages in state CANCEL")
    public int getCountOfCancel() {
        return messageService.getCountMessages(MsgStateEnum.CANCEL, null);
    }

    @ManagedOperation(description = "Count of messages in state FAILED and after interval")
    public int getCountOfFailedAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.FAILED, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state PROCESSING and after interval")
    public int getCountOfProcessingAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.PROCESSING, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state WAITING and after interval")
    public int getCountOfWaitingAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.WAITING, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state OK and after interval")
    public int getCountOfOkAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.OK, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state NEW and after interval")
    public int getCountOfNewAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.NEW, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state PARTLY_FAILED and after interval")
    public int getCountOfPartlyFailedAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.PARTLY_FAILED, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state CANCEL and after interval")
    public int getCountOfCancelAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.CANCEL, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state WAITING_FOR_RES and after interval")
    public int getCountOfWaitingForResponseAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.WAITING_FOR_RES, Seconds.seconds(intervalSec));
    }

    @ManagedOperation(description = "Count of messages in state POSTPONED and after interval")
    public int getCountOfPostponedAfterInterval(int intervalSec) {
        return messageService.getCountMessages(MsgStateEnum.POSTPONED, Seconds.seconds(intervalSec));
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
