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
package org.springframework.hateoas.sample.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.sample.core.Customer;
import org.springframework.hateoas.sample.core.Customers;
import org.springframework.hateoas.sample.core.Order;
import org.springframework.hateoas.sample.core.Orders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Spring MVC controller exposing {@link Order} instances directly to the web. Quite a couple of problems to note here:
 * <ol>
 * <li>Basically exporting the the domain model to the web 1:1. No means to control what is exposed in which way except
 * rendering-specific annotations on the domain classes themselves (which you wnat to avoid).</li>
 * <li>No hypermedia elements at all. Relations to other resources exposed should rather become links in the
 * representation, not embedded. Every resource is "on it's own". There are no general links between the entities.</li>
 * </ol>
 * 
 * @author Oliver Gierke
 */
@Controller
@Profile("web")
public class OrderController {

	@Autowired
	private Orders orders;
	@Autowired
	private Customers customers;

	/**
	 * Exposes all {@link Order}s.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	HttpEntity<List<Order>> showOrders() {

		List<Order> result = orders.findAll();
		return new ResponseEntity<List<Order>>(result, HttpStatus.OK);
	}

	/**
	 * Exposes an individual {@link Order}.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/orders/{id}")
	HttpEntity<Order> showOrder(@PathVariable Long id) {

		Order order = orders.findOne(id);
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

	/**
	 * Exposes all {@link Order}s placed by a particular customer.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/customers/{id}/orders")
	HttpEntity<List<Order>> showCustomerOrders(@PathVariable Long id) {

		Customer customer = customers.findOne(id);

		if (customer == null) {
			return new ResponseEntity<List<Order>>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Order>>(orders.findAll(customer), HttpStatus.OK);
	}
}
