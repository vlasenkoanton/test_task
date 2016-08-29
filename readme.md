1. To change index-search properties please see IndexProps and SearchProps classes.
2. ATTENTION! Using default properties indexes are saving in directory "filesystem"/indexes. To change this property
see IndexProps class.
3. UPDATED! Testing of 3 levels depth(213 pages indexed) beginning from the same start url was achieved approximately
7.5x times speed growth in multithreading comparing to single-thread environment. You can specify any depth levels and
it shouldn't throw OutOfMemoryException.