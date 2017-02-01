package br.com.marcosoft.apropriator.po;

import java.util.List;

import org.marcosoft.lib.Condition;
import org.marcosoft.lib.WaitWindow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import br.com.marcosoft.apropriator.selenium.SeleniumSupport;

public class FindElement {
    private WebElement element;
    private final By by;
    private final ElementCondition elementCondition;

    private FindElement(final By by, ElementCondition elementCondition) {
        this.by = by;
        this.elementCondition = elementCondition;
    }

    public static WebElement waitForElement(final By by, ElementCondition elementCondition) {
        return new FindElement(by, elementCondition)
            .getElement();
    }

    private WebElement getElement() {
        final Condition condition = new Condition() {
            public boolean satisfied() {
                element = findElement(by, elementCondition);
                return element != null;
            }
        };
        WaitWindow.waitForCondition(condition, "Esperando Elemento " + by.toString());
        return this.element;
    }

    public static WebElement findElement(By by, ElementCondition elementCondition) {
        final List<WebElement> elements = SeleniumSupport.getWebDriver().findElements(by);
        for (final WebElement webElement : elements) {
            if (elementCondition.isTrue(webElement)) {
                return webElement;
            }
        }
        return null;
    }


    public static interface ElementCondition {
        boolean isTrue(WebElement element);
    }

    public static ElementCondition isDisplayed() {
        return new ElementCondition() {
            public boolean isTrue(WebElement element) {
                return element.isDisplayed();
            }
        };
    }

    public static ElementCondition isEnabled(final String attrituteName, final String attributeValue) {
        return new ElementCondition() {
            public boolean isTrue(WebElement element) {
                return element.isEnabled() && attributeValue.equals(element.getAttribute(attrituteName));
            }
        };
    }

    public static ElementCondition isDisabled(final String attrituteName, final String attributeValue) {
        return new ElementCondition() {
            public boolean isTrue(WebElement element) {
                return !element.isEnabled()
                    && attributeValue.equals(element.getAttribute(attrituteName));
            }
        };
    }

    public static ElementCondition isEnabled() {
        return new ElementCondition() {
            public boolean isTrue(WebElement element) {
                return element.isDisplayed() && element.isEnabled();
            }
        };
    }
}
