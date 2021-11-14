NPC Skin Converter is a utility I developed to convert LOTR Mod NPC skins from the old Legacy format (64x64) into the new Renewed format (128x64, incorporating features from the modern player skin format) which will be in place following Renewed 5.1.

Maybe you will find it useful too.

To run, 'mvn package' to get the .jar, then put it in the folder containing the skins you wish to convert, and run using 'java -jar npc-skin-converter-[version].jar [skin-type]'

Skin types are man, elf, hobbit, dwarf, orc.

The program will create an /output folder as a subdirectory and put the converted skins in there. If any of the output files already have a file existing at their path, the converter will skip that file to avoid accidentally overwriting work. So if you want to run the program again you'll need to clear the contents of the /output folder.
