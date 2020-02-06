package com.epam.jdi.mobile.driver.get;

import com.epam.jdi.mobile.settings.WebSettings;
import io.github.bonigarcia.wdm.WebDriverManager;

import static com.epam.jdi.mobile.common.Exceptions.exception;
import static com.epam.jdi.mobile.driver.get.DriverData.PRELATEST_VERSION;
import static com.epam.jdi.mobile.driver.get.DriverData.getOs;

/**
 * Created by Roman_Iovlev on 11/28/2017.
 */
public class DownloadDriverManager {
    public static boolean DOWNLOAD_DRIVER = true;

    private static boolean hasVersion(String version) {
        char c = version.charAt(0);
        return (c >= '0' && c <= '9');
    }
    public static WebDriverManager wdm;

    public static void downloadDriver(DriverTypes driverType,
          Platform platform, String version) {
        try {
            String driverName = driverType.toString();
            switch (driverType) {
                case CHROME:
                    wdm = WebDriverManager.chromedriver(); break;
                case FIREFOX:
                    wdm = WebDriverManager.firefoxdriver(); break;
                case IE:
                    wdm = WebDriverManager.iedriver(); break;
                case EDGE:
                    wdm = WebDriverManager.edgedriver(); break;
                case OPERA:
                    wdm = WebDriverManager.operadriver(); break;
                default:
                    throw exception("Unknown driver: " + driverType);
            }
            if (getOs() == OsTypes.WIN) {
                switch (platform) {
                    case X32:
                        wdm = wdm.arch32();
                        break;
                    case X64:
                        wdm = wdm.arch64();
                        break;
                }
                driverName += " " + platform;
            }
            if (hasVersion(version)) {
                wdm = wdm.version(version);
                driverName += " " + version;
            }
            if (version.equalsIgnoreCase(PRELATEST_VERSION)) {
                wdm.setup();
                wdm.version(DriverInfo.getBelowVersion());
            }
            wdm.setup();
            WebSettings.logger.info("Download driver: '" +  driverName + "' successfully");
        } catch (Exception ex) {
            throw exception(ex, "Can't download latest driver for " + driverType);
        }
    }
}