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
 *
 */

package org.ballerinalang.net.jms.nativeimpl.endpoint.queue.receiver;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.connector.api.Struct;
import org.ballerinalang.model.NativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.net.jms.Constants;
import org.ballerinalang.net.jms.nativeimpl.endpoint.common.SessionConnector;
import org.ballerinalang.net.jms.utils.BallerinaAdapter;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * Create JMS consumer for a consumer endpoint.
 *
 * @since 0.970
 */

@BallerinaFunction(
        orgName = "ballerina",
        packageName = "jms",
        functionName = "createQueueReceiver",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "QueueReceiver", structPackage = "ballerina.jms"),
        args = { @Argument(name = "session", type = TypeKind.STRUCT, structType = "Session"),
                 @Argument(name = "messageSelector", type = TypeKind.STRING)
        },
        isPublic = true
)
public class CreateConsumer implements NativeCallableUnit {
    @Override
    public void execute(Context context, CallableUnitCallback callableUnitCallback) {
        Struct queueConsumerBObject = BallerinaAdapter.getReceiverObject(context);

        BStruct sessionBObject = (BStruct) context.getRefArgument(1);
        String messageSelector = context.getStringArgument(0);
        Session session = BallerinaAdapter.getNativeObject(sessionBObject,
                                                           Constants.JMS_SESSION,
                                                           Session.class,
                                                           context);
        Struct queueConsumerConfigBRecord = queueConsumerBObject.getStructField(Constants.CONSUMER_CONFIG);
        String queueName = queueConsumerConfigBRecord.getStringField(Constants.QUEUE_NAME);

        try {
            Destination queue = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue, messageSelector);
            Struct consumerConnectorBObject = queueConsumerBObject.getStructField(Constants.CONSUMER_ACTIONS);
            consumerConnectorBObject.addNativeData(Constants.JMS_CONSUMER_OBJECT, consumer);
            consumerConnectorBObject.addNativeData(Constants.SESSION_CONNECTOR_OBJECT, new SessionConnector(session));
        } catch (JMSException e) {
            BallerinaAdapter.throwBallerinaException("Error while creating queue consumer.", context, e);
        }
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
