package com.sele3.configs;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;

import java.time.Duration;

@Getter
@Setter
public class Configuration {

    private String platform;
    private boolean headless;
    private String browserSize;
    private boolean startMaximized;
    private PageLoadStrategy pageLoadStrategy;
    private boolean remote;
    private String remoteUrl;
    private MutableCapabilities capabilities;
    private String baseUrl;
    private Duration timeout;
    private Duration pollingInterval;
    private boolean clickViaJs;

}
