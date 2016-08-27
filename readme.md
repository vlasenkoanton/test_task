1. To change index-search properties please see IndexProps and SearchProps classes.
2. ATTENTION! Using default properties indexes are saving in directory "filesystem"/indexes. To change this property
see IndexProps class.
3. There are 2 variants of multithreading:
    1) Commit "Multithreading v.1". Testing of 3 levels depths and 260 visited links showed approximately 6x times speed
    growth(on my computer). Having tested more than 3 levels depth I got OutOfMemoryException(Leak heap space). This is
    result of storing loaded Jsoup pages in memory. Second variant of multithreading solves such problem.
    2) Commit "Multithreading v.2(No OutOfMemoryException)". This is the last commit. It is working slower than 1
    variant on small depth levels (I got 4x times speed growth with identical to 1st variant test data) but on big depth
    levels it is working pretty quick and does not throw OutOfMemoryException.

Writing the code I noticed that some google, twitter and facebook pages were loaded for ages(even up to 60 seconds on
one page) when pages from other sites need average 0.5 sec. And it doesn't depend on page content size.