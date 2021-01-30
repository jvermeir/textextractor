# Extract relevant text from a story copied from a web page

I'm a great fan of tor.com and tor-online.de. Tor publishes a short story on both websites each week. I usually save the story as a pdf which I then convert to epub using Calibre.
The epub files are stored on my Dropbox account so I can read them later. The problem is that pdf's tend to be rather large where the text version, copy-pasted from the web page, is hard to read 
because of all the links and comments it contains. So this utility removes all the stuff I don't need and creates a properly named text file.
This file can then be imported into Calibre and then the process runs as before.

## build 

```bash
mvn clean install
```

create a symlink in a folder that is in your path, e.g. in /usr/local/bin

```bash
ln -s /Users/jan/dev/meuk/textextractor/bin/run.sh /user/local/bin/torconvert
```

## run

```bash
./bin/run.sh <filename>
```

where filename should end in .de (for text from tor-online.de) or .com (for text from tor.com)
 
 
## blog

references: https://martinfowler.com/bliki/FluentInterface.html
idea: This way of writing fluent interfaces is not safe because the methods of  `TextExtractor` cannot be called in random order. 
in fact the only way is this:

```java
        TextExtractor text = TextExtractor.loadText(filename, TextSource.whatTypeIsThis(filename))
                .removeHeader()
                .setTitleAndAuthor()
                .removeSecondHeader()
                .removeEndOfFooter();
```

or any prefix of the list.
