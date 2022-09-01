package com.example.demo;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentByEmail(String email);

    List<Student> findStudentsByFirstNameEqualsAndAgeGreaterThanEqual(String name, Integer age);

    @Query(value = "SELECT * FROM Student WHERE first_name=:firstName AND age >= :age",
            nativeQuery = true)
    List<Student> findStudentsByFirstNameEqualsAndAgeGreaterThanEqualNative(@Param("firstName")String firstName,
                                                                            @Param("age")Integer age);
//    NamedParams
//    @Query(value="SELECT * FROM Student WHERE first_name=:firstName AND age >= :age",
//            nativeQuery=true)
//    List<Student> findStudentsByFirstNameEqualsAndAgeGreaterThanEqualNative(@Param("firstName") String firstName,@Param("age") Integer age);

    @Transactional
    @Modifying
    @Query("DELETE FROM Student u WHERE u.id=?1")
    int deleteStudentById(Long id);
}
