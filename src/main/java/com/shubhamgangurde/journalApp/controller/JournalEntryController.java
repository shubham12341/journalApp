package com.shubhamgangurde.journalApp.controller;

import com.shubhamgangurde.journalApp.entity.JournalEntry;
import com.shubhamgangurde.journalApp.entity.User;
import com.shubhamgangurde.journalApp.repository.UserRepository;
import com.shubhamgangurde.journalApp.service.JournalEntryService;
import com.shubhamgangurde.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;
    Authentication authentication = null;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUserByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if(journalEntries != null && !journalEntries.isEmpty()){
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity<JournalEntry> addNewJournalEntryOfUser(@RequestBody JournalEntry journalEntry){
        try{
            authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.addNewJournalEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{journalId}")
    public ResponseEntity<JournalEntry> getJournalEntryByIdOfUser(@PathVariable ObjectId journalId){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUserByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(journalId)).toList();
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(journalId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{journalId}")
    public ResponseEntity<?> deleteJournalEntryByIdOfUser(@PathVariable ObjectId journalId){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removedEntry = journalEntryService.deleteJournalEntryById(journalId, userName);
        if(removedEntry) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("id/{journalId}")
    public ResponseEntity<JournalEntry>  updateJournalEntryOfUser(@PathVariable ObjectId journalId, @RequestBody JournalEntry newJournalEntry){
        try {
            JournalEntry journalEntry = journalEntryService.updateJournalEntry(journalId,newJournalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
