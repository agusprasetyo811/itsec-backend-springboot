package agus.prasetyo.backend.apps.service;

import agus.prasetyo.backend.apps.model.dto.RoleDTO;
import agus.prasetyo.backend.apps.model.db.Role;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.repository.RoleRepository;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleService(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(user -> modelMapper.map(user, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "all_role")
    public List<RoleDTO> findAll(String sortBy, String sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        return roleRepository.findAll(sort).stream()
                .map(el -> modelMapper.map(el, RoleDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<RoleDTO> findById(UUID id) {
        return roleRepository.findById(id).map(el -> modelMapper.map(el, RoleDTO.class));
    }

    // @Cacheable(value = "detail_role", key = "#id")
    public RoleDTO findByIdOrThrow(UUID id) throws ResourceNotFoundException {
        return roleRepository.findById(id)
                .map(el -> modelMapper.map(el, RoleDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
    }


    public RoleDTO create(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role saved = roleRepository.save(role);
        return modelMapper.map(saved, RoleDTO.class);
    }

    public RoleDTO update(UUID id, RoleDTO roleDTO) {
        return roleRepository.findById(id)
                .map(role -> {
                    if (roleDTO.getName() != null) {
                        role.setName(roleDTO.getName());
                    }
                    if (roleDTO.getRules() != null) {
                        role.setRules(roleDTO.getRules());
                    }
                    Role updated = roleRepository.save(role);
                    return modelMapper.map(updated, RoleDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
    }

    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }


}
