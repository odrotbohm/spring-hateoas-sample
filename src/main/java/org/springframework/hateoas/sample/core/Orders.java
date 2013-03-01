/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.hateoas.sample.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.sample.core.Order.LineItem;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Simple repository to access {@link Order}s.
 * 
 * @author Oliver Gierke
 */
public interface Orders {

	/**
	 * Returns all {@link Order}s.
	 * 
	 * @return
	 */
	List<Order> findAll();

	/**
	 * Returns all {@link Order}s placed by the given {@link Customer}.
	 * 
	 * @param customer
	 * @return
	 */
	List<Order> findAll(Customer customer);

	/**
	 * Returns the {@link Order} with the given id.
	 * 
	 * @param id
	 * @return
	 */
	Order findOne(Long id);

	/**
	 * In-memory implementation of {@link Orders}.
	 * 
	 * @author Oliver Gierke
	 */
	@Repository
	static class InMemoryOrders implements Orders {

		private final List<Order> orders = new ArrayList<Order>();

		@Autowired
		public InMemoryOrders(Customers customers) {

			Assert.notNull(customers, "Customers must not be null!");
			Customer customer = customers.findOne(1L);

			LineItem iPad = new Order.LineItem("iPad");
			LineItem iPhone = new Order.LineItem("iPhone");

			Order order = new Order(1L, customer);
			order.add(iPhone).add(iPad);

			this.orders.add(order);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.sample.Orders#findAll()
		 */
		public List<Order> findAll() {
			return Collections.unmodifiableList(orders);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.sample.Orders#findAll(org.springframework.hateoas.sample.Customer)
		 */
		public List<Order> findAll(Customer customer) {

			List<Order> result = new ArrayList<Order>();

			for (Order order : orders) {
				if (order.belongsTo(customer)) {
					result.add(order);
				}
			}

			return Collections.unmodifiableList(result);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.sample.Orders#findOne(java.lang.Long)
		 */
		public Order findOne(Long id) {

			for (Order order : orders) {
				if (order.hasId(id)) {
					return order;
				}
			}
			return null;
		}
	}
}
