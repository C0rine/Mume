# Mume 
### (Museum Memes)  
Corine Jacobs  
Studentnr.: 10001326  
Corine_J@MSN.com or MuseumMemes@outlook.com
  
Platform: Android  
Minimum API level: 19  
Target API level: 22  

Copyright Statement: [Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License](https://github.com/C0rine/Mume/blob/master/LICENSE.md) 
  
This app was tested on a Samsung Galaxy Core Prime (SM-G361F) and HTC ONE mini.

### Summary
Mume (MUseum MEmes) is an Android App that uses images from the databases of museums (for this project only the database of the Rijksmuseum is used) to create memes. Based on the popular Facebook-page: https://www.facebook.com/classicalartmemes  

*User gap: to facilitate making and sharing Classical Art Memes.*  
*Underlying idea: introduce art history more into popular-/webculture.*  

Features:
- Search through Rijksmuseum artworks and retrieve individual images of the artworks.
- Edit these images by adding text, drawing on them and/or cropping them.
- Share these images online or with friends through apps already installed on the Android device.
- View the latest memes of the [Classical Art Memes](https://www.facebook.com/classicalartmemes) Facebook page.

### Screenshots 
Screenshot descriptions (left to right, top to bottom): search-activity, results-activity, selected-activty, image-editor (Adobe SDK), Share-Intent.
![Preliminary sketches](/doc/allscreenshots28jan2016.jpg)  
For large resolution images of these screenshots please see the doc folder (images from 28 jan 2016).

### Datasources  
- [Rijksmuseum API](http://rijksmuseum.github.io/): click [here](https://www.rijksmuseum.nl/en/api/terms-and-conditions-of-use) for license/copyright information.
- [Classical Art Memes Facebook Album](https://www.facebook.com/media/set/?set=a.595162167262642.1073741827.595155763929949&type=3) (timeline photos): click [here](https://developers.facebook.com/policy/) for license/copyright information.

###SDKs
- [Adobe Creative Suite (image editor)](https://creativesdk.adobe.com/): click [here](http://wwwimages.adobe.com/content/dam/Adobe/en/legal/servicetou/Creative_SDK-en_US.pdf) for license/copyright information.

### Sources of External Code
All of the resources used are referenced within the code using comments. Below the most important resources are listed: 
- For setting up the Volley Singleton: http://developer.android.com/training/volley/simple.html (official developer guide)
- For the implementation of Volley and learning how to use JSON: https://www.youtube.com/watch?v=hJRjCurwXVw&index=30&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD (YouTube tutorials)
- For the configuration of the GridView that holds the search results: http://developer.android.com/guide/topics/ui/layout/gridview.html (official developer guide)
- For the implementation of the Facebook Graph Call to get the album: https://developers.facebook.com/docs/graph-api/reference/v2.5/album (official documentation)
- Used to set up the RecyclerView with the CardViews for Facebook: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156 (tutorial)
- For the implementation of the header and footer in the RecyclerView: http://stackoverflow.com/questions/26530685 (stackoverflow answers)
- For setting up the Adobe SDK: https://creativesdk.adobe.com/docs/android/#/articles/gettingstarted/index.html (official guide)
- For implementing the Adobe SDK image-editor: https://creativesdk.adobe.com/docs/android/#/articles/imageediting/index.html (official guide)
- For the implementation of the custom ImageView (SquareImageView): http://stackoverflow.com/questions/16506275 (stackoverflow answer)  

As far as I could find these sources contained no license, nor copyright.
