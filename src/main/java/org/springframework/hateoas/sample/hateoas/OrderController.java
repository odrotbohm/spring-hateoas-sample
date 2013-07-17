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
package org.springframework.hateoas.sample.hateoas;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.sample.core.Customer;
import org.springframework.hateoas.sample.core.Customers;
import org.springframework.hateoas.sample.core.Order;
import org.springframework.hateoas.sample.core.Order.LineItem;
import org.springframework.hateoas.sample.core.Orders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Oliver Gierke
 */
@Controller
@Profile("hateoas")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OrderController {

	private final @NonNull Orders orders;
	private final @NonNull Customers customers;

	/**
	 * Exposes a collection resource for {@link Order}s.
	 * 
	 * @return
	 */
	@RequestMapping("/orders")
	HttpEntity<Resources<Order>> showOrders() {

		Resources<Order> orders = new Resources<>(this.orders.findAll());
		orders.add(linkTo(methodOn(OrderController.class).showOrders()).withSelfRel());

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	/**
	 * Exposes a single {@link Order} resource.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/orders/{id}")
	HttpEntity<Resource<Order>> showOrder(@PathVariable Long id) {

		Resource<Order> order = new Resource<Order>(orders.findOne(id));
		order.add(linkTo(methodOn(OrderController.class).showOrder(id)).withSelfRel());

		return new ResponseEntity<Resource<Order>>(order, HttpStatus.OK);
	}

	/**
	 * Exposes all {@link Order}s for {@link Customer}s.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/customers/{id}/orders")
	HttpEntity<Resources<OrderResource>> showCustomerOrders(@PathVariable Long id) {

		Customer customer = customers.findOne(id);

		if (customer == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		List<OrderResource> orderResources = new ArrayList<>();

		for (Order order : orders.findAll(customer)) {

			OrderResource resource = new OrderResource(order.getLineItems());
			resource.add(linkTo(methodOn(OrderController.class).showOrder(order.getId())).withSelfRel());
			resource.add(linkTo(methodOn(CustomerController.class).showCustomer(id)).withRel("customer"));

			orderResources.add(resource);
		}

		Resources<OrderResource> resources = new Resources<>(orderResources);
		resources.add(linkTo(methodOn(OrderController.class).showCustomerOrders(id)).withSelfRel());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Link", linkTo(methodOn(OrderController.class).showCustomerOrders(id)).withSelfRel().toString());

		return new ResponseEntity<>(resources, headers, HttpStatus.OK);
	}

	/**
	 * DTO for {@link Order}s.
	 * 
	 * @author Oliver Gierke
	 */
	@RequiredArgsConstructor
	@Getter
	private static class OrderResource extends ResourceSupport {

		private final List<LineItem> lineItems;
	}
}
