# YSSWidget
Simple RSS Widget for Android based on SAX.

Despite RSS is pretty deprecated and Atom is much better, the task was to build this.

It does:
-    take entered URL from settings and checks it
-    throws an error if URL is invalid
-    load XML file
-    parcel it and transfers into array
-    give to an object
      which subsequently transfers it to widget
-    widget shows message title and its text
-    user can navigate between messages using buttons
-    update every minute even if device is sleeping
-    translated to Russian
    
    
It does not (yet):

-    work with XML tags such as link, pubDate etc.
-    handle images
-    throw errors about nothing to show on entered URL
-    throw errors about failed loading
-    show loading animation
