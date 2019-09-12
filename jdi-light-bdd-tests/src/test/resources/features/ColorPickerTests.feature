@colorPicker
Feature: ColorPicker

  Scenario: Color picker get color test
    Given I open "Html5 Page"
    And "Color Picker" was set to "#ffd7a6" color
    Then "Color Picker" color equals to "#ffd7a6"

  Scenario: Color picker set color test
    Given I open "Html5 Page"
    When I set "Color Picker" to "#ffd7a6" color
    Then "Color Picker" color equals to "#ffd7a6"

  Scenario: Color picker label assertion test
    Given I open "Html5 Page"
    Then "Color Picker" label text equals to "Select a color"
    Then "Color Picker" label text contains "color"
    Then "Color Picker" label text match to "\w{6} a color"

  Scenario: Color picker assertThat validation test
    Given I open "Html5 Page"
    And "Color Picker" was set to "#ffd7a6" color
    Then "Color Picker" color is "#ffd7a6"

  Scenario: baseValidation
    Given I open "Html5 Page"
    Then "Color Picker" is basically valid