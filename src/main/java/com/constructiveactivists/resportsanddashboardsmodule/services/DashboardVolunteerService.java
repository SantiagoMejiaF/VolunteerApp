package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardService {

    private final VolunteerService volunteerService;

    public Map<String, Long> getAgeRanges() {

        return volunteerService.getAllVolunteers().stream()
                .map(volunteer -> volunteerService.calculateAge(volunteer.getPersonalInformation().getBornDate()))
                .collect(Collectors.groupingBy(age -> {
                    if (age > 100) {
                        return "100+";
                    } else {
                        int startRange = (age / 10) * 10;
                        int endRange = startRange + 9;
                        return startRange + "-" + endRange;
                    }
                }, TreeMap::new, Collectors.counting()));
    }

    public double getAverageAge() {
        return volunteerService.getAllVolunteers().stream()
                .mapToInt(volunteer -> volunteer.getPersonalInformation().getAge())
                .average()
                .orElse(0.0);
    }
}
