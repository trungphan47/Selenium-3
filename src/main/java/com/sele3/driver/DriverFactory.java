package com.sele3.driver;

import com.sele3.configs.Configuration;
import org.openqa.selenium.WebDriver;

public interface DriverFactory {

    WebDriver create(Configuration configuration);
}
