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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.sample.core.Customer;
import org.springframework.hateoas.sample.core.Customers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Oliver Gierke
 */
@Controller
@Profile("hateoas")
@RequestMapping("/customers")
@ExposesResourceFor(Customer.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class CustomerController {

	private final @NonNull Customers customers;
	private final @NonNull EntityLinks entityLinks;

	@RequestMapping(method = RequestMethod.GET)
	HttpEntity<Resources<Customer>> showCustomers() {

		Resources<Customer> resources = new Resources<>(customers.findAll());
		resources.add(entityLinks.linkToCollectionResource(Customer.class));

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	HttpEntity<Resource<Customer>> showCustomer(@PathVariable Long id) {

		Resource<Customer> resource = new Resource<>(customers.findOne(id));
		resource.add(entityLinks.linkToSingleResource(Customer.class, id));

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
