import com.codeborne.selenide.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BugHuntersTest {
  @Test
  public void startMission() {
    open("https://hackthefuture.bignited.be");
    $(By.className("typing")).shouldBe(visible, Duration.ofSeconds(2)).shouldHave(text("Mayday! We have a problem!"));
    $(By.id("action-button")).shouldBe(visible, Duration.ofSeconds(5)).click();
    $(By.className("typing")).shouldBe(visible, Duration.ofSeconds(2)).shouldHave(text("Going towards the transmission"));
  }

  @Test
  public void fillInformation() {
    open("https://hackthefuture.bignited.be/information");
    $(By.className("input-container")).shouldBe(visible);

    $(By.id("name")).shouldBe(visible);
    $(By.id("name")).setValue("Noah");

    $(By.id("age")).shouldBe(visible);
    $(By.id("age")).setValue("22");

    $(By.id("species")).shouldBe(visible);
    $(By.id("species")).click();
    $(By.id("species")).selectOption("Human");

    $(By.id("planet")).shouldBe(visible);
    $(By.id("planet")).setValue("Earth");

    $(By.id("planet")).pressEnter();

    $(By.className("typing")).shouldBe(visible).shouldHave(text("""
            Thank you for your information
            Noah
            Age: 22
            Species: human
            From: Earth
            Your access is now granted, good luck on your mission"""));
  }

  @Test
  public void leaveInformationEmpty() {
    open("https://hackthefuture.bignited.be/information");
    $(By.id("planet")).pressEnter();
    $(By.className("typing")).shouldBe(visible).shouldHave(text("Please fill in all fields..."));
    $(By.id("planet")).pressEnter();
    $(By.id("planet")).pressEnter();
    $(By.id("planet")).pressEnter();
    $(By.id("planet")).pressEnter();
    $(By.id("planet")).pressEnter();
    $(By.className("typing")).shouldBe(visible).shouldHave(text("Please don't harass the machine"));
  }

  @Test
  public void completeGame() throws InterruptedException {
    open("https://hackthefuture.bignited.be/collect-code");

    SelenideElement image = $(By.cssSelector("img[src='assets/images/victim.png'][alt='victim']"));

    Actions action = new Actions(WebDriverRunner.getWebDriver());
    action.moveToElement(image, 24, 78).click().perform();

    int code = Integer.parseInt($(By.className("murder")).shouldBe(visible).getText());
    System.out.println(code);

    $(By.className("murder")).shouldBe(visible).click();
    $(By.className("ski-button")).shouldBe(visible).click();

    int localStorageCode = Integer.parseInt(Objects.requireNonNull(executeJavaScript("return localStorage.getItem('code');")));
    System.out.println(localStorageCode);

    int digit1 = code/1000;
    int digit2 = code/100 % 10;
    int digit3 = code/10 % 10;
    int digit4 = code % 10;

    $(By.id("numpad")).shouldBe(visible, Duration.ofSeconds(20)).click();
    $(By.id(String.valueOf(digit1))).shouldBe(visible, Duration.ofSeconds(10)).click();
    $(By.id("numpad")).shouldBe(visible, Duration.ofSeconds(1)).click();
    $(By.id(String.valueOf(digit2))).shouldBe(visible, Duration.ofSeconds(10)).click();
    $(By.id("numpad")).shouldBe(visible, Duration.ofSeconds(1)).click();
    $(By.id(String.valueOf(digit3))).shouldBe(visible, Duration.ofSeconds(10)).click();
    $(By.id("numpad")).shouldBe(visible, Duration.ofSeconds(1)).click();
    $(By.id(String.valueOf(digit4))).shouldBe(visible, Duration.ofSeconds(10)).click();
    $(By.id("numpad")).shouldBe(visible, Duration.ofSeconds(1)).click();
    $(By.id("enter")).shouldBe(visible, Duration.ofSeconds(10)).click();

    Thread.sleep(10000);
    action.sendKeys(Keys.ARROW_UP).perform();

    Thread.sleep(30000);
    $(By.className("typing")).shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("Are you here to help?"));

    SelenideElement professor = $(By.className("professor")).shouldBe(visible);
    Selenide.executeJavaScript("arguments[0].style.display='none';", professor);

    /*
    Thread.sleep(10000);
    Point point = $$(".buttons button").findBy(text("Yes")).getLocation();
    action.moveByOffset(point.getX(), point.getY()).click().perform();

    Thread.sleep(10000);
    $(By.className("typing")).shouldBe(visible).shouldHave(text("I'll tell you what happened"));
    $(By.className("ng-star-inserted")).shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("chevron_right")).click();
    Thread.sleep(10000);
    $(By.className("typing")).shouldBe(visible).shouldHave(text("An experiment went wrong here. Some kind of reaction changed the DNA of our animal experiment..."));
    $(By.className("ng-star-inserted")).shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("chevron_right")).click();
    Thread.sleep(10000);
    $(By.className("typing")).shouldBe(visible).shouldHave(text("You have to scan the meteor that created the reaction, maybe we can find a solution this way"));
    */
    open("https://hackthefuture.bignited.be/scanner");

    $(By.className("floating-cube")).shouldBe(visible, Duration.ofSeconds(10)).click();
    SelenideElement scanButton = $(By.className("scan-button"));
    // hold down the scan button
    action.clickAndHold(scanButton).perform();
    Thread.sleep(20000);
    action.release().perform();

    Thread.sleep(10000);
    int currentReading = Integer.parseInt($(By.id("current-reading")).getText());
    int wantedReading = Integer.parseInt($(By.id("wanted-reading")).getText());

    Thread.sleep(2000);
    SelenideElement arrowUp1 = $$(By.className("up")).get(0).shouldBe(visible);
    SelenideElement arrowUp2 = $$(By.className("up")).get(1).shouldBe(visible);
    SelenideElement arrowUp3 = $$(By.className("up")).get(2).shouldBe(visible);
    SelenideElement arrowUp4 = $$(By.className("up")).get(3).shouldBe(visible);

    SelenideElement arrowDown1 = $$(By.className("down")).get(0).shouldBe(visible);
    SelenideElement arrowDown2 = $$(By.className("down")).get(1).shouldBe(visible);
    SelenideElement arrowDown3 = $$(By.className("down")).get(2).shouldBe(visible);
    SelenideElement arrowDown4 = $$(By.className("down")).get(3).shouldBe(visible);

    while (currentReading != wantedReading) {
      if (currentReading/1000 < wantedReading/1000) {
        arrowUp1.click();
        currentReading += 1000;
      }
      if (currentReading/1000 > wantedReading/1000) {
        arrowDown1.click();
        currentReading -= 1000;
      }
      if (currentReading/100 % 10 < wantedReading/100 % 10) {
        arrowUp2.click();
        currentReading += 100;
      }
      if (currentReading/100 % 10 > wantedReading/100 % 10) {
        arrowDown2.click();
        currentReading -= 100;
      }
      if (currentReading/10 % 10 < wantedReading/10 % 10) {
        arrowUp3.click();
        currentReading += 10;
      }
      if (currentReading/10 % 10 > wantedReading/10 % 10) {
        arrowDown3.click();
        currentReading -= 10;
      }
      if (currentReading % 10 < wantedReading % 10) {
        arrowUp4.click();
        currentReading += 1;
      }
      if (currentReading % 10 > wantedReading % 10) {
        arrowDown4.click();
        currentReading -= 1;
      }
    }

    action.sendKeys(Keys.ENTER).perform();

    Thread.sleep(20000);
    $(By.className("boss")).shouldBe(visible);
    $(By.className("player")).shouldBe(visible);
    double bossX = Double.parseDouble($(By.className("boss")).getCssValue("left").replace("px", ""));
    double playerX = Double.parseDouble($(By.className("player")).getCssValue("left").replace("px", ""));
    while (Math.abs(playerX - bossX) > 80) {
      if (playerX < bossX) {
        action.sendKeys(Keys.ARROW_RIGHT).perform();
      } else {
        action.sendKeys(Keys.ARROW_LEFT).perform();
      }
      bossX = Double.parseDouble($(By.className("boss")).getCssValue("left").replace("px", ""));
      playerX = Double.parseDouble($(By.className("player")).getCssValue("left").replace("px", ""));
    }
    while ($(By.className("boss")).isDisplayed()) {
      action.sendKeys(Keys.SPACE).perform();
    }

    Thread.sleep(20000);
  }
}



