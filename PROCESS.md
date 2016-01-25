#Day 1
**Mon 4 Jan 2016**   
Made the first sketches of UI (image below) and wrote the proposal (see [README.md](https://github.com/C0rine/Mume/blob/master/README.md)). 
  
![Preliminary sketches](/doc/preliminarysketches.jpg)  

I have looked at several apps of museums and meme-generators for inspriration for these preliminary UI designs.

#Day 2
**Tue 5 Jan 2016**  
Started writing the [Design Document](https://github.com/C0rine/Mume/edit/master/DESIGN.md). I did a lot of research into how to implement the Rijkmuseum API and which data I want to retrieve from it. I also looked in more detail at how to code image editing in Android. I came across several things I probably need to add to the app, that I had not though about before:
- Help page
- View random artwork button in the search-activity
- Naming the Rijksmuseum on selected-activity
- Maybe, if there is enough time, also a button to save the edited image to local storage
  
Something I also need to figure out is how I am going to implement multiple pages of seachresults both in coding and in the UI.

#Day 3
**Wed 6 Jan 2016**  
- Made a definite decision to only use English in the app.
- Decided to hold off creating a "see random artwork"-button. I will only make this if I have enough time left after making the MVP.
- Decided to also hold off the possibility to save the image to local storage.
- Decided to not add a separate "Help"-activity, but to put this in dialogboxes.
- I have also made a definite decision to have a "selected"-activity before the "edit"-activity. This way the user can first view an image in a higher resolution and only if he/she likes it, download the image and edit it. Without this step in between I am afraid that there will be too many unecessary downloads.

I have also made a first version of a class diagram:
![Class Diagram](/doc/classdiagram.jpg) 

#Day 4
**Thu 7 Jan 2016**  
- Completed DESIGN.md by making the advanced sketches in Photoshop.
- I have made layouts and activities for two of the four activities to be made.
- Instead of using a `ListView` I have decided to now use a `GridView` to display the searchresults. This better facilitates the grid-like way I wanted to diplay the results anyway.

#Day 5
**Fri 8 Jan 2016**  
Today I completed making the layout for the other two activities. The edit-activity is still not fully complete yet though since I am not sure what functionalities will exactly be implemented here. I will first implement the Rijksmuseum API, the searchfunction and showing the searchresults before moving on to the editing. I have also implemented the share-intent and help-dialog. The UI is mostly finished (at least for now), so I can really start focussing on the API implementation next week.
  
I have refactored the code I had so far. I have added plenty comments and removed useless code. I have set some standards/conventions for myself for uniform code:
- In the layout XML files all `views` and `viewgroups` will show the `id`-tag at the top so the `id`s will be easy to look up.
- Format of the `id`: "name_viewtype" in lowercase (i.e. artimage_imageview)
- I have also set a format for string resources: "name_viewtype_texttype" in lowercase (i.e. search_edittext_hint or share_button_text)

Today we had presentations. I got a nice tip to use [Volley](http://developer.android.com/training/volley/index.html) for the API requests. I will look further into this this weekend or next week.
  
#Weekend
**Sat 9 and sun 10 Jan 2016**  
- Added layouts for landscape orientation (gridview has 3 colums instead of 2 for better overview in landscape orientation).
- Followed some Android Volley [tutorials](https://www.youtube.com/watch?v=ohkPZw-gY3g&index=29&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD) and read the [documentation](http://developer.android.com/training/volley/index.html).
- Made a start on implementing Volley.

#Day 6
**Mon 11 Jan 2016**  
- Followed more [tutorials](https://www.youtube.com/watch?v=T4SF7S6pYfE&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=31) on Volley.
- Made more progress on implementing Volley. I can now make requests to the Rijksmuseum website to get JSON data (JSONobject request and String request).
- Started on coding a way to retrieve images using Volley.
- Created a Artrecord-class that will be used to populate the grid view in results-activity. The instances of the artrecord-class will be created with the data retrieved from the Rijksmuseum API. 
  
Tomorrow I will start on writing code to parse the JSON response from the Rijksmuseum API.

#Day 7
**Tue 12 Jan 2016**  
- Started on parsing of the JSON with a [tutorial](https://www.youtube.com/watch?v=5GzVtP0IODU&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=37).
- Added a Searcher-class to make queries
- I have also added a Parser-class to parse the JSON response
- The searchbar has been connected to the Searcher class and Volley. Requests can be made. The artistnames in the first page of the results are sent to the Grid View adapter and are now shown there.
  
There is still a lot of work to do, but I am very happy that I have managed to get the API partially implemented and can at least show some results. The UI does not look ideal at this moment, and the artistnames that are now displayed in the Gridview have to be formatted better, but I will tackle these problems later on. Tomorrow I will first focus on also retrieving the images from the Rijksmuseum API and showing them in the gridview (right now a standard Nightwatch image from the resources folder is displayed):
  
![Results Activity 12 jan](/doc/resultsactivity12jan2016.jpg) 

#Day 8
**Wed 13 Jan 2016**
- Started with refactoring of my code, to keep the overview.
- Along with the artistnames I can now also retrieve the objectnumbers of each searchresult (we need this to retrieve thumbnails).
- With the objectnumber also retrieved I started on the very long process of getting the image url for the thumbnail. A new request has to be made for each thumbnail. I was very buggy at the start, I ran into multiple problems. After hours of coding and mostly bugfixing I managed to get the image url for each thumbnail into the separate gridview items. (The artistname is still there, but just not visible in the UI). 
  
![Results Activity 13 jan](/doc/resultsactivity13jan2016.jpg)  
  
Design decisions:  
- In the results of the request to get the artistnames is also an image. Why not use that? --> That image is (on average) 2000 x 3000 px. To load an image of this size for each searchresults would take a lot of time, but moreover will cost the user a lot of MB's if he/she is not connected to WiFi.
- So I use the collection-image endpoint of the Rijksmuseum API. A request to this endpoint is made using the objectnumber. The reponse is a list of levels: ` These levels have a number of tiles. The tiles form the full image. You can choose the right level by using the width and height. They describe the total resolution of the chosen level. You can also select a level by name. Level z0 contains the largest available image and z6 the smallest.` ([API documentation](http://rijksmuseum.github.io/)) For now I use the first tile of the highest level, since this was easy to retrieve. Ideally I'd want to use the full image of the smallest level, but not each image has z6 as smallest level. Some only go to z4. So I will create a new method to get a full small-sized thumbnail later. 

#Day 9
**Thu 14 Jan 2016**  
- Refactored code. I have changed some class names to better suit their actual functions.
- Set the images in the imageview of the gridview (this is still only the first tile of the highest level (see previous day log), I will write a method for a full image later).  
![Results Activity 14 jan](/doc/resultsactivity14jan2016.jpg)  

- Made a custom ImageView called SquareImageView which will make sure the images in the tiles of the gridview are always square. If the image is not square itself it will be cropped to center.
  
The scrolling of the gridview does not go really smooth. I have used a placeholder image to fill the image with while in the background the actual artwork image is being retrieved. It still was not really smooth since images would just randomly pop up in the imageview. To make this look less hectic and unsettling for the user I have used a fade-in animation to load the images in the imageview.  
  
I have spend a lot of hours on the recycling of views in the gridview ([link](https://www.udacity.com/course/viewer#!/c-ud853/l-1395568821/m-1601259313)). For now I have saved the images in cache, but when the user scrolls, there are still network requests being made. This is a bit inconvient when the user scrolls up and down. But since Android implemented it this way I assume for now this is how it is supposed to work. There are still some glitches in the UI when loading the tiles for the first time, but I will try to fix these tomorrow.  
  
I have branched my repo since I tried another LruCache approach to fix the glitches in de gridview. It gave me more glitches. I thought however I might need the code later so I saved it in a commit on a different branch.

#Day 10
**Fri 15 Jan 2016**  
- Added ProgressBar (loading circle) to the Results-Activity to further smoothen the UI. This loading circle now is showing for a fixed 1500ms to give the gridview behind it some time to load. Ideally I would like this to be the actual time to load, but I will implement this probably in the last week.
- I have implemented a way to get the data from the collection-detail endpoint of the Rijksmuseum API. The request is made when the user presses one of the tiles and goes to the Selected-Activity. So the artist name, materials, dating, etc.. can be viewed now in the Selected-Activity. What is left to do is to also show the appropriate image. I will try to implement this upcoming weekend.

#Weekend
**Sat 16 Jan and Sun 17 Jan 2016**  
I have started using GitHub Issues to remind myself of what I still need to do, but also to document any bugs I find now that want to fix in the last week. Also on friday I said I wanted to write a method to get a whole image (instead of one tile). I did not have the time/energy to do this. I only wrote some pseudocode for it. What I did do:  
- Added onTouchListener to the search-icon in the editText used for searching. The user can now also click the icon to start the search.
- I have also implemented the searchbar in the results-activity. The user can now immediatly start a new search from within this activity. This bar now also shows which words were searched when the result is shown.
- Both the on-screen keyboard for the search-activity and the results-activity have been changed to show a 'Go' button instead of a return button. This 'Go' button can also be used to start a new search.

#Day 11
**Mon 18 Jan 2016**  
- I have written a method that retrieves a better tile to be shown in the results-activity:  
![Results Activity 18 jan](/doc/resultsactivity18jan2016.jpg)  
- I had decided not to get several tiles and try to merge them, but instead find the lowest level of the image in which the image would still be at least 250 x 250 px (enough to fill a thumbnail). From that level I would then take the first tile. This saves me a lot of network requests to retrieve multiple tiles for each thumbnail and also a lot time that would be needed to both retrieve the bitmaps and merge them.
- I tried to use NetworkImageView for the thumbnails (for better efficiency) but this gave multiple glitches (i.e. some images would not load anymore), so I decided to just keep the original code.
- Clicking on a result now also sends an image to the selected-activity. In this activity it gets placed in a TouchImageView (a custom [ImageView class](https://github.com/MikeOrtiz/TouchImageView) by Michael Ortiz). TouchImageView allows the user to also zoom the image and pan it.  
![Selected Activity 18 jan](/doc/selectedactivity18jan2016.jpg)  
- Added 'sort by relevance' to the request being made to the Rijksmuseum API.
- Fixed some minor lay-out issues.

Tomorrow I will stop trying to fix the bugs that are still left in the results-activity and selected-activity. They both mainly function and most of the bugs are just UI issues. In the last week I will have time to fix them. Right now I want to start focussing on the image-editting. I will try to implement the [Aviary SDK](https://developers.aviary.com/) (and not try to program this myself).

#Day 12
**Tue 19 Jan 2016**  
Started on [Aviary](https://developers.aviary.com/) / [Adobe Creative SDK](https://creativesdk.adobe.com/) for Android:  
- Added the SDK to the project and authenticated it.
- Added the image-editing Activity from the SDK.
- Implemented it.  
The whole implemention of the SDK was so much more easy than I expected! I thought it would take me all week. There all still some bugs, but I think I could fix those tomorrow.  
Since this went so easy and fast I might also implement a Facebook API to view the art memes from the [official Facebook page](https://www.facebook.com/classicalartmemes).

#Day 13
**Wed 20 Jan 2016**  
- Fixed one major bug with Aviary SDK by using the MediaStore. Now the image that has been edited with the SDK can be shared trough a share-intent. The main functionality of the app is hereby done. The UI can use a lot of improvement and there are several bugs, but all the MVP functionalities are done.
- Customized the tools provided by the SDK.
- Started on implementing the Facebooks API: it's initialized and authenticated.
- Implemented Facebook user login.  

#Day 14
**Thu 21 Jan 2016**  
Mostly worked on implementing the Facebook API to display the images from the Classical Art Memes FB page.  
- Implemented a method to get the image urls for the photos in the album.
- Use CardView to display the images.
- Fixed the layout on the search-activity with a header in the scrollable RecyclerView.

#Day 15
**Fri 22 Jan 2016**  
Got some useful tips from the group during presentation:  
- If the image gets saved two times, why don't just delete the wrong one.
- Check the app on another device with a different resolution (for lay-out purposes).  

I have worked a little on the app:  
- Added date and time-stamp to the FB images
- Made some layout changes for Search Activity
- Added the caption of the images to the FB images (if there is one)

#Weekend
**23 and 24 Jan 2016**  
Fixed the error that occured during the Facebook login that caused the app to crash.

#Day 16
**25 Jan 2016**  

