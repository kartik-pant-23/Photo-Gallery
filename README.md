# Photo-Gallery
* Shows photo gallery similar to as in whatsapp, folders segregated.
* Would work perfect on Scoped storage android 10 and above.
* Is meant for image picking use in flutter, adding instructions provided below.

## Instructions to add in flutter project for image-picking alternative
* Copy paste all 5 files from this project into your flutter android module at app/src/main/java in a package 'ImageHelperFiles'
* Don't forget to copy paste required resource files, drawables and layouts.
* Register activities in manifest as follows.
```xml
<activity
            android:name=".ImageHelperFiles.GalleryImages"
            android:theme="@style/Theme.AppCompat.NoActionBar" />
<activity
            android:name=".ImageHelperFiles.PhotoGallery"
            android:theme="@style/Theme.AppCompat.NoActionBar" />
```
* In your flutter android module at app/src/main/java in MainActivity.java class, paste the following code to register the method call.
```java
private MethodChannel.Result mResult;
    private final int IMAGE_PICKER = 100;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        String CHANNEL = "METHOD-CHANNEL-NAME";
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler((call, result) -> {
                    switch (call.method) {
                        case "pick_image" :
                            mResult = result;
                            pickFromGallery();
                            break;
                        default:
                            result.notImplemented();
                    }
                });
    }

    void pickFromGallery() {
        Intent galleryIntent = new Intent(this, GalleryImages.class);
        startActivityForResult(galleryIntent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICKER) {
            switch(resultCode) {
                case RESULT_OK:
                    ArrayList<String> selectedImages = data.getStringArrayListExtra("selected_images");
                    mResult.success(selectedImages);
                    break;
                case RESULT_CANCELED:
                    selectedImages = new ArrayList<>();
                    mResult.success(selectedImages);
                    break;
                default:
                    mResult.error("Unknown Error", "Unhandled error occurred!", null);
            }
        }
    }
```
* Place this file in lib folder of your flutter project.
```dart
import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ImagePicker {

  final MethodChannel channel;
  LeimoImagePicker({@required this.channel});

  Future<List> chooseImages() async {
    return await channel.invokeMethod("pick_image");
  }

}
```
* Handle onClick function on OK Button in GalleryImages.java file to setup logic for sending back selected images. Suggested way is shown below.
```java
Intent intent = getIntent();
intent.putStringArrayListExtra("selected_images", selectedImages);
setResult(RESULT_OK, intent);
finish();
```
* You can also handle onBackPressed method as follows.
```java
Intent intent = getIntent();
setResult(RESULT_CANCELED, intent);
finish();
```
* To trigger the method for image picking, add the following code in your flutter project.
```dart
ImagePicker imagePicker = ImagePicker(
            channel: const MethodChannel("METHOD-CHANNEL-NAME"));
        var _images = await imagePicker.chooseImages();
```
