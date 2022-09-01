package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, StudentIdCardRepository studentIdCardRepository){
        return args -> {
            generateData(studentIdCardRepository,studentRepository);
        };
    }

    private void pagingStudents(StudentRepository studentRepository){
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("firstName").ascending());
        Page<Student> page=studentRepository.findAll(pageRequest);
        System.out.println(page);
    }

    private void sortingStudents(StudentRepository studentRepository){
        Sort sort = Sort.by("firstName").ascending()
                .and(Sort.by("age")).descending();
        studentRepository.findAll(sort).forEach(System.out::println);
    }


    private void generateData(StudentIdCardRepository studentIdCardRepository, StudentRepository studentRepository){
        Faker faker = new Faker();
        for(int i = 0; i<=20;i++){
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu", firstName,lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));
            student.addBook(new Book(LocalDateTime.now().minusDays(4), "Clean Code"));
            student.addBook(new Book(LocalDateTime.now().minusMonths(4), "Think and grow rich"));
            student.addBook(new Book(LocalDateTime.now().minusYears(4), "Spring Data JPA"));
            StudentIdCard studentIdCard = new StudentIdCard(faker.number().digits(9).toString(), student);
            student.setStudentIdCard(studentIdCard);
            student.addEnrolment(new Enrolment(new EnrolmentId(1L, 1L),student,new Course("Computer Science", "IT"), LocalDateTime.now()));
            student.addEnrolment(new Enrolment(new EnrolmentId(1L, 2L),student, new Course( "Amigoscode Spring Data JPA", "IT"), LocalDateTime.now()));

            studentRepository.save(student);

            studentRepository.findById(1L)
                    .ifPresent(s->{
                        System.out.println("fetch book lazy...");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                        });

                    });
//            studentRepository.findById(1L)
//                    .ifPresent(System.out::println);
        }
    }
}
