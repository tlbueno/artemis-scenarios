/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.artemis.scenarios.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.artemis.scenarios.model.requests.OrdersIncomeRequest;
import org.apache.activemq.artemis.scenarios.model.response.OrdersResponse;

public class IncomeService extends BaseService {

   public OrdersResponse process(OrdersIncomeRequest income) throws Exception {
      ConnectionFactory factory = createConnectionFactory(income.getProtocol(), income.getUri());

      try (Connection connection = factory.createConnection(income.getUser(), income.getPassword())) {
         Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

         MessageProducer producer = session.createProducer(session.createQueue("Orders"));

         for (int i = 0; i < income.getNumberOfOrders(); i++) {
            producer.send(session.createTextMessage("This is a test " + i));
         }

         session.commit();
      }


      return new OrdersResponse().setNumberOfOrders(income.getNumberOfOrders());

   }

}
