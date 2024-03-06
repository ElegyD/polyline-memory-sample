# Polyline memory leak sample

Sample code for issue: https://issuetracker.google.com/issues/318773921

To change the renderer, edit MapsActivity#L31  
The polyline in this sample includes a pattern, which increases the memory usage even more with the latest renderer.  
To disable the pattern, comment MapsActivity#L82

Run the app and check the Logcat:  
The app adds a polyline every 5 seconds, removing the previously added one and logs the allocated memory before and after adding the polyline, then substracing the values to show the increase in allocated bytes.

With `MapsInitializer.Renderer.LATEST`:
```
2024-03-06 11:16:01.984  MapsActivity  me.elegyd.polylinesample  D  14784416 bytes allocated
2024-03-06 11:16:02.023  MapsActivity  me.elegyd.polylinesample  D  14878624 bytes allocated, increased by 94208 bytes
2024-03-06 11:16:07.028  MapsActivity  me.elegyd.polylinesample  D  14960544 bytes allocated
2024-03-06 11:16:07.092  MapsActivity  me.elegyd.polylinesample  D  15054752 bytes allocated, increased by 94208 bytes
2024-03-06 11:16:12.098  MapsActivity  me.elegyd.polylinesample  D  15120288 bytes allocated
2024-03-06 11:16:12.164  MapsActivity  me.elegyd.polylinesample  D  15214496 bytes allocated, increased by 94208 bytes
2024-03-06 11:16:17.170  MapsActivity  me.elegyd.polylinesample  D  15280032 bytes allocated
2024-03-06 11:16:17.234  MapsActivity  me.elegyd.polylinesample  D  15374240 bytes allocated, increased by 94208 bytes
```

With `MapsInitializer.Renderer.LEGACY`:
```
2024-03-06 11:22:19.461  MapsActivity  me.elegyd.polylinesample  D  7143968 bytes allocated
2024-03-06 11:22:19.469  MapsActivity  me.elegyd.polylinesample  D  7160352 bytes allocated, increased by 16384 bytes
2024-03-06 11:22:24.475  MapsActivity  me.elegyd.polylinesample  D  7345632 bytes allocated
2024-03-06 11:22:24.491  MapsActivity  me.elegyd.polylinesample  D  7362016 bytes allocated, increased by 16384 bytes
2024-03-06 11:22:29.498  MapsActivity  me.elegyd.polylinesample  D  7563632 bytes allocated
2024-03-06 11:22:29.514  MapsActivity  me.elegyd.polylinesample  D  7580016 bytes allocated, increased by 16384 bytes
2024-03-06 11:22:34.521  MapsActivity  me.elegyd.polylinesample  D  7748864 bytes allocated
2024-03-06 11:22:34.537  MapsActivity  me.elegyd.polylinesample  D  7765248 bytes allocated, increased by 16384 bytes
```