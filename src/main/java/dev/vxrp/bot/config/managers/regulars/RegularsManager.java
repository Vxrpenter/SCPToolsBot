package dev.vxrp.bot.config.managers.regulars;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class RegularsManager {
    public RegularsManager(Path path) throws IOException, FileNotFoundException {
        //Folder Creation
        String folderPath = path+"/regulars/";
        new File(folderPath).mkdir();

        //Base Folder Creation
    }
}
