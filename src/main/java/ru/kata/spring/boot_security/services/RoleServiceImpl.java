package ru.kata.spring.boot_security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.models.Role;
import ru.kata.spring.boot_security.repositories.RoleRepository;

import java.util.HashSet;
import java.util.Set;


@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Set<Role> getRoles() {
        return new HashSet<>(roleRepository.findAll());
    }


    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }


    @Override
    public Role findByStringId(String sid) {
        Long LongId = Long.parseLong(sid);
        return roleRepository.findById(LongId).orElse(null);
    }


    @Override
    public Role findByName(String name) {
        return roleRepository.findByNameOfRole(name);
    }


    @Override
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
