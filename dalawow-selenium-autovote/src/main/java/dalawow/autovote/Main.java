package dalawow.autovote;

import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

	/**
	 * Structure to store login information (actually a simple pair of strings with
	 * long / password).
	 */
	private static final class Login {
		static Login of(final String id, final String password) {
			return new Login(id, password);
		}

		final String id;
		final String password;

		private Login(final String id, final String password) {
			this.id = id;
			this.password = password;
		}
	}

	/**
	 * Make current thread sleep for a random amount of time, between 'from' and
	 * 'to' millis.
	 *
	 * If from >= to, sleep exactly 'from' millis.
	 */
	private static final void waitMS(final long from, final long to) throws InterruptedException {
		final long nextLong;
		if (from >= to) {
			nextLong = from;
		} else {
			nextLong = ThreadLocalRandom.current().nextLong(from, to);
		}
		Thread.sleep(nextLong);
	}

	/**
	 * Read command line arguments and buildup a proper array of {@link Login}
	 */
	private static final Login[] parseArgs(final String[] args) {
		// TODO
		return new Login[] { Login.of("XXX", "YYY") };
	}

	/**
	 * Use selenium to access dalawow site and perform the 4 votes
	 */
	private static final void voteFor(final Login login) throws InterruptedException {

		// Create an instance of the driver
		final WebDriver driver = new FirefoxDriver();

		// Navigate to a web page
		driver.get("https://www.dalaran-wow.com/account-log/");

		// Perform actions on HTML elements, entering text and submitting the form
		final WebElement usernameElement = driver.findElement(By.id("accountName"));
		final WebElement passwordElement = driver.findElement(By.id("password"));

		usernameElement.sendKeys(login.id);
		passwordElement.sendKeys(login.password);

		waitMS(500, 3500);

		passwordElement.submit(); // submit by text input element

		// Anticipate web browser response, with an explicit wait
		final WebDriverWait waitLoginSuccessfull = new WebDriverWait(driver, 20);
		try {
			waitLoginSuccessfull.until(ExpectedConditions.presenceOfElementLocated(By.id("page-menu")));
		} catch (final TimeoutException e) {
			System.err.println("Login for user '"+login.id+"' timed out.");
			return;
		}

		waitMS(1500, 2500);

		driver.get("https://www.dalaran-wow.com/account/vote/");
		final WebDriverWait waitVoteReady = new WebDriverWait(driver, 10);
		waitVoteReady.until(ExpectedConditions.presenceOfElementLocated(By.id("howitworks")));

		waitMS(2000, 3000);

		for (int i = 1; i <= 4; i++) {
			driver.get("https://www.dalaran-wow.com/account/vote/" + i);
			waitMS(6000, 10000);
		}

		// Conclude a test
		driver.quit();
		waitMS(1500, 2500);
	}

	public static void main(final String[] args) throws InterruptedException {
		final Login[] logins = parseArgs(args);
		for (final Login login : logins) {
			voteFor(login);
		}
	}
}
