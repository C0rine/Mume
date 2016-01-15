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
- Set the images in the imageview of the gridview
- Made a custom ImageView called SquareImageView which will make sure the images in the tiles of the gridview are always square. If the image is not square itself it will be cropped to center.
  
The scrolling of the gridview does not go really smooth. I have used a placeholder image to fill the image with while in the background the actual artwork image is being retrieved. It still was not really smooth since images would just randomly pop up in the imageview. To make this look less hectic and unsettling for the user I have used a fade-in animation to load the images in the imageview.  
  
I have spend a lot of hours on the recycling of views in the gridview ([link]()). For now I have saved the images in cache, but when the user scrolls, there are still network requests being made. This is a bit inconvient when the user scrolls up and down. But since Android implemented it this way I assume for now this is how it is supposed to work. There are still some glitches in the UI when loading the tiles for the first time, but I will try to fix these tomorrow. 
