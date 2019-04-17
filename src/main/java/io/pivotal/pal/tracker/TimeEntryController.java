package io.pivotal.pal.tracker;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {


    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry readTimeEntry = timeEntryRepository.find(timeEntryId);
        if (readTimeEntry != null) {
            return ResponseEntity.ok(readTimeEntry);
        }
        return new ResponseEntity(readTimeEntry,HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntryList = timeEntryRepository.list();
        return new ResponseEntity<>(timeEntryList, HttpStatus.OK);
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry responseEntity = timeEntryRepository.update(timeEntryId,timeEntry);
        if (responseEntity != null) {
            return ResponseEntity.ok(responseEntity);
        }
        return new ResponseEntity(responseEntity,HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
