package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import guru.qa.Lang;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selectors.byText;


public class LamodaWebTest {

    @ValueSource(strings = {"Брюки", "Куртка"})
    @ParameterizedTest(name = "Результаты поиска предлагают варианты для разных полов по запросу {0}")
    void commonSearchTest(String testData) {
        open("https://www.lamoda.ru/women-home/");
        $("._3jotUx9G5izzdWD5DIoPVO").click();
        $("._3jotUx9G5izzdWD5DIoPVO").setValue(testData).pressEnter();
        $$(".ui-catalog-search-gender-selector a").shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    @CsvSource(value = {
            "Бесконтактная доставка с примеркой,  Воспользуйтесь бесконтактной доставкой ",
            "Платите когда хотите,  Платите",
    })
    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" при выборе пункта меню \"{0}\"")
    void commonComplexSearchTest(String testData, String expectedResult) {
        open("https://www.lamoda.ru/women-home/");
        $(byText(testData)).click();
        $(".t396").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> dataProviderForLamodaGenderTest() {
        return Stream.of(
                Arguments.of(Gender.Женщинам, List.of("Идеи", "Новинки", "Одежда", "Обувь", "Аксессуары", "Бренды", "Premium", "Спорт", "Красота", "Дом", "Sale%")),
                //Arguments.of(Gender.Женщинам, List.of("Premium  бренды · новинки · sale" , "Идеи", "Новинки", "Одежда", "Обувь", "Аксессуары", "Бренды", "Спорт", "Красота", "Дом", "Sale%")),
                Arguments.of(Gender.Детям, List.of("Новинки", "Девочкам", "Мальчикам", "Малышам", "Premium", "Спорт", "Игрушки", "Дом", "Уход", "Школа", "Sale%"))
        );
    }
    @MethodSource("dataProviderForLamodaGenderTest")
    @ParameterizedTest(name = "Для пункта {0} отображаются кнопки {1}")
    void lamodaGenderMenuTest(Gender gender, List<String> expectedButtons) {
        open("https://www.lamoda.ru/women-home/");
        $$(".wfAgcjOGxIX72sHUFSq9_ a").find(text(gender.name())).click();
        $$("._23aOc1Xo-IRHPf-9UU8tln a")
                .filter(visible)
                .shouldHave(CollectionCondition.texts(expectedButtons));
    }

    @EnumSource(Gender.class)
    @ParameterizedTest
    void lamodaGenderMenuEnumTest(Gender gender) {
        open("https://www.lamoda.ru/women-home/");
        $$(".wfAgcjOGxIX72sHUFSq9_ a").find(text(gender.name())).click();
        $(".wCjUeog4KtWw64IplV1e6").shouldBe(visible);
    }
}
