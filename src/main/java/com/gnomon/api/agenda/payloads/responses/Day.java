package com.gnomon.api.agenda.payloads.responses;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Day implements Comparable<Day> {
	
	LocalDate date;
	List<AgendaEventSummary> events;
	
	public Day(LocalDate date) {
		this.date = date;
		this.events = new ArrayList<AgendaEventSummary>();
	}
	
	public Day(LocalDate date, List<AgendaEventSummary> events) {
		this.date = date;
		this.events = events;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<AgendaEventSummary> getEvents() {
		return events;
	}

	public void setEvents(List<AgendaEventSummary> events) {
		this.events = events;
	}

	@Override
	public int compareTo(Day day) {
		return date.compareTo(day.getDate());
	}
}
