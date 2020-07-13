Small scrapper for my personal needs

### **CAPABILITIES**
* [VK](https://www.vk.com): Download video from VK in highest available resolution
* [VK](https://www.vk.com): If a video is an embedded Youtube video - print YT link to console
* [itch.io](https://itch.io): Claim all games from bundle to your account

### **USAGE**
* Download latest version from [release page](https://github.com/ButtPirate/scrapper/releases)
* Create file `links.txt` in the same folder as downloaded .jar file
* Paste any supported links to file `links.txt`. One link on one line, no separators. See section below for all supported link formats
* Optional: if resource needs authorization to make request, create file `cookies.txt` in the same folder and paste cookie from your browser. See section below for guidance
* Open console, navigate to folder containing .jar and type `java -jar scrapper_{version}.jar`

### **COOKIE FORMATS**
Don't add header name e.g `Cookie: ...`, just paste cookie body from browser
##### *VK*
 
````remixlang=3; remixstid=...; remixflash=0.0.0; remixscreen_width=1280; remixscreen_height=720; remixscreen_dpr=2.5; remixscreen_depth=24; remixscreen_orient=1; remixscreen_winzoom=1; remixrt=1; remixgp=...; remixdt=0; remixsid=<...>; remixusid=<...>; remixseenads=1````

##### *Itch.io*

``__cfduid=<...>; itchio=<...>``

### **LINK FORMAT**
* VK: https://vk.com/video-<...>_<...>
* Itch.io: https://itch.io/bundle/download/<...>

### **BUILDING**
* `mvn compile`
* `mvn assembly:single`
