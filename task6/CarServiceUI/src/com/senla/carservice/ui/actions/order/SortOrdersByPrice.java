package com.senla.carservice.ui.actions.order;

import com.senla.carservice.ui.actions.api.IAction;
import com.senla.carservice.util.utils.Printer;
import com.senla.carservice.view.facade.CarService;

public class SortOrdersByPrice implements IAction {

	@Override
	public void execute() {
		CarService carService = CarService.getInstance();
		carService.sortOrdersByPrice();
		Printer.print(carService.getOrders());

	}

}
