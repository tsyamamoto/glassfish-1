/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.test.jms.mdbdest.ejb;

import java.util.logging.*;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;

@MessageDriven(name = "test5", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/jms_unit_test_Topic"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "wrongname")
})
public class NewMessageBean5 implements MessageListener {
    private static final Logger logger = Logger.getLogger(NewMessageBean.class.getName());
    
    private static int count;
    
    @Resource
    private MessageDrivenContext mdc;

    @Resource(mappedName = "jms/jms_unit_result_Queue")
    private Queue resultQueue;

    @Inject
    @JMSConnectionFactory("jms/jms_unit_test_QCF")
    @JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
    private JMSContext jmsContext;
    
    public NewMessageBean5() {
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void onMessage(Message message) {
        JMSProducer producer = jmsContext.createProducer();
        TextMessage tmsg = jmsContext.createTextMessage("Received: " + this.getClass().getName());
        producer.send(resultQueue, tmsg);
        System.out.println(this.getClass().getName() + " sent message!!!");
    }
}