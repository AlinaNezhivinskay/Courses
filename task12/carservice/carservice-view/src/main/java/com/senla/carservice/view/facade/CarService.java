package com.senla.carservice.view.facade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.senla.carservice.dependencyinjection.coordinationrepository.CoordinationRepository;
import com.senla.carservice.api.facade.ICarService;
import com.senla.carservice.api.services.IGarageService;
import com.senla.carservice.api.services.IMasterService;
import com.senla.carservice.api.services.IOrderService;
import com.senla.carservice.model.beans.Garage;
import com.senla.carservice.model.beans.Master;
import com.senla.carservice.model.beans.Order;
import com.senla.carservice.model.orderstate.OrderState;
import com.senla.carservice.model.sortfields.master.SortMasterFields;
import com.senla.carservice.model.sortfields.order.SortOrderFields;
import com.senla.carservice.util.utils.DateWorker;

public class CarService implements ICarService {
	private static Logger log = Logger.getLogger(CarService.class.getName()); 
	private static ICarService instance;
	private IGarageService garageService;
	private IMasterService masterService;
	private IOrderService orderService;

	public static ICarService getInstance() {
		if (instance == null) {
			instance = new CarService();
		}
		return instance;
	}

	private CarService() {
		this.garageService = (IGarageService) CoordinationRepository.getInstance().getObject(IGarageService.class);
		this.masterService = (IMasterService) CoordinationRepository.getInstance().getObject(IMasterService.class);
		this.orderService = (IOrderService) CoordinationRepository.getInstance().getObject(IOrderService.class);

	}

