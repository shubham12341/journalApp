package com.shubhamgangurde.journalApp.service;

import com.shubhamgangurde.journalApp.entity.JournalEntry;
import com.shubhamgangurde.journalApp.entity.User;
import com.shubhamgangurde.journalApp.repository.JournalEntryRepository;
import com.shubhamgangurde.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    Authentication authentication = null;



    // To add a new Journal Entry in our database
    @Transactional
    public void addNewJournalEntry(JournalEntry journalEntry, String userName){
        try{
            journalEntry.setDate(LocalDateTime.now());
            User user = userService.getUserByUserName(userName);
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(savedEntry);
            userService.addUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occured while saving the Journal entry!", e);
        }

    }
    // To add a new Journal Entry in our database
    public void addNewJournalEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    // To get all the Journal Entries from our database
    public List<JournalEntry> getAllJournalEntries(){
        return journalEntryRepository.findAll();
    }
  
    // To get a particular Journal Entry by ID from our database
    public Optional<JournalEntry> getJournalEntryById(ObjectId journalId){
        return journalEntryRepository.findById(journalId);
    }

    // To delete a particular Journal Entry by ID from our database
    @Transactional
    public boolean deleteJournalEntryById(ObjectId journalId, String userName){
        boolean removedEntry = false;
        try{
            User user = userService.getUserByUserName(userName);
            removedEntry = user.getJournalEntries().removeIf(x -> x.getId().equals(journalId));
            if(removedEntry){
                userService.addUser(user);
                journalEntryRepository.deleteById(journalId);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error has occured while deleting the journal entry: ", e);
        }
        return removedEntry;

    }

    // To update an existing Journal Entry in our database
    @Transactional
    public JournalEntry updateJournalEntry(ObjectId journalId, JournalEntry newJournalEntry) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUserByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(journalId)).toList();
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryRepository.findById(journalId);
            if (journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : oldEntry.getContent());
                return journalEntryRepository.save(oldEntry);

            }
        }
        return newJournalEntry;
    }
}
