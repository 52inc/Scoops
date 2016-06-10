# Scoops

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.52inc/scoops/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.52inc/scoops)

Android library for managing and applying multiple defined `R.style.Theme....` themes at runtime for dyanically changing the look and feel of your application

--- 
## Installation

Add this line to your gradle/dependencies group:

```groovy
compile 'com.52inc:scoops:0.1.0'
```

Then you will need to initialize the singleton in your `Application` subclass like this:

```java
Scoop.waffleCone()
        .addFlavor("Default", R.style.Theme_Scoop, true)
        .addFlavor("Light", R.style.Theme_Scoop_Light)
        .addDayNightFlavor("DayNight", R.style.Theme_Scoop_DayNight)
        .addFlavor("Alternate 1", R.style.Theme_Scoop_Alt1)
        .addFlavor("Alternate 2", R.style.Theme_Scoop_Alt2, R.style.Theme_Scoop_Al2_Dialog)
        .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
        .initialize();
```

## Use 

Basic usage is to call the `.apply(...)` method in your activities before you call `setContentView(R.layout.some_layout)` like this:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Apply Scoop to the activity
    Scoop.getInstance().apply(this);

    // Set the activity content
    setContentView(R.layout.activity_main);

    ...
}
```

You can also use `.applyDialog(...)` to apply the specified dialog theme resource to an activity (to make an activity appear as a dialog with the correct theme)

### Advanced Use

There are a few custom attributes you can use to make sure your application has the correct styling when switching themes. These are:

```xml
<attr name="toolbarTheme" format="reference" />
<attr name="toolbarPopupTheme" format="reference" />
<attr name="toolbarItemTint" format="reference|color" />
```

Then in your layouts, apply the toolbar themes like so:

```xml
<android.support.v7.widget.Toolbar
        ...
        
        android:theme="?attr/toolbarTheme"
        app:popupTheme="?attr/toolbarPopupTheme"
        
        />
```

Then define the attribute in your themes like this:


```xml
<style name="Theme.Scoop" parent="Theme.AppCompat.NoActionBar">
	...
    <item name="toolbarTheme">@style/ThemeOverlay.AppCompat.Dark.ActionBar</item>
    <item name="toolbarPopupTheme">@style/ThemeOverlay.AppCompat.Light</item>
    <item name="toolbarItemTint">#fff</item>
</style>
```

You can also apply the `toolbarItemTint` color to all the icons in the toolbar by calling `.apply(Context, Menu)` 

	
### Settings Menu

This library provides a built in theme chooser settings screen to use called `ScoopSettingsActivity` that you can use by utilizing one of it's static Intent factories:

```java
ScoopSettingsActivity.createIntent(Context);
ScoopSettingsActivity.createIntent(Context, R.string.some_title_to_use);
ScoopSettingsActivity.createIntent(Context, "Some title to use");
```

## License

	Copyright (c) 2016 52inc

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
	either express or implied. See the License for the specific
	language governing permissions and limitations under the License.

