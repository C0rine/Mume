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
####1) Connection to the search activity####
The search activity will have a searchbar the searchwords used in this bar will be provided to the API.  
Example query: https://www.rijksmuseum.nl/api/en/collection?q=Q&imgonly=True&key=fakekey&format=json  
**Input for the query**  
- **/en**: language  
- **/collection**: we will search the collection   
- **?q=Q**: Q is the searchword provided by the user  
- **&imgonly=true**: we only want records of artworks that also have images in their record  
- **&key=fakekey**: fakekey gets replaced by my api key  
- **&format=json**: we will retrieve the results in json  

**Information that I want to show in the results-activity:**
- "principalOrFirstMaker" --> We use the name of the painter since it is short. Artwork titles would be too long. 
  
####2) Selecting one artwork####
When the user taps one artwork the objectNumber of that artwork should be retrieved and a new query should be made:  
https://www.rijksmuseum.nl/api/en/collection/objnum?key=fakekey&format=json  
**objnum**: object number (i.e. SK-C-5)

**Information I want to show in the select-activity:**
- "title"
- "principalMakers" : "name"
- "dating" : [] 
- "materials" : [] 
- Also add a button to the Rijksmuseum webpage record of this image for more information. This could be sent from the previous activity ("links" : "web") or found in this activity by adding "links" : "search" to "objectNumber". 

####3) Editing the artwork####
The image should be saved to the Android device in bitmap format for editing. Downloading: http://javatechig.com/android/download-image-using-asynctask-in-android 
