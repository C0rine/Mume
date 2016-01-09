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
- Added layouts for landscape orientation (gridview has 3 colums instead of 2 for better overview in landscape orientation)
