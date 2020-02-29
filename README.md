# Extract relevant text from a story copied from a web page

I'm a great fan of tor.com and tor-online.de. Tor publishes a short story on both websites each week. I usually save the story as a pdf which I then convert to epub using Calibre.
The epub files are stored on my Dropbox account so I can read them later. The problem is that pdf's tend to be rather large where the text version, copy-pasted from the web page, is hard to read 
because of all the links and comments it contains. So this utility removes all the stuff I don't need and creates a properly named text file.
This file can then be imported into Calibre and then the process runs as before.

## build 

```bash
mvn clean install
```

## run

```bash
./bin/run.sh <filename>
```

where filename should end in .de (for text from tor-online.de) or .com (for text from tor.com)
 