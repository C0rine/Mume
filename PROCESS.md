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

