package com.karmantial.groupservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.karmantial.groupservice.model.Group;
import com.karmantial.groupservice.service.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups(){
        return ResponseEntity.ok(groupService.getGroups());
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(Group group){
        return ResponseEntity.ok(groupService.saveGroup(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> editGroup(@RequestBody Group group, @PathVariable Long id){
        return ResponseEntity.ok(groupService.editGroup(id, group));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id){
      Boolean deletedGroup = groupService.deleteGroup(id);
        if (deletedGroup) {
            return ResponseEntity.ok("Grupo eliminado");
        }
        throw new Error("Grupo no encontrado");
    }
}
