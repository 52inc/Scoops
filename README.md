<img src="art/web_hi_res_512.png" width="190" align="right" hspace="20" />

Scoops
======

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.52inc/scoops/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.52inc/scoops) [![Build Status](https://travis-ci.org/52inc/Scoops.svg?branch=master)](https://travis-ci.org/52inc/Scoops)

Android library for managing and applying multiple defined `R.style.Theme....` themes at runtime for dyanically changing the look and feel of your application

---
## Demo

![demo gif](http://i.imgur.com/5SqjbhL.gif) ![demo gif 2](http://i.imgur.com/WPTjYUP.gif)

--- 
## Installation

Add this line to your gradle/dependencies group:

```groovy
compile 'com.52inc:scoops:1.0.0'
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

# Dynamic color property changing

This is the ability to have a view or attribute update it's color (background, src, text, etc) whenever the user/developer chnages the color for a defined property, or `Topping`. Please refer to [Sample App](https://github.com/52inc/Scoops/tree/feature-dynamic-color-attr/app/src/main/java/com/ftinc/themeenginetest) for actual code references.

## Installation

```groovy
buildscript {
    repositories {
       jcenter()
    }
    dependencies {
	 	...
    	classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}
```

```groovy
compile 'com.52inc:scoops:1.0.0'
apt 'com.52inc:scoops-compiler:1.0.0'
```

## Manual Implementation  
_This does not require the compiler dependency to use._

```java
Toolbar mAppBar;

@Override
public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.some_layout);
	
	Scoop.getInstance().bind(this, Toppings.PRIMARY, mAppBar)
					 .bindStatusBar(this, Toppings.PRIMARY_DARK);
}

@Override
public void onDestroy(){
	super.onDestroy();
	Scoop.getInstance().unbind(this);
}	

void onSomeEvent(){
	Scoop.getInstance().update(Toppings.PRIMARY, someColorInt) 
 					 .update(Toppings.PRIMARY_DARK, someDarkColorInt);
}

```

## Annotated Implementation
There are two annotations to use to binding views and the like to color properties that can be dynamically updated (i.e. palette, etc) which are `@BindTopping()` and `@BindToppingStatus()`.

### `@BindTopping()`
This annotation is for binding View's to certain color properties, aka `Topping`, so that when you later update that color property say after running Palette on an image, or for whatever reason, the view you binded will be automatically updated. The topping Id is the value required for the annotation but you can optionally define a `ColorAdapter` (Used to determine how the color change is applied to your View) you want to use and an `Interpolator` to customize the color change animation.

### `@BindToppingStatus()`
This annotation is used to bind an Activities status bar color to a color property so you can dynamically change the color the window's status bar. Like `@BindTopping()` you can also define an `Interpolator` to use in the animation, however a `ColorAdapter` is not an option here since there is only one way to apply color to a status bar.

### Example

```java
@BindToppingStatus(Toppings.PRIMARY_DARK)
public class MainActivity extends AppCompatActivity {

    @BindTopping(Toppings.PRIMARY)
    @BindView(R.id.appbar)
    Toolbar mAppBar;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = FABColorAdapter.class,
            interpolator = AccelerateInterpolator.class
    )
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind ButterKnife
        ButterKnife.bind(this);

        // Bind Scoops
        Scoop.getInstance().bind(this);
		
		...
    }

    @Override
    protected void onDestroy() {
        Scoop.getInstance().unbind(this);
        super.onDestroy();
    }
}
```

## Snapshot

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    }
}
```

```groovy
compile 'com.52inc:scoops:1.0.1-SNAPSHOT`
apt `com.52inc:scoops-compiler:1.0.1-SNAPSHOT`
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

