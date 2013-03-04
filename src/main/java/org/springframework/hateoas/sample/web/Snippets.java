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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.sample.core.Customer;
import org.springframework.hateoas.sample.core.Customers;
import org.springframework.hateoas.sample.core.Order;
import org.springframework.hateoas.sample.core.Orders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Code snippets to hypermediize the sample controllers.
 * 
 * @author Oliver Gierke
 */
public class Snippets {

	Customers customers;
	Orders orders;

	Resource<Customer> resource;
	Resources<Customer> resources;

	EntityLinks links;

	/**
	 * Step 1 - Resources
	 */
	HttpEntity<Resource<Customer>> showCustomer(@PathVariable Long id) {

		Resource<Customer> resource = new Resource<>(customers.findOne(id));
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	/**
	 * Step 2 - LinkBuilder
	 */
	void linkBuilder(Long id) {

		// import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
		resource.add(linkTo(CustomerController.class).slash(id).withSelfRel());
		resource.add(linkTo(methodOn(CustomerController.class).showCustomer(id)).withSelfRel());
	}

	/**
	 * Step 3 - EntityLinks
	 */
	@RequestMapping("/customers/{id}/orders")
	HttpEntity<Resources<Order>> showCustomerOrders(@PathVariable Long id) {

		// @ExposesResourceFor(Customer.class)

		// @Autowired
		// EntityLinks links;

		// @EnableHypermediaSupport

		Customer customer = customers.findOne(id);

		if (customer == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Resources<Order> resources = new Resources<>(orders.findAll(customer));
		resources.add(linkTo(methodOn(CustomerController.class).showCustomer(id)).withRel("customer"));
		// resources.add(links.linkForSingleResource(Customer.class, id).withRel("customer"));

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
}
