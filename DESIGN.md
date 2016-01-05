# Design Document

### Basics
**Activities**  
(see sketches below)  
- Search
- Results
- Select
- Edit 
- Share
  
**Permissions**  
We need internet to make connection to the internet, we need to get permission for this in the Android Manifest file:
```<uses-permission android:name="android.permission.INTERNET"/>```

### Advanced Sketches UI

### Classes and Public Methods

### API implementation
**1) Connection to the search activity**
The search activity will have a searchbar the searchwords used in this bar will be provided to the API.

Example query: https://www.rijksmuseum.nl/api/en/collection?q=Q&imgonly=True&key=fakekey&format=json

Input for the query:  
**/en**: language  
**/collection**: we will search the collection   
**?q=Q**: Q is the searchword provided by the user  
**&imgonly=true**: we only want records of artworks that also have images in their record  
**&key=fakekey**: fakekey gets replaced by my api key  
**&format=json**: we will retrieve the results in json  
  
**2) Selecting one artwork** 
When the user taps one artwork the objectNumber of that artwork should be retrieved and a new query should be made:  
https://www.rijksmuseum.nl/api/en/collection/objnum?key=fakekey&format=json  
**objnum**: object number (i.e. SK-C-5)

**3) Editing the artwork**
The image should be saved to the Android device in bitmap format.
  
*1) Searching the database using the Rijksmuseum API*  
I should be able to figure out how to implement this with the [documentation](http://rijksmuseum.github.io/) and [demos](http://rijksmuseum.github.io/demos/) provided by the Rijksmuseum.  
The API response will be in JSON, I have found a [tutorial](http://mobilesiri.com/json-parsing-in-android-using-android-studio/) that explains how to parse this in Android Studio.

*2) Selecting one image and downloading it to the Android device*  
The UI should facilitate scrolling through the images retrieved by a searchquery. Then the user should be able to select one image to view it full screen and see more information. When the user wants to edit the image, the images should (probably) be downloaded to the android device. 

*3) Editing the selected image by adding text*  
I plan to code this functionality myself. I have found [this question](http://stackoverflow.com/questions/11318205/how-to-write-text-on-an-image-in-java-android) on Stack Overflow that gives some explanation on how this could be implemented.

*4) A "Share Activity" that facilitates sharing the image to the web and other apps already installed on the Android device*  
For the sharing of the images I plan to use an option already implemented in Android: the "Share Intent". Sending an image to this intent will show [all the apps](http://i0.wp.com/www.devcfgc.com/wp-content/uploads/2014/10/intent-chooser.jpg) already installed on the Android device that facilitate uploading/sharing images. More on this [Sending of Binary Content](http://developer.android.com/training/sharing/send.html).
