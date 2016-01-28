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
- [Rijksmuseum API](http://rijksmuseum.github.io/)
- [Classical Art Memes Facebook Album](https://www.facebook.com/media/set/?set=a.595162167262642.1073741827.595155763929949&type=3) (timeline photos)

### Sources of External Code
All of the resources used are referenced within the code using comments. Below the most important resources are listed:  
- For the implementation of Volley and learning how to use JSON: 
- For the configuration of the GridView that holds the search results: 
- For the implementation of the Facebook Graph Call to get the album: https://developers.facebook.com/docs/graph-api/reference/v2.5/album (official documentation)
- Used to set up the RecyclerView with the CardViews for Facebook: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156 (tutorial)
- For implementing the Adobe SDK image-editor: https://creativesdk.adobe.com/docs/android/#/articles/imageediting/index.html (official guide)
