package br.com.marcosoft.apropriator.util;
import java.io.File;

import org.mozilla.universalchardet.UniversalDetector;

public class CharsetDetector {
    
  public static String detect(File file) throws java.io.IOException {
    byte[] buf = new byte[4096];
    
    java.io.FileInputStream fis = new java.io.FileInputStream(file);

    // (1)
    UniversalDetector detector = new UniversalDetector(null);

    // (2)
    int nread;
    while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
      detector.handleData(buf, 0, nread);
    }
    // (3)
    detector.dataEnd();
    // (4)
    String encoding = detector.getDetectedCharset();
    // (5)
    detector.reset();
    
    fis.close();
    
    return encoding;

  }
}