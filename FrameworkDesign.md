flowchart TB

    %% =====================================================
    %% TEST LAYER
    %% =====================================================
    subgraph TEST["2. TEST LAYER - TestNG"]
        LoginTest["LoginTest.java<br/><br/>
        @BeforeMethod<br/>
        @Test<br/>
        @DataProvider<br/>
        Assertions<br/>
        Test execution"]

        DashboardTest["DashboardTest.java"]
        ShipmentTest["ShipmentTest.java"]
    end

    %% =====================================================
    %% PAGE OBJECT LAYER
    %% =====================================================
    subgraph PAGE["3. PAGE OBJECT LAYER"]
        
        BasePage["BasePage.java<br/><br/>
        click()<br/>
        type()<br/>
        getText()<br/>
        waitForVisible()<br/>
        isDisplayed()"]

        LoginPage["LoginPage.java<br/><br/>
        login()<br/>
        enterUsername()<br/>
        enterPassword()<br/>
        clickLogin()<br/>
        getLoginError()"]

        DashboardPage["DashboardPage.java<br/><br/>
        isDashboardDisplayed()<br/>
        getWelcomeText()<br/>
        clickLogout()<br/>
        openShipmentMenu()"]

        ShipmentPage["ShipmentPage.java<br/><br/>
        searchShipment()<br/>
        selectShipment()<br/>
        getShipmentStatus()"]
    end

    %% =====================================================
    %% UTILITY LAYER
    %% =====================================================
    subgraph UTIL["4. UTILITY / ACTION LAYER"]

        WebUtils["WebUtils.java<br/><br/>
        click()<br/>
        type()<br/>
        clear()<br/>
        getText()<br/>
        isDisplayed()<br/>
        scrollToElement()"]

        WaitUtils["WaitUtils.java<br/><br/>
        waitForVisibility()<br/>
        waitForClickable()<br/>
        waitForText()<br/>
        fluentWait()"]

        JsUtils["JsUtils.java<br/><br/>
        clickByJS()<br/>
        scrollIntoView()<br/>
        executeScript()"]

        ScreenshotUtils["ScreenshotUtils.java<br/><br/>
        takeScreenshot()<br/>
        takeFullPageScreenshot()"]

        DateUtils["DateUtils.java<br/><br/>
        getCurrentDate()<br/>
        addDays()<br/>
        formatDate()"]

        FileUtils["FileUtils / ExcelUtils.java<br/><br/>
        readFile()<br/>
        writeFile()<br/>
        getCellData()"]
    end

    %% =====================================================
    %% INFRASTRUCTURE
    %% =====================================================
    subgraph INFRA["5. INFRASTRUCTURE LAYER"]

        DriverFactory["DriverFactory.java<br/><br/>
        createDriver()<br/>
        createChromeDriver()<br/>
        createFirefoxDriver()"]

        DriverManager["DriverManager.java<br/><br/>
        getDriver()<br/>
        quitDriver()<br/>
        removeDriver()<br/><br/>
        ThreadLocal<WebDriver>"]

        ConfigReader["ConfigReader.java<br/><br/>
        getString()<br/>
        getInt()<br/>
        getBoolean()<br/>
        getList()"]

        PageFactoryManager["PageFactoryManager.java<br/><br/>
        initElements()<br/>
        initialize(Page)"]

        TestListener["TestListener.java<br/><br/>
        beforeTest()<br/>
        afterMethod()<br/>
        onTestFailure()<br/>
        onTestSuccess()"]
    end

    %% =====================================================
    %% SELENIUM
    %% =====================================================
    subgraph SELENIUM["6. SELENIUM WEBDRIVER LAYER"]

        WebDriver["Selenium WebDriver"]

        Chrome["ChromeDriver"]
        Firefox["FirefoxDriver"]
        Edge["EdgeDriver"]
    end

    %% =====================================================
    %% BROWSER / APPLICATION
    %% =====================================================
    subgraph APP["7. BROWSER & APPLICATION"]

        Browser["Web Browser"]

        WebApp["Web Application<br/><br/>
        Login<br/>
        Dashboard<br/>
        Shipment<br/>
        Other Modules"]
    end

    %% =====================================================
    %% TEST DATA / CONFIG / REPORTING
    %% =====================================================
    subgraph EXTERNAL["EXTERNAL INTEGRATIONS"]

        TestData["Test Data<br/><br/>
        Excel<br/>
        CSV<br/>
        JSON"]

        Config["Configuration<br/><br/>
        config.properties<br/>
        Environment Config"]

        Reporting["Reporting<br/><br/>
        Allure<br/>
        ExtentReports"]

        Logging["Logging<br/><br/>
        Log4j2"]

        CICD["CI/CD<br/><br/>
        Jenkins<br/>
        GitLab CI"]

        DBAPI["Database / API<br/><br/>
        JDBC<br/>
        REST Assured"]
    end

    %% =====================================================
    %% MAIN FLOW
    %% =====================================================

    LoginTest --> LoginPage
    DashboardTest --> DashboardPage
    ShipmentTest --> ShipmentPage

    %% Page inheritance
    LoginPage -->|extends| BasePage
    DashboardPage -->|extends| BasePage
    ShipmentPage -->|extends| BasePage

    %% Page to utility
    BasePage --> WebUtils
    BasePage --> WaitUtils

    LoginPage --> WebUtils
    LoginPage --> WaitUtils

    DashboardPage --> WebUtils
    DashboardPage --> WaitUtils

    ShipmentPage --> WebUtils
    ShipmentPage --> WaitUtils

    %% Utility to infrastructure / driver
    WebUtils --> WebDriver
    WaitUtils --> WebDriver
    JsUtils --> WebDriver
    ScreenshotUtils --> WebDriver

    %% Infrastructure
    DriverFactory --> DriverManager
    DriverManager --> WebDriver

    ConfigReader --> DriverFactory
    PageFactoryManager --> LoginPage
    PageFactoryManager --> DashboardPage
    PageFactoryManager --> ShipmentPage

    %% Selenium
    WebDriver --> Chrome
    WebDriver --> Firefox
    WebDriver --> Edge

    Chrome --> Browser
    Firefox --> Browser
    Edge --> Browser

    Browser --> WebApp

    %% Test listener
    TestListener -.-> LoginTest
    TestListener -.-> DashboardTest
    TestListener -.-> ShipmentTest

    %% External integrations
    TestData -.-> LoginTest
    TestData -.-> ShipmentTest

    Config -.-> ConfigReader

    TestListener --> Reporting
    TestListener --> ScreenshotUtils
    TestListener --> Logging

    CICD --> LoginTest
    DBAPI -.-> LoginTest
    DBAPI -.-> ShipmentTest


    %% =====================================================
    %% STYLING
    %% =====================================================

    classDef test fill:#E8F5E9,stroke:#2E7D32,stroke-width:2px
    classDef page fill:#E3F2FD,stroke:#1565C0,stroke-width:2px
    classDef util fill:#FFF3E0,stroke:#EF6C00,stroke-width:2px
    classDef infra fill:#F3E5F5,stroke:#6A1B9A,stroke-width:2px
    classDef selenium fill:#E0F7FA,stroke:#00838F,stroke-width:2px
    classDef app fill:#ECEFF1,stroke:#37474F,stroke-width:2px
    classDef external fill:#FFF8E1,stroke:#F9A825,stroke-width:2px

    class LoginTest,DashboardTest,ShipmentTest test
    class BasePage,LoginPage,DashboardPage,ShipmentPage page
    class WebUtils,WaitUtils,JsUtils,ScreenshotUtils,DateUtils,FileUtils util
    class DriverFactory,DriverManager,ConfigReader,PageFactoryManager,TestListener infra
    class WebDriver,Chrome,Firefox,Edge selenium
    class Browser,WebApp app
    class TestData,Config,Reporting,Logging,CICD,DBAPI external