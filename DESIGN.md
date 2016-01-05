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
We need internet to make http request and download images. In the Android Manifest file we can get this permission:  
```<uses-permission android:name="android.permission.INTERNET"/>```

### Advanced Sketches UI
![Advanced Sketches UI](/doc/imagenamehere.jpg)  

### API implementation
####1) Connection to the search activity
The search activity will have a searchbar the searchwords used in this bar will be provided to the API.  
Example query: ```https://www.rijksmuseum.nl/api/en/collection?q=Q&imgonly=True&key=fakekey&format=json```  

**Input for the query:**
- **/en**: language  
- **/collection**: we will search the collection   
- **?q=Q**: Q is the searchword provided by the user  
- **&imgonly=true**: we only want records of artworks that also have images in their record  
- **&key=fakekey**: fakekey gets replaced by my api key  
- **&format=json**: we will retrieve the results in json  

**Information that I want to show in the results-activity:**
- "principalOrFirstMaker" --> We use the name of the (principal) maker since it is short. Artwork titles would be too long.
- An image/thumbnail of the artwork --> can be found via another query:  ```https://www.rijksmuseum.nl/api/nl/collection/SK-C-5/tiles?format=jsonp&key=fakekey```. This API returns a list of levels. These levels have a number of tiles. The tiles form the complete image. You can choose the right level by using the width and height. They describe the total resolution of the chosen level. You can also select a level by name. Level z0 contains the largest available image and z6 the smallest. The sizes of the z levels differ considarably so I think I would be best off by selecting a level based on width.

**Presenting this information in the UI**  
Using a custom listview. For parsing the JSON data retrieved from the API AsyncTask will be used for multithreading. 
  
####2) Selecting one artwork
When the user taps one artwork the objectNumber of that artwork should be retrieved and a new query should be made: ```https://www.rijksmuseum.nl/api/en/collection/objnum?key=fakekey&format=json```.
- **objnum**: object number (i.e. SK-C-5)

**Information I want to show in the select-activity:**
- The image can best be retrieved the same was as the thumbnail for the previous activity, but this time choosing an image with a higher resolution. 
- "title"
- "principalMakers" : "name"
- "dating" : [] 
- "materials" : [] 
- Also add a button to the Rijksmuseum webpage record of this image for more information. This could be sent from the previous activity ("links" : "web") or found in this activity by adding "links" : "search" to "objectNumber". 

### Image Editing
[Download Image using AsyncTask in Android](http://javatechig.com/android/download-image-using-asynctask-in-android) (multithreading). I can convert to/download as a bitmp by using the [Android BitmapFactory Class](http://developer.android.com/reference/android/graphics/BitmapFactory.html).  
If I download the image from the tile API of the Rijksmuseum then I do need to find a way to correctly display those tiles of the image next to eachother. They do have x, y values that indicate their position relative to eachother, but I do not know yet how to specifically implement this in Android Studio. 

There are several editing options I wish to implement. I however do not know if I have enought time to implement all these, so I wil at least implement the most above option, and will only implement those below it if I have enough time left.  
- Add text
- Add multiple text fields
- Change text size
- Change text color
- Change font
- Crop image
- Add borders to image
To implement the image editing options an Android [Canvas](http://developer.android.com/reference/android/graphics/Canvas.html) will be used. The bitmap image can be used as the basis/background for the canvas.

### Image Sharing
