package com.school.erp.security;

import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Check Teacher first
        var teacherOpt = teacherRepository.findByPhone(phoneNumber);
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            if (!teacher.getIsActive()) {
                throw new UsernameNotFoundException("Teacher account is inactive");
            }
            return new UserDetailsImpl(teacher.getId(), phoneNumber, "ROLE_TEACHER");
        }

        // Check Student
        var studentOpt = studentRepository.findByPhone(phoneNumber);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (!student.getIsActive()) {
                throw new UsernameNotFoundException("Student account is inactive");
            }
            return new UserDetailsImpl(student.getId(), phoneNumber, "ROLE_STUDENT");
        }

        throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
    }
}
