package com.senla.carservice.model.beans;

import java.text.ParseException;
import java.util.Date;

import com.senla.carservice.model.beans.abstractentity.Entity;
import com.senla.carservice.model.orderstate.OrderState;

public class Order extends Entity {
	private long id;
	private Date submissionDate;
	private Date executionDate;
	private Date plannedStartDate;
	private double price;
	private OrderState state;
	private Garage garage;
	private Master master;

	public Order(Date submissionDate, Date executionDate, Date plannedStartDate, double price, Garage garage)
			throws ParseException {
		this.submissionDate = submissionDate;
		this.executionDate = executionDate;
		this.plannedStartDate = plannedStartDate;
		this.price = price;
		state = OrderState.EXECUTABLE;
		garage.setIsFree(false);
		this.garage = garage;
	}

	public Order(long id, Date submissionDate, Date executionDate, Date plannedStartDate, double price,
			OrderState state, Garage garage, Master master) throws ParseException {
		this(submissionDate, executionDate, plannedStartDate, price, garage);
		this.id = id;
		this.state = state;
		this.master = master;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public double getPrice() {
		return price;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public Garage getGarage() {
		return garage;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}

	@Override
	public boolean equils(Object o) {
		return (id == ((Order) o).getId()) ? true : false;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(id).append(' ').append("submissionDate: ").append(submissionDate).append(' ')
				.append("executionDate: ").append(executionDate).append(' ').append("plannedStartDate: ")
				.append(plannedStartDate).append(' ').append(price).append(' ').append(state).append(' ')
				.append("garage: ").append(garage.getId()).append(' ').append(garage.getIsFree());
		return str.toString();
	}
}
