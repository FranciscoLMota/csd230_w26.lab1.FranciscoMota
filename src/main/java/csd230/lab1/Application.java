package csd230.lab1;


import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.ProductEntity;
import csd230.lab1.entities.UserEntity;
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


		BookEntity book = new BookEntity(
				fakeBook.title(),
				Double.parseDouble(priceString),
				10,
				fakeBook.author());
		;


		// --- START NEW CODE ---
		csd230.lab1.entities.MagazineEntity magazine = new csd230.lab1.entities.MagazineEntity(
				faker.lorem().word() + " Magazine",
				12.99,
				20,
				50,
				java.time.LocalDateTime.now()
		);


		CartEntity cart = new CartEntity();
		cartRepository.save(cart);


		// create a book
		// add book to the cart
		cart.addProduct(book);
		// book.setCart(cart); // dont have to set cart because cart.addProduct() does it for you
		cartRepository.save(cart);


		cart.addProduct(magazine);
		// magazine.setCart(cart);
		cartRepository.save(cart);


		// productRepository.save(book);


		List<ProductEntity> allProducts = productRepository.findAll();


		for (ProductEntity p : allProducts) {
			System.out.println(p.toString());
		}
		List<CartEntity> allCarts = cartRepository.findAll();
		for (CartEntity c : allCarts) {
			System.out.println(c.toString());
			for (ProductEntity p : c.getProducts()) {
				System.out.println(p.toString());
			}
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