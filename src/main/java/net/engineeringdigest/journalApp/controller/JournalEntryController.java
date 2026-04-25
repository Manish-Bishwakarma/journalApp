package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> journalEntries = user.getEntries();
        if (journalEntries != null && !journalEntries.isEmpty()) {
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String username = authentication.getName();
          journalEntryService.saveJournalEntry(journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        // Here your credentials are authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //1. Find User
        User user = userService.findByUsername(username);

        //2. Check if User has the Journal Entry with the given ID
        List<JournalEntry> collect = user.getEntries().stream().filter(entry -> entry.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()) { // If the list is not empty, it means that the user has a journal entry with the given ID, so we can proceed to retrieve it from the database using the journal entry service. If the list is empty, it means that the user does not have a journal entry with the given ID, so we can return a 404 Not Found response.
            Optional<JournalEntry> journalEntryOptional = journalEntryService.getJournalEntryById(myId);
            if (journalEntryOptional.isPresent()) {
                return new ResponseEntity<JournalEntry>(journalEntryOptional.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteByJournalEntryId(myId, username);
        if (removed) {
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update
    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getEntries().stream().filter(entry -> entry.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()) { // If not empty mean the user is correct and he/she provide right ID to update
            Optional<JournalEntry> journalEntryOptional = journalEntryService.getJournalEntryById(myId);
            if (journalEntryOptional.isPresent()) {
                JournalEntry existingJournalEntry = journalEntryOptional.get();
                existingJournalEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : existingJournalEntry.getTitle());
                existingJournalEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : existingJournalEntry.getContent());
                journalEntryService.saveJournalEntry(existingJournalEntry);
                return new ResponseEntity<>(existingJournalEntry, HttpStatus.OK);
            }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}