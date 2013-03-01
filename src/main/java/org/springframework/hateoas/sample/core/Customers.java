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
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Simple repository abstraction for {@link Customer}s.
 * 
 * @author Oliver Gierke
 */
public interface Customers {

	/**
	 * Returns all {@link Customer}s.
	 * 
	 * @return
	 */
	List<Customer> findAll();

	/**
	 * Finds a particular {@link Customer} with the given id or {@literal null} in case no Customer is available with the
	 * given id.
	 * 
	 * @param id
	 * @return
	 */
	Customer findOne(Long id);

	/**
	 * Basic in-memory implementation of {@link Customers}.
	 * 
	 * @author Oliver Gierke
	 */
	@Repository
	static class InMemoryCustomers implements Customers {

		private final List<Customer> customers = new ArrayList<Customer>();

		public InMemoryCustomers() {
			customers.add(new Customer(1L, "Dave", "Matthews"));
			customers.add(new Customer(2L, "Carter", "Beauford"));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.sample.Customers#findAll()
		 */
		public List<Customer> findAll() {
			return customers;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.sample.Customers#findOne(java.lang.Long)
		 */
		public Customer findOne(Long id) {

			for (Customer customer : customers) {
				if (customer.hasId(id)) {
					return customer;
				}
			}

			return null;
		}
	}
}
