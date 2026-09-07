package com.sele3.driver;

import com.sele3.configs.Configuration;
import org.openqa.selenium.WebDriver;

public interface DriverFactory {

    default String getPlatform() {
        return "";
    }

    WebDriver create(Configuration configuration);
}
