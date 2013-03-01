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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Simple order entity.
 * 
 * @author Oliver Gierke
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {

	private final Long id;
	private final Customer customer;
	private final List<LineItem> lineItems = new ArrayList<LineItem>();

	/**
	 * Returns whether the Order has the given id.
	 * 
	 * @param id
	 * @return
	 */
	public boolean hasId(Long id) {
		return this.id.equals(id);
	}

	/**
	 * Returns whether the {@link Order} belongs to the given {@link Customer}.
	 * 
	 * @param customer
	 * @return
	 */
	public boolean belongsTo(Customer customer) {
		return this.customer.equals(customer);
	}

	/**
	 * Adds the given {@link LineItem} to the {@link Order}.
	 * 
	 * @param item
	 * @return
	 */
	public Order add(LineItem item) {
		this.lineItems.add(item);
		return this;
	}

	@Getter
	@RequiredArgsConstructor
	public static class LineItem {

		private final String name;
	}
}
