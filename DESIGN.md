# Design Document
##### Activities  
(see sketches below)
- Search
- Results
- Selected
- Edit 

### Advanced Sketches UI
![Advanced Sketches UI](/doc/advancedsketches_activities.jpg)    
![Advanced Sketches UI](/doc/advancedsketches_other.jpg)

### Use Case
Based on tutorial by [Derek Banas](https://www.youtube.com/watch?v=OkC7HKtiZC0).

##### Description
The user types searchword(s) in the searchbar. These words are used to make a query to the Rijksmuseum database. The Rijksmuseum API sends back a list of results. The results as displayed to the user as images with artistname. The user selects one image. A query is made to the Rijksmuseum database to get more information on the specific artwork. The result is displayed with a bigger image and more metadata. The user presses a button that he/she wants to meme the image. The image gets downloaded to the device. The image gets opened to be edited. The user edits the image. The user presses a button to share the image. The user picks an app to share the image with. The image gets send to the appropriate app. 

##### Actors
- User
- Rijksmuseum API

##### Preconditions
- Internet connection
- Rijksmuseum website needs to be available

##### Goals
Succesful Conclusions:  
- Retrieve data from Rijksmuseum
- Send an edited image to another app  
  
Failed Conclusions:  
Cases in which the system will fail or will stop processing before going through the entire use case.  
- No connection could be made to the internet
- No data is retrieved from the Rijksmuseum API because it is offline
- There were no searchresults for the query that was made
- The downloading of the image failed

##### Requirements  
*Shall (MVP)*  
- Searchbar to search Rijksmuseum collection
- Ability to view searchresults 
- Ability to select one of the searchresults and view more information about it 
- Functionality to add text to the image of that searchresult
- Clear searchresults, image to edit, etc.. once the user leaves/closes these activities
- Share image through other apps on the Android device

*Should (possible and preferable extras, but not necessary)*
- Help dialogbox for each activity in case the user does not understand how to use the app
- Filter options for searchresults
- Change size, color, font of the text to be added to the image
- Crop image to be edited
- Add borders to the image to be edited

### Class Diagram
![Class Diagram](/doc/classdiagram.jpg) 
  
---

# Implementation Details
### API implementation
#### 1) Connection to the search activity
The search activity will have a searchbar. The searchwords used in this bar will be provided to the API.  
Example query: `https://www.rijksmuseum.nl/api/en/collection?q=Q&imgonly=True&key=fakekey&format=json`  

**Input for the query:**
- `/en`: the app will be in English, so we want to retrieve the data in English
- `/collection`: we will search the collection   
- `?q=Q`: Q is the searchword provided by the user  
- `&imgonly=true`: we only want records of artworks that also have images in their record  
- `&key=fakekey`: fakekey gets replaced by my api key  
- `&format=json`: we will retrieve the results in json  

**Information that I want to show in the results-activity:**
- "principalOrFirstMaker" --> We use the name of the (principal) maker since it is short. Artwork titles would be too long.
- An image/thumbnail of the artwork --> can be found via another query:  `https://www.rijksmuseum.nl/api/nl/collection/SK-C-5/tiles?format=jsonp&key=fakekey`. This API returns a list of levels. These levels have a number of tiles. The tiles form the complete image. You can choose the right level by using the width and height. They describe the total resolution of the chosen level. You can also select a level by name. Level z0 contains the largest available image and z6 the smallest. The sizes of the z-levels differ considarably so I think I would be best off by selecting a level based on width in pixels.

**Presenting this information in the UI**  
Using a custom listview. For parsing the JSON data retrieved from the API AsyncTask will be used for multithreading. The searchresults are broken down in pages by the API parameters `p` (the result page) and `ps` (number of results per page) can be used to fetch more results. 
  
####2) Selecting one artwork
When the user taps one artwork the objectNumber of that artwork should be retrieved and a new query should be made: `https://www.rijksmuseum.nl/api/en/collection/objnum?key=fakekey&format=json`.
- `objnum`: object number (i.e. SK-C-5)

**Information I want to show in the select-activity:**
- The image can best be retrieved the same was as the thumbnail for the previous activity, but this time choosing an image with a higher resolution. 
- "title"
- "principalMakers" : "name"
- "dating" : [] 
- "materials" : [] 
- Also add a button to the Rijksmuseum webpage record of this image for more information. This could be sent from the previous activity ("links" : "web") or found in this activity by adding "links" : "search" to "objectNumber". 

### Image Editing
[Download Image using AsyncTask in Android](http://javatechig.com/android/download-image-using-asynctask-in-android) (multithreading). I can convert to/download as a bitmp by using the [Android BitmapFactory Class](http://developer.android.com/reference/android/graphics/BitmapFactory.html). If I download the image from the tile-API of the Rijksmuseum then I do need to find a way to correctly display those tiles of the image next to eachother. They do have x, y values that indicate their position, but I do not know yet how to specifically implement this in Android Studio. 

To implement the image editing options an Android [Canvas](http://developer.android.com/reference/android/graphics/Canvas.html) will be used. The bitmap image can be used as the basis/background for the canvas.

### Image Sharing
To share the canvas it has to be converted back to an image again. For this I can use a [OutputStream](http://stackoverflow.com/questions/13533471/how-to-save-view-from-canvas-to-png-file) and compress to JPG or PNG.
After this is done the image can be shared through a "Share Intent" by [sending binary content](http://developer.android.com/training/sharing/send.html). This will show a pop-up to show all the apps on the Android Device that can do something (sharing/editing) with the image (i.e. WhatsApp, Facebook, Twitter, Google Drive, etc..). This way the actual sharing of the image is passed on to other apps. 
