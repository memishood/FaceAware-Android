FaceAware for Android Studio
========
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![](https://jitpack.io/v/memishood/FaceAware-Android.svg)](https://jitpack.io/#memishood/FaceAware-Android)
<br>
FaceAware is zoom library to face for Android.<br>

<img src="https://github.com/memishood/FaceAware-Android/blob/master/art/avatarExample.png" width=50%>

Let me show how to use this library:

## Setup
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```
dependencies {
    def faceAwareVersion = "3.0.0"
    implementation "com.github.memishood:FaceAware-Android:$faceAwareVersion"
}
```

## XML

```xml
<tr.com.emrememis.library.FaceAware
    android:id="@+id/faceAware"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@your_image"/>
```
## Kotlin

```kotlin  
        faceAware.setImageResource(R.drawable.test)
        //or
        faceAware.setImageDrawable(ActivityCompat.getDrawable(this,R.drawable.test))
        //or
        faceAware.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.test))
        //or
        Glide.with(this).load(R.drawable.test).into(faceAware)
```
-------------------
## For better understanding
<img src="https://github.com/memishood/FaceAware-Android/blob/master/art/largeExample.jpg" width=50%>
