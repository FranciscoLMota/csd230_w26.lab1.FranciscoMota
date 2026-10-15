package csd230.lab1;


import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import csd230.lab1.entities.*;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import csd230.lab1.repositories.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.List;
import java.time.LocalDateTime;


@SpringBootApplication
public class Application implements CommandLineRunner {
	private final ProductEntityRepository productRepository;
	private final CartEntityRepository cartRepository;
	private final UserEntityRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public Application(ProductEntityRepository productRepository,
	                   CartEntityRepository cartRepository,
	                   UserEntityRepository userRepository,
	                   PasswordEncoder passwordEncoder
	) {
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Faker faker = new Faker();
		Commerce cm = faker.commerce();
		com.github.javafaker.Number number = faker.number();
		com.github.javafaker.Book fakeBook = faker.book();
		for (int i = 0; i < 10; i++) {
			BookEntity book = new BookEntity(
					fakeBook.title(),
					Double.parseDouble(faker.commerce().price()),
					10,
					fakeBook.author(),
					faker.numerify("978-#-####-####-#"));
			productRepository.save(book);
		}

		for (int i = 0; i < 10; i++) {
			productRepository.save(new MagazineEntity(
					cm.department() + " Magazine",
					Double.parseDouble(cm.price()),
					number.numberBetween(5, 30),
					number.numberBetween(10, 100),
					LocalDateTime.now().minusMonths(number.numberBetween(0, 12))));
		}

		for (int i = 0; i < 10; i++) {
			productRepository.save(new DiscMagEntity(
					cm.department() + " Disc Magazine",
					Double.parseDouble(cm.price()),
					number.numberBetween(5, 30),
					number.numberBetween(10, 100),
					LocalDateTime.now().minusMonths(number.numberBetween(0, 12)),
					faker.bool().bool()));
		}

		for (int i = 0; i < 10; i++) {
			productRepository.save(new TicketEntity(
					faker.music().genre() + " concert ticket",
					Double.parseDouble(cm.price())));
		}

		List<ProductEntity> allProducts = productRepository.findAll();


		for (ProductEntity p : allProducts) {
			System.out.println(p.toString());
		}

		// ------------------------------------
		// CREATE USERS (Lecture 2.6)
		// ------------------------------------


		// Admin User (Can Add/Edit/Delete)
		UserEntity admin = new UserEntity("admin", passwordEncoder.encode("admin"), "ADMIN");
		userRepository.save(admin);


		// Regular User (Can only View/Buy)
		UserEntity user = new UserEntity("user", passwordEncoder.encode("user"), "USER");
		userRepository.save(user);


		System.out.println("Default users created: admin/admin and user/user");


	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}

}
