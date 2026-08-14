package com.karmantial.groupservice.service;

import java.util.List;
import java.util.Optional;

import com.karmantial.groupservice.model.Group;
import com.karmantial.groupservice.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public Group saveGroup(Group group){
        return groupRepository.save(group);
    }

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }

    public Group editGroup(Long id, Group group){
        Optional<Group> existingGroup = groupRepository.findById(id);

        if (!existingGroup.isEmpty()) {
            Group foundGroup = existingGroup.get();
            foundGroup.setGroupName(group.getGroupName());
            return groupRepository.save(foundGroup);
        }
        throw new Error("Grupo no encontrado");
    }

    public boolean deleteGroup(Long id){
        Optional<Group> existingGroup = groupRepository.findById(id);

        if (!existingGroup.isEmpty()) {
            groupRepository.deleteById(id);
            return true;
        }
        throw new Error("Grupo no encontrado");
    }
}
