Shitty scrapper for my personal needs

**CAPABILITIES**
* Download video from VK in highest available resolution
* If a video is an embedded Youtube video - print link to console

**USAGE**
* Paste an array of links to variable links in `Main.java`
* Run `Main.java` from IDE or console.

**ADD AUTH**

If video only available to logged in users or certain profiles
* Open VK and log in
* Open browser's dev tools
* Make a request for any page
* Copy header "Cookie" (`remixlang=3...`)
* Paste to variable cookie

**TODO**
* Read links from file on disk
* Obtain cookie by providing username & password
* Download youtube videos
* Add option to provide link to album - parse and download all video links on it
* Migrate to VK API?
* Error handling