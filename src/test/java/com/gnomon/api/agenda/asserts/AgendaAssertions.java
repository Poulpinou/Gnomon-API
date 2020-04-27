package com.gnomon.api.agenda.asserts;

import com.gnomon.api.agenda.models.Agenda;

public class AgendaAssertions {
	public static AgendaAssert assertThat(Agenda agenda) {
		return new AgendaAssert(agenda);
	}
}
