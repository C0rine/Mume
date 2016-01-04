# Mume  
Corine Jacobs  
10001326  
Corine_J@MSN.com  
  
Mume (MUseum MEmes) will be an Android App (created with Android Studio) which uses images from the databases of museums (for this project only the database of the Rijksmuseum will be used) to create memes. 

Based on the popular Facebook-page: https://www.facebook.com/classicalartmemes  

*User gap: to facilitate making and sharing Classical Art Memes.*  
*Underlying idea: introduce art history more into popular-/webculture.*  

### Features
- Search through (Rijks)museum artworks and retrieve individual images of the artworks
- Add text to these images
- Share these images online or with friends through apps already installed on the Android device 

### Preliminary Sketches  
![Preliminary sketches](/doc/preliminarysketches.jpg)  

### Datasources  
For this project I have chosen the database of the Rijksmuseum since they provide a clear API with plenty documentation and examples. Moreover: "All data and all images made available through the API are either in the public domain or are subject to a CC0 license. The data and images are royalty-free and may be copied, distributed, modified and used without the permission of the Rijksmuseum.". More information on the API: https://www.rijksmuseum.nl/en/api.

### Decomposition of the app  
*1) Searching the database using the Rijksmuseum API*  
I should be able to figure out how to implement this with the [documentation](http://rijksmuseum.github.io/) and [demos](http://rijksmuseum.github.io/demos/) provided by the Rijksmuseum.  
The API response will be in JSON, I have found a [tutorial](http://mobilesiri.com/json-parsing-in-android-using-android-studio/) that explains how to parse this in Android Studio.

*2) Selecting one image and downloading it to the Android device*  
The UI should facilitate scrolling through the images retrieved by a searchquery. Then the user should be able to select one image to view it full screen and see more information. When the user wants to edit the image, the images should (probably) be downloaded to the android device. 

*3) Editing the selected image by adding text*  
I plan to code this functionality myself. I have found [this question](http://stackoverflow.com/questions/11318205/how-to-write-text-on-an-image-in-java-android) on Stack Overflow that gives some explanation on how this could be implemented.

*4) A "Share Activity" that facilitates sharing the image to the web and other apps already installed on the Android device*  
For the sharing of the images I plan to use an option already implemented in Android: the "Share Intent". Sending an image to this intent will show [all the apps](http://i0.wp.com/www.devcfgc.com/wp-content/uploads/2014/10/intent-chooser.jpg) already installed on the Android device that facilitate uploading/sharing images. More on this [Sending of Binary Content](http://developer.android.com/training/sharing/send.html).

### Potential Problems  
I'm not sure wether this app will be diffucult or very easy to make.  
  
1) In case it is difficult:  
- Use the [Aviary SDK](https://developers.aviary.com/) for the image editing, instead of coding this myself.

2) In case it is easy:
- Add databases of other museums.
- Implement the [Adlib API](http://api.adlibsoft.com/site/). Adlib is a system used by most big museums for collection management. 
- Add an activity to view the images of the Classical Art Memes Facebookpage (that stays up to date by using an [API](https://developers.facebook.com/docs/graph-api/reference/v2.5/album) to get the images from the photogallery of the Facebookpage).
- Adding the possibility to edit pictures the user has made him-/herself.

### Review of similar application
##### [Meme Generator (Free)](https://play.google.com/store/apps/details?id=com.zombodroid.MemeGenerator&hl=en)
An application that lets you create memes. 

