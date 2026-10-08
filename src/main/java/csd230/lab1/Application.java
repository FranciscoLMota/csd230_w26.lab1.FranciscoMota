package csd230.lab1;


import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import csd230.lab1.entities.*;
import csd230.lab1.pojos.Cart;
import csd230.lab1.pojos.Magazine;
import csd230.lab1.pojos.Product;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import csd230.lab1.repositories.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Optional;


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
		String name = cm.productName();
		String description = cm.material();
		String priceString = faker.commerce().price();

		for (int i = 0; i < 5; i++) {
			BookEntity book = new BookEntity(
					fakeBook.title(),
					Double.parseDouble(priceString),
					10,
					fakeBook.author());
			;
			productRepository.save(book);
		}



		// --- START NEW CODE ---
		MagazineEntity magazine = new MagazineEntity(
				faker.lorem().word() + " Magazine",
				12.99,
				20,
				50,
				java.time.LocalDateTime.now()
		);
		productRepository.save(magazine);

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
}