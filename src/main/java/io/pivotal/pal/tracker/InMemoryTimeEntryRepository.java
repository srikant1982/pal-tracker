package io.pivotal.pal.tracker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private AtomicLong idSequence = new AtomicLong(1);
    private Map<Long, TimeEntry> timeEntries = new HashMap<>();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        System.out.println("--------"+timeEntry.toString());
        timeEntry.setId(idSequence.getAndIncrement());
        System.out.println("::::::::::"+timeEntry.toString());
        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntries.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        ArrayList<TimeEntry> timeEntries = new ArrayList<>(this.timeEntries.values());
        return timeEntries.size() > 0 ? timeEntries : null;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry upatedTimeEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        if (timeEntries.get(id) == null) {
            return null;
        }
        timeEntries.replace(id, upatedTimeEntry);
        return upatedTimeEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        timeEntries.remove(timeEntryId);
    }
}
