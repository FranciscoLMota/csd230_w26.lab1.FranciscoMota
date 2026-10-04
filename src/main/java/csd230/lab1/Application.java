package csd230.lab1;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.CartEntity;

import csd230.lab1.repositories.BookEntityRepository;
import csd230.lab1.repositories.CartEntityRepository;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class Application  implements CommandLineRunner {
	private final BookEntityRepository bookRepository;
	private final CartEntityRepository cartRepository;

	public Application(BookEntityRepository bookRepository, CartEntityRepository cartRepository) {
		this.bookRepository = bookRepository;
		this.cartRepository = cartRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		if (bookRepository.count() > 0) {
			return;
		}


		BookEntity first = new BookEntity();
		first.setTitle("Spring MVC Basics");
		first.setPrice(29.99);
		first.setCopies(5);
		first.setAuthor("Course Example");
		first.setIsbn("0-2680-1008-0");
		bookRepository.save(first);


		BookEntity second = new BookEntity();
		second.setTitle("Thymeleaf in Practice");
		second.setPrice(34.99);
		second.setCopies(4);
		second.setAuthor("Course Example");
		second.setIsbn("0-8655-0705-8");
		bookRepository.save(second);


		cartRepository.findById(1L)
				.orElseGet(() -> cartRepository.save(new CartEntity()));


	}

}