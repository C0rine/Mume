# Mume  
Corine Jacobs  
10001326  
Corine_J@MSN.com  
  
Mume (Museum meme) will be an Android App (created with Android Studio) which uses images from the databases of museums (for this project only the database of the Rijksmuseum will be used) to create memes. 

Based on the popular Facebook-page: https://www.facebook.com/classicalartmemes  

User gap: to facilitate making and sharing Classical Art Memes.  
Underlying idea: introduce art history more into popular-/webculture.  

### Features
- Search through (Rijks)museum artworks
- Add text to the images
- Shares images with friends 

### Preliminary Sketches  
![Sketch 1](/doc/img.jpg)  

### Datasources  
For this project I have chosen the database of the Rijksmuseum since they provide a clear API with plenty documentation and examples. Moreover: "All data and all images made available through the API are either in the public domain or are subject to a CC0 license. The data and images are royalty-free and may be copied, distributed, modified and used without the permission of the Rijksmuseum.". More information on the API: https://www.rijksmuseum.nl/en/api.

### Decomposition  
- Implementing the Rijksmuseum API 
- Connect the feedback of a query using the API to the UI
- An activity that takes one image and allows you to edit an image
- A "Share Activity" that facilitates sharing the image to the web and other apps already installed on the Android device (http://developer.android.com/training/sharing/send.html --> "Send Binary Content").

### Potential Problems  
I'm not sure wether this app will be diffucult or very easy to make. 
In case it is diffucult:
- I plan to code the option to add text to the image myself, but if this appears to be to difficult I could see if it is more easy to use Aviary SDK for the image editing (https://developers.aviary.com/).

In case I have a lot of time left and the project was easy / features to add after the project is done:
- Add databases of other museums (or add Adlib API, Adlib is a system used by most(/all) big museums for collection management: http://api.adlibsoft.com/site/). 
- Add an activity to view the images of the Classical Art Memes Facebookpage (that stays up to date by using an API to get the images from the photogallery of the Facebookpage: https://developers.facebook.com/docs/graph-api/reference/v2.5/album).

### Review of similar applications
An application that lets you create memes: https://play.google.com/store/apps/details?id=com.zombodroid.MemeGenerator&hl=en
