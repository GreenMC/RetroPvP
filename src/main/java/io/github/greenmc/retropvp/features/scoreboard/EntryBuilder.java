package io.github.greenmc.retropvp.features.scoreboard;

import me.despical.commons.scoreboard.type.Entry;

import java.util.LinkedList;
import java.util.List;

public class EntryBuilder {

	private final List<Entry> entries = new LinkedList<>();

	public EntryBuilder put(String string, int position) {
		entries.add(new Entry(string, position));
		return this;
	}

	public List<Entry> get() {
		return entries;
	}

}