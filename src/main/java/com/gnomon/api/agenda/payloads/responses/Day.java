package com.gnomon.api.agenda.payloads.responses;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Day implements Comparable<Day> {
	
	LocalDate date;
	List<EventSummary> events;
	
	public Day(LocalDate date) {
		this.date = date;
		this.events = new ArrayList<EventSummary>();
	}
	
	public Day(LocalDate date, List<EventSummary> events) {
		this.date = date;
		this.events = events;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<EventSummary> getEvents() {
		return events;
	}

	public void setEvents(List<EventSummary> events) {
		this.events = events;
	}

	@Override
	public int compareTo(Day day) {
		return date.compareTo(day.getDate());
	}
}
