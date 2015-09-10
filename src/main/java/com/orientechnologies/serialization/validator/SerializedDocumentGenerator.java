package com.orientechnologies.serialization.validator;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.serialization.serializer.record.ORecordSerializer;
import com.orientechnologies.orient.core.serialization.serializer.record.ORecordSerializerFactory;
import com.orientechnologies.orient.core.serialization.serializer.record.binary.ORecordSerializerBinary;

import java.io.*;

public class SerializedDocumentGenerator {

  public static void main(String[] args) throws IOException {
    SerializedDocumentGenerator serializer = new SerializedDocumentGenerator();
    String formanter;
    if (args.length > 0)
      formanter = args[0];
    else
      formanter = ORecordSerializerBinary.NAME;
    serializer.generateDocuments(formanter);
  }

  public void generateDocuments(String formanter) throws IOException {
    File root = new File("./source");
    File dest = new File("./binary");
    if (dest.exists()) {
      String[] files = dest.list((dir, name) -> name.endsWith(".bin"));
      if (files != null)
        for (String file : files)
          new File(dest.getPath() + "/" + file).delete();
    } else
      dest.mkdir();
    System.out.println(root.getAbsolutePath());
    if (!root.exists())
      throw new IOException(" source folder doesn't exist");
    String[] list = root.list((dir, name) -> name.endsWith(".json"));
    for (String fileName : list)
      generateDocument(root, dest, fileName, formanter);
  }

  public void generateDocument(File root, File dest, String file, String formanter) throws IOException {
    ODocument doc = new ODocument();
    doc.fromJSON(new FileInputStream(root.getPath() + "/" + file));
    String name = file.substring(0, file.length() - ".json".length());
    String destName = dest.getPath() + "/" + name + ".bin";
    ORecordSerializer serializer = ORecordSerializerFactory.instance().getFormat(formanter);
    FileOutputStream output = new FileOutputStream(destName);
    output.write(serializer.toStream(doc, false));
    output.close();
    System.out.println("generated :" + destName);
  }

}
