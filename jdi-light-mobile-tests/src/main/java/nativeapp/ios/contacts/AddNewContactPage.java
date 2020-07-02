package nativeapp.ios.contacts;

import com.epam.jdi.light.mobile.elements.base.MobileAppUIElement;
import com.epam.jdi.light.mobile.elements.common.ios.app.SystemButton;
import com.epam.jdi.light.mobile.elements.pageobjects.annotations.MobileFindBy;

public class AddNewContactPage {
    @MobileFindBy(accessibilityId = "Cancel")
    public static SystemButton cancelButton;
    @MobileFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"New Contact\"]")
    public static MobileAppUIElement newContactNavBar;
}