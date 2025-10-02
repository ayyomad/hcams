package com.example.hospitalmvp;

import com.example.hospitalmvp.dto.AppointmentDtos;
import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.PatientRepository;
import com.example.hospitalmvp.repository.SlotRepository;
import com.example.hospitalmvp.service.AppointmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private SlotRepository slotRepository;

    @Test
    void preventsDoubleBooking() {
        Patient p = patientRepository.findAll().get(0);
        Doctor d = doctorRepository.findAll().get(0);
        List<Slot> slots = slotRepository.findByDoctor(d);
        Slot s = slots.stream().filter(Slot::isAvailable).findFirst().orElseThrow();
        Long appt1 = appointmentService.book(new AppointmentDtos.BookRequest(p.getId(), d.getId(), s.getId()));
        Assertions.assertNotNull(appt1);
        Assertions.assertThrows(IllegalStateException.class,
                () -> appointmentService.book(new AppointmentDtos.BookRequest(p.getId(), d.getId(), s.getId())));
    }
}
