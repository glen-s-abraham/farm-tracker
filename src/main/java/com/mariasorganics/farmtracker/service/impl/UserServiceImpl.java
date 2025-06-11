package com.mariasorganics.farmtracker.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mariasorganics.farmtracker.entity.AppUser;
import com.mariasorganics.farmtracker.exception.ResourceNotFoundException;
import com.mariasorganics.farmtracker.repository.AppUserRepository;
import com.mariasorganics.farmtracker.service.IUserService;
import static com.mariasorganics.farmtracker.service.impl.helpers.UserSpecifications.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final AppUserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<AppUser> getPaginated(int page, int size, String keyword, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField != null ? sortField : "id");
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return repo.findAll(hasUsernameLike(keyword), pageable);
    }

    @Override
    public AppUser getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public AppUser save(AppUser user) {
        if (user.getId() == null || !user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repo.save(user);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
