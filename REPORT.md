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


###Challenges

###Final Solution
