package dalawow.autovote;

import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

	private static final class Login {
		static Login of(final String login, final String password) {
			return new Login(login, password);
		}
		final String login;
		final String password;
		private Login(final String login, final String password) {
			this.login = login;
			this.password = password;
		}
	}

	private static final void waitMS(final long from, final long to) throws InterruptedException {
		final long nextLong;
		if (from >= to) {
			nextLong = from;
		} else {
			nextLong = ThreadLocalRandom.current().nextLong(from, to);
		}
		Thread.sleep(nextLong);
	}

	public static void main(String[] args) throws InterruptedException {

		final Login[] logins = new Login[] {
				Login.of("XXX", "YYY")
			};

		for (final Login login : logins) {

			// Create an instance of the driver
			WebDriver driver = new FirefoxDriver();

			// Navigate to a web page
			driver.get("https://www.dalaran-wow.com/account-log/");

			// Perform actions on HTML elements, entering text and submitting the form
			WebElement usernameElement = driver.findElement(By.id("accountName"));
			WebElement passwordElement = driver.findElement(By.id("password"));

			usernameElement.sendKeys(login.login);
			passwordElement.sendKeys(login.password);

			waitMS(500, 3500);

			passwordElement.submit(); // submit by text input element

			// Anticipate web browser response, with an explicit wait
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("page-menu")));

			waitMS(1500, 2500);

			driver.get("https://www.dalaran-wow.com/account/vote/");
			WebDriverWait wait2 = new WebDriverWait(driver, 10);
			wait2.until(ExpectedConditions.presenceOfElementLocated(By.id("howitworks")));

			waitMS(4000, 6000);

			for (int i = 1; i <= 4; i++) {
				driver.get("https://www.dalaran-wow.com/account/vote/" + i);
				waitMS(6000, 10000);
			}

			// Conclude a test
			driver.quit();
			waitMS(1500, 2500);
		}

	}
}