	@Override
	public void addGarage(Garage garage) {
		try {
			garageService.addGarage(garage);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Override
	public boolean removeGarage(Garage garage) {
		try {
			return garageService.removeGarage(garage);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public List<Garage> getGarages() {
		try {
			return garageService.getGarages();
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public void addMaster(Master master) {
		try {
			masterService.addMaster(master);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Override
	public boolean removeMaster(Master master) {
		try {
			return masterService.removeMaster(master);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public List<Master> getMasters() {
		try {
			return masterService.getMasters();
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public void addOrder(Order order) {
		try {
			orderService.addOrder(order);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Override
	public boolean removeOrder(Order order) {
		try {
			return orderService.removeOrder(order);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public boolean closeOrder(Order order) {
		try {
			return orderService.closeOrder(order);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public boolean cancelOrder(Order order) {
		try {
			return orderService.cancelOrder(order);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public List<Garage> getFreeGarages() {
		try {
			return garageService.getFreeGarages();
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> getOrders() {
		try {
			return orderService.getOrders();
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> getCurrentExecutingOrders() {
		try {
			return orderService.getCurrentExecutingOrders();
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public Order getOrderByMaster(Master master) {
		try {
			return orderService.getOrderByMaster(master);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public Master getMasterByOrder(Order order) {
		try {
			return orderService.getMasterByOrder(order);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> getExecutedOrders(Date startDate, Date endDate) {
		try {
			return orderService.getOrders(OrderState.EXECUTED, DateWorker.formatDate(startDate),
					DateWorker.formatDate(endDate));
		} catch (ParseException e) {
			log.error("ParseException", e);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> getCanceledOrders(Date startDate, Date endDate) {
		try {
			return orderService.getOrders(OrderState.CANCELED, DateWorker.formatDate(startDate),
					DateWorker.formatDate(endDate));
		} catch (ParseException e) {
			log.error("ParseException", e);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> getRemoteOrders(Date startDate, Date endDate) {
		try {
			return orderService.getOrders(OrderState.REMOTE, DateWorker.formatDate(startDate),
					DateWorker.formatDate(endDate));
		} catch (ParseException ex) {
			log.error("ParseException: ", ex);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public int getFreeCarServicePlaceNum(Date date) {
		try {
			date = DateWorker.formatDate(date);
			int freeMasters = masterService.getFreeMastersNumber(date) + orderService.getFreeMasterNumber(date);
			int freeGarages = garageService.getFreeGarageNumber() + orderService.getFreeGarageNumber(date);
			int freePlace = (freeMasters > freeGarages) ? freeGarages : freeMasters;
			return freePlace;
		} catch (ParseException ex) {
			log.error("ParseException: ", ex);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return 0;
	}

	@Override
	public Date getFreeDate() {
		try {
			Date date = new Date();
			date = DateWorker.formatDate(date);
			while (getFreeCarServicePlaceNum(date) == 0) {
				date = DateWorker.shiftDate(date, 1);
			}
			return date;
		} catch (ParseException e) {
			log.error("ParseException", e);
		}
		return null;
	}

	@Override
	public List<Order> sortOrdersBySubmissionDate() {
		try {
			return orderService.sort(SortOrderFields.submissionDate);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> sortOrdersByExecutionDate() {
		try {
			return orderService.sort(SortOrderFields.executionDate);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> sortOrdersByPlannedStartDate() {
		try {
			return orderService.sort(SortOrderFields.plannedStartDate);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Order> sortOrdersByPrice() {
		try {
			return orderService.sort(SortOrderFields.price);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public List<Master> sortMastersByAlphabetAscending() {
		try {
			return masterService.sort(SortMasterFields.name);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;

	}

	@Override
	public List<Master> sortMastersByAlphabetDescending() {
		try {
			return masterService.sort(SortMasterFields.name);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;

	}

	@Override
	public List<Master> sortMastersByEmployment() {
		try {
			return masterService.sort(SortMasterFields.isFree);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public void sortExecutingOrdersBySubmissionDate() {
		try {
			orderService.sortOrders(OrderState.EXECUTABLE, SortOrderFields.submissionDate);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Override
	public void sortExecutingOrdersByExecutionDate() {
		try {
			orderService.sortOrders(OrderState.EXECUTABLE, SortOrderFields.executionDate);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Override
	public void sortExecutingOrdersByPrice() {
		try {
			orderService.sortOrders(OrderState.EXECUTABLE, SortOrderFields.price);
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	@Override
	public boolean exit() {
		try {
			// !!!!!!!!!!!!!!!!!!!
			return true;
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public Garage getGarageById(Long id) {
		try {
			return garageService.getGarageById(id);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public Master getMasterById(Long id) {
		try {
			return masterService.getMasterById(id);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public Order getOrderById(Long id) {
		try {
			return orderService.getOrderById(id);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public void shiftExecution(int daysNum) {
		try {
			orderService.shiftExecution(daysNum);
		} catch (Exception e) {
			log.error("Exception", e);
		}

	}

	@Override
	public boolean assignMasterToOrder(Order order, Master master) {
		try {
			return orderService.assignMasterToOrder(order, master);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public boolean assignGarageToOrder(Order order, Garage garage) {
		try {
			return orderService.assignGarageToOrder(order, garage);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public List<Master> getFreeMasters() {
		try {
			return masterService.getFreeMasters();
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;
	}

	@Override
	public boolean updateOrder(Order order) {
		try {
			return orderService.updateOrder(order);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public Order getCopy(Order order) {
		try {
			return orderService.getCopy(order);
		} catch (CloneNotSupportedException e) {
			log.error("CopyNotSupportException", e);
		}
		return null;
	}

	@Override
	public boolean exportGarages(List<Garage> garages) {
		try {
			return garageService.exportGarages(garages);
		} catch (IOException e) {
			log.error("IOExcepton", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException", e);
		} catch (NoSuchFieldException e) {
			log.error("NoSuchFieldException", e);
		}
		return false;

	}

	@Override
	public boolean importGarages() {
		try {
			return garageService.importGarages();
		} catch (FileNotFoundException e) {
			log.error("FileNotFound", e);
		} catch (IOException e) {
			log.error("IOExcepton", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException", e);
		} catch (NoSuchFieldException e) {
			log.error("NoSuchFieldException", e);
		} catch (InstantiationException e) {
			log.error("InstantiationException", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException", e);
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException", e);
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException", e);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;

	}

	@Override
	public boolean exportMasters(List<Master> masters) {
		try {
			return masterService.exportMasters(masters);
		} catch (IOException e) {
			log.error("IOExcepton", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException", e);
		} catch (NoSuchFieldException e) {
			log.error("NoSuchFieldException", e);
		}
		return false;
	}

	@Override
	public boolean importMasters() {
		try {
			return masterService.importMasters();
		} catch (FileNotFoundException e) {
			log.error("FileNotFound", e);
		} catch (IOException e) {
			log.error("IOExcepton", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException", e);
		} catch (NoSuchFieldException e) {
			log.error("NoSuchFieldException", e);
		} catch (InstantiationException e) {
			log.error("InstantiationException", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException", e);
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException", e);
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException", e);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}

	@Override
	public boolean exportOrders(List<Order> orders) {
		try {
			return orderService.exportOrders(orders);
		} catch (IOException e) {
			log.error("IOExcepton", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException", e);
		} catch (NoSuchFieldException e) {
			log.error("NoSuchFieldException", e);
		}
		return false;
	}

	@Override
	public boolean importOrders() {
		try {
			return orderService.importOrders();
		} catch (FileNotFoundException e) {
			log.error("FileNotFound", e);
		} catch (IOException e) {
			log.error("IOExcepton", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException", e);
		} catch (NoSuchFieldException e) {
			log.error("NoSuchFieldException", e);
		} catch (InstantiationException e) {
			log.error("InstantiationException", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException", e);
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException", e);
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException", e);
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return false;
	}
}
