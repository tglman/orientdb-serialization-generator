package com.orientechnologies.serialization.validator;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.ridbag.ORidBag;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ODocumentInternal;
import com.orientechnologies.orient.core.serialization.serializer.record.ORecordSerializer;
import com.orientechnologies.orient.core.serialization.serializer.record.ORecordSerializerFactory;
import com.orientechnologies.orient.core.serialization.serializer.record.string.ORecordSerializerJSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tglman on 08/10/15.
 */
public class SerializedDocumentGeneratorSchema {

  public static void main(String[] args) throws IOException {

    ODatabaseDocument db = new ODatabaseDocumentTx("memory:dump");
    db.create();
    try {
      OGlobalConfiguration.RID_BAG_EMBEDDED_TO_SBTREEBONSAI_THRESHOLD.setValue(-1);
      ORecord doc = db.load(new ORecordId(0, 1));
      ORecordSerializerFactory factory = ORecordSerializerFactory.instance();
      for (ORecordSerializer serializer : factory.getFormats()) {
        if (serializer instanceof ORecordSerializerJSON)
          continue;
        File dest = new File("./" + serializer);
        if (dest.exists()) {
          String[] files = dest.list((dir, name) -> name.endsWith(".bin"));
          if (files != null)
            for (String file : files)
              new File(dest.getPath() + "/" + file).delete();
        } else
          dest.mkdir();
        String destName = dest.getPath() + "/schema.bin";
        FileOutputStream output = new FileOutputStream(destName);
        output.write(serializer.toStream(doc, false));
        output.close();
      }

    } finally {
      db.drop();
    }

  }


}
