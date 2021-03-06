package com.senla.carservice.comparators.orders;

import java.util.Comparator;
import com.senla.carservice.beans.Order;

public class ExecutionDateComparator implements Comparator<Order> {

	@Override
	public int compare(Order order1, Order order2) {
		if (order1 == null && order2 == null) {
			return 0;
		}
		if (order1 == null) {
			return 1;
		}
		if (order2 == null) {
			return -1;
		}
		return order1.getExecutionDate().compareTo(order2.getExecutionDate());
	}

}
