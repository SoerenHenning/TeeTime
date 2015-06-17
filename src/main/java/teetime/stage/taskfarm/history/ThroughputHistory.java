package teetime.stage.taskfarm.history;

import java.util.LinkedList;
import java.util.List;

public class ThroughputHistory {

	private final static int MAX_ENTRIES = 20;

	private final LinkedList<ThroughputEntry> entries = new LinkedList<ThroughputEntry>();

	public List<ThroughputEntry> getEntries() {
		return entries;
	}

	public void addEntry(final ThroughputEntry entry) {
		this.entries.addFirst(entry);
		if (this.entries.size() > MAX_ENTRIES) {
			this.entries.removeLast();
		}
	}
}
