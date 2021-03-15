import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {
    private Faker faker;

    public String gatDate(int shift) {
        LocalDate date = LocalDate.now();
        LocalDate plusDate = date.plusDays(shift);

        String dateString = plusDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return dateString;
    }

    @BeforeEach
    void setUpAll(){
        faker = new Faker(new Locale("ru"));

    }

    @Test
    void correctTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue(faker.address().cityName());
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue(faker.name().fullName());
        form.$("[data-test-id='phone'] .input__control").setValue(faker.phoneNumber().phoneNumber());
        form.$(".checkbox__box").click();
        form.$(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + correctDate));
    }

    @Test
    void RescheduleMeetingTest(){

        String correctDate = gatDate(4);
        String RescheduleDate = gatDate(6);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue(faker.address().cityName());
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue(faker.name().fullName());
        form.$("[data-test-id='phone'] .input__control").setValue(faker.phoneNumber().phoneNumber());
        form.$(".checkbox__box").click();
        form.$(withText("Запланировать")).click();

        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(RescheduleDate);
        form.$(withText("Запланировать")).click();
        form.$(withText("Перепланировать")).click();

        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + RescheduleDate));
    }

    @Test
    void incorrectCityTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue(faker.address().state());
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue(faker.name().fullName());
        form.$("[data-test-id='phone'] .input__control").setValue(faker.phoneNumber().phoneNumber());
        form.$(".checkbox__box").click();
        form.$(withText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void incorrectDateTest(){

        String incorrectDate = gatDate(0);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue(faker.address().cityName());
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(incorrectDate);
        form.$("[data-test-id='name'] .input__control").setValue(faker.name().fullName());
        form.$("[data-test-id='phone'] .input__control").setValue(faker.phoneNumber().phoneNumber());
        form.$(".checkbox__box").click();
        form.$(withText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void incorrectNameTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue(faker.address().cityName());
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue(faker.name().username());
        form.$("[data-test-id='phone'] .input__control").setValue(faker.phoneNumber().phoneNumber());
        form.$(".checkbox__box").click();
        form.$(withText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void NotAgreementTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue(faker.address().cityName());
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue(faker.name().fullName());
        form.$("[data-test-id='phone'] .input__control").setValue(faker.phoneNumber().phoneNumber());
        form.$(withText("Запланировать")).click();
        $("[class='checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid'").shouldBe(visible);
    }
}
