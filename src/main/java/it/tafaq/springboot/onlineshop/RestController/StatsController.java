package it.tafaq.springboot.onlineshop.RestController;

import it.tafaq.springboot.onlineshop.dto.StatsDto;
import it.tafaq.springboot.onlineshop.entity.User;
import it.tafaq.springboot.onlineshop.service.StatsService;
import it.tafaq.springboot.onlineshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;

    public StatsController(StatsService statsService , UserService userService) {
        this.statsService = statsService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<StatsDto> getStats(@RequestParam int year) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email);
        StatsDto stats;
        if (currentUser.getRole().equals("ROLE_SUPER_ADMIN")) {
            stats = statsService.getGlobalStatsByYear(year);
        }
        else {
            stats = statsService.getAdminStatsByYear(year , currentUser.getId());
        }
        return ResponseEntity.ok(stats);
    }

}
