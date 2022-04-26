package io.github.greenmc.retropvp.user.scoreboard;

import me.despical.commons.scoreboard.type.Entry;

import java.util.LinkedList;
import java.util.List;

public class EntryBuilder {

	private final List<Entry> entries = new LinkedList<>();

	public void put(String string, int position) {
		entries.add(new Entry(string, position));
	}

	public List<Entry> get() {
		return entries;
	}

}