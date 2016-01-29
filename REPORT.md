#Final Report  
Corine Jacobs  
10001326

###Short Description
Mume (MUseum MEmes) is an Android App that uses images from the databases of museums (for this project only the database of the Rijksmuseum is used) to create memes. Based on the popular Facebook-page: https://www.facebook.com/classicalartmemes  

*User gap: to facilitate making and sharing Classical Art Memes.*  
*Underlying idea: introduce art history more into popular-/webculture.*  

###Technical Design
**Activities and Adapters**  
- SearchActivity: contains a searchbar to start a search, with below that a RecyclerView with CardViews that hold images from the Facebook page. An adapter (FacebookImagesAdapter) is used to fill the RecyclerView with the items from Facebook.  
![SearchActivity](/doc/searchactivity28jan2016.jpg)  

- ResultsActivity: contains a gridview in which each item has an ImageView that contains a thumbnail of the searchresult and a TextView with the artistname of the searchresult. There is also a searchbar in this activity so the user can quickly enter a new search. At the bottom of the Activity are two buttons to navigate between the results-pages. An Adapter (ResultsAdapter) is used to fill the gridview with the searchresults. None of the searchresults are saved to local storage. Everything is only cached.  
![ResultsActivity](/doc/resultsactivity28jan2016.jpg)  

- SelectedActivity: Shows the selected image from the ResultsActivity with below that metadata on the image in a TableLayout. Between the ImageView and the TableLayout is a button that launches the Image Editor.  
![SelectedActivity](/doc/selectedactivity28jan2016.jpg)  

- Image Editor: Is completely created by the [Adobe SDK](https://creativesdk.adobe.com/). The activity gets started from the SelectedActivity which sends the image url along with the intent to start a new activity. The url is used by the SDK to get the image. After editing is done, the Image Editor sends the edited image back to the SelecetedActivity where the user will be asked in a Dialog if they only want to save the image or would also like to share. The image gets saved to local storage on the Android Device.  
![ImageEditor](/doc/imageeditor28jan2016.jpg)  

- Share Intent: If the user chooses to share the image, a Share Intent will be launched.  
![ShareIntent](/doc/shareintent28jan2016.jpg)  

**Classes**  
- ImageRetriever: Makes calls to the collection-image endpoint of the Rijksmuseum API. This class is used in ResultsActivity to get the urls for the thumbnails of the searchresults.
- MyApplication: Used by various classes to get the Application Context. Also used by the Adobe SDK to initialize it.
- Parser: Used by various classes to parse JSONresponse and retrieve specific data from it (i.e. a String[] of artistnames for the ResultsActivity GridView).
- QueryMaker: used to build requesturls which are used to make the networkrequest. Input for the querymaker are searchtype (defining which endpoint of the Rijksmuseum API we want to call), searchwords (and in the case of the collection endpoint also page number). It returns Strings representing urls.
- VolleySingleton: manages the networksrequest in a queue (we only want one queue, thus it needs to be a singleton). It also manages the loading of images from url (ImageLoader).

**Requirements**  
In order for the app to work correctly there needs to be an internet connection. Everytime a http request gets made the connectivity will be checked. If there is no internet connection the user will be notified of it with a Toast.  

Permissions needed:
- `"android.permission.INTERNET"`: To access the internet
- `"android.permission.ACCESS_NETWORK_STATE"`: To check the state of the internet connection
- `"android.permission.WRITE_EXTERNAL_STORAGE"`: To save the edited image to local storage


###The Process
**Important Changes and Arguments for the Changes**  
- In the beginning I wanted to code the image editor myself. I eventually went with the Adobe SDK since this would save me a lot of time, but also made more sense. The editor of Adobe worked way better then I could ever code an editor myself. It left me with a lot more time to focus on other things I found important.
- In the first week I had created a UML diagram (see DESIGN.md) with the classes I planned on using. The classes I have eventually created look nothing like the classes in that diagram. Reasons for this great deviation is that I had no idea in the first week how Volley worked. Upon implementing Volley it quickly became clear I had to change things drastically. Also the ArtRecord class is completely gone. Reason for this is that I thought I would save the metadata longer or would create a ArtRecord object for each thumbnail in the searchresults. In the end this did not seem efficient since I actually needed String[]s of the same metadata (i.e. only artistnames) to use in the Adapter to fill the GridView.
- I thought I would have to merge imagetiles for the thumbnails. Eventually I did not have to do this which saves me a lot of calculations and network requests, but does make the app look less good on for example tablets. (We only use one tile which does show most of the image, but does have a low resolution.)
- Eventually there were also some changes I made due to a lack of time:
	- Removed support for landscape orientation
	- I did not plan to use buttons to navigate through the searchresults in the ResultsActivity. I wanted to append new 	searchresults to the GridView (instead of a new GridView) when the user scrolled almost to the end of it. But with the limited time left, I decided that for the deadline I would just use the buttons instead.

**Challenges**  
It was a hassle to get the thumbnail images in the GridView of the ResultsActivity. It became clear the Rijksmuseum API is not tailored at all for Android. It was very difficult to get an appropriate image from the collection-image endpoint (for all issues I ran into see PROCESS.md). I struggled with this for many many hours. Eventually I did find a solution, but for this I had to hardcode some dimensions. This does make the app a lot less suitable for for example tablet. 

The recycling of the GridView is also something I spend many hours on. The app loads the images mostly correctly but sometimes with the recycling the view did not get refreshed or the old image would linger in there for a while. I mostly solved this with some work arounds, but the way the GridView gets filled first is very spastics (views flying over the screen until all the network requests are complete). To hide this I wanted to use a loading screen, but I could not figure out a way to check if all the requests are done. The problem for me with this was that the requests were not being executed on the UI theard and that Volley does not have method to check if the Requestqueue is empty/finished. I have now hardcoded the time the loading screen is displayed, but it would be better ofcourse if the loadingscreen would last until all network requests are done. 

###Final Solution
