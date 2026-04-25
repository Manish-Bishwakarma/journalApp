package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger; // slf4j is an abstraction of logback
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j //We will use Logback framework via Lombok's @Slf4j, which is logging abstraction framework (Simple Logging Facade for Java)
public class JournalEntryService {

//    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class); // to avoid accidental reassignment of the logger.

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveJournalEntry(JournalEntry journalEntry, String username) {
        try {
            User user = userService.findByUsername(username);
            journalEntry.setDate(java.time.LocalDateTime.now());

            JournalEntry save = journalEntryRepository.save(journalEntry);
            user.getEntries().add(save);
//            user.setUsername(null); // Check if Transaction Rollback when exception occurs
            userService.saveUser(user);
        } catch (Exception ex) {
            log.error("Error saving journal entry: {}", ex.getMessage());
            log.error("Stack trace: ", ex);
        }
    }

    // Method of Update
    public void saveJournalEntry(JournalEntry journalEntry) {
        try {
            journalEntryRepository.save(journalEntry);
        } catch (Exception ex) {
            log.error("Error saving journal entry: {}", ex.getMessage());
            log.error("Stack trace: ", ex);
        }
    }

    public List<JournalEntry> getAllJournalEntry() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getJournalEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteByJournalEntryId(ObjectId id, String username) {
        boolean removed = false;
        try {
            // 1. Find User
            User user = userService.findByUsername(username); // we need to find the user by their username using the user service, so that we can associate the journal entry with the user.
            // 2. Remove the journal entry from the user's list of entries
            removed = user.getEntries().removeIf(entry -> entry.getId().equals(id));// we need to remove the journal entry from the user's list of entries, so that we can maintain the relationship between the user and their journal entries. The 'removeIf' method is used to remove the journal entry with the specified ID from the user's list of entries.
            if(removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception ex) {
            log.error("Error deleting journal entry: {}", ex.getMessage());
        }
        return removed;
    }

    public List<JournalEntry> getJournalEntriesByUsername(String username) {
        User user = userService.findByUsername(username);
        return user.getEntries();
    }
}
